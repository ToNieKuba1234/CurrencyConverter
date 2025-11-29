package gui;

import javax.swing.JTextField;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

// Custom JTextField class designed for monetary input.
// It handles number validation and placeholder behavior.
public class AmountField extends JTextField implements FocusListener {

    // Variables for placeholder text and colors
    String placeholder;
    Color placeholderColor = Color.GRAY;
    Color foregroundColor = Color.BLACK;

    public AmountField(String placeholder) {
        this.placeholder = placeholder;

        // Initial setup for the field's normal look
        this.setForeground(foregroundColor);
        this.setFont(this.getFont().deriveFont(Font.PLAIN));

        // 1. Apply a DocumentFilter using an anonymous class
        // to control input, blocking non-numeric characters and multiple dots.
        ((AbstractDocument) this.getDocument()).setDocumentFilter(new DocumentFilter() {

            // Validation check: ensures the string is a valid number format (e.g., 123.45)
            private boolean isValid(String str) {
                if(str.isEmpty()) return true;

                // Regex: allows any number of digits, followed by an optional single dot, and more digits.
                return str.matches("\\d*\\.?\\d*");
            }

            // Method executed when user types a character or pastes text
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String currText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currText.substring(0, offset) + string + currText.substring(offset);

                // Only allow the input if the resulting string is a valid number
                if(isValid(newText)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            // Method executed when text is replaced (e.g., highlighting and typing)
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {

                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                // Only allow replacement if the final result is a valid number
                if (isValid(newText)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            // Method executed when text is removed (deletion is usually allowed)
            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });

        // 2. Set the initial placeholder look
        setPlaceholderMode();

        // 3. Add the focus listener to manage placeholder state
        this.addFocusListener(this);
    }

    // --- Placeholder Logic (FocusListener Implementation) ---

    // When the field gains focus (user clicks it)
    @Override
    public void focusGained(FocusEvent e) {
        // If the current text is the placeholder, switch to normal edit mode
        if (super.getText().equals(placeholder)) {
            setNormalMode();
        }
    }

    // When the field loses focus
    @Override
    public void focusLost(FocusEvent e) {
        // If the field is empty after losing focus, restore the placeholder
        if (super.getText().isEmpty()) {
            setPlaceholderMode();
        } else {
            // Ensure visual style is normal if content is present
            this.setForeground(foregroundColor);
            this.setFont(this.getFont().deriveFont(Font.PLAIN));
        }
    }

    // Sets the visual style to placeholder mode (gray, italic text)
    void setPlaceholderMode() {
        this.setText(placeholder);
        this.setForeground(placeholderColor);
        this.setFont(this.getFont().deriveFont(Font.ITALIC));
    }

    // Resets the visual style to normal mode (black, plain text, empty content)
    void setNormalMode() {
        this.setText("");
        this.setForeground(foregroundColor);
        this.setFont(this.getFont().deriveFont(Font.PLAIN));
    }

    // Overridden method to ensure external logic doesn't read the placeholder text
    @Override
    public String getText() {
        String currentText = super.getText();
        if (currentText.equals(placeholder)) {
            return ""; // Return empty string if only the placeholder is visible
        }
        return currentText;
    }
}