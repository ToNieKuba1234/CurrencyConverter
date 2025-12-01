package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import api.RateAPI;

//Main Window Handling - extending JFrame
public class ConverterGUI extends JFrame {
    int windowWidth = 832;
    int windowHeight = 468;

    //Colors
    Color gray = new Color(100, 100, 100);
    Color black = new Color(28, 28, 28);
    Color blue = new Color(0, 102, 255);

    //Test properties
    String[] currencies = {"PLN", "USD", "EUR", "GBP"};

    //GUI elements
    JPanel titlePanel = new JPanel();
    JPanel converterPanel = new JPanel();

    JPanel centerContainer = new JPanel();

    JLabel titleLabel = new JLabel();
    JLabel detailsLabel = new JLabel();

    AmountField amountFieldA = new AmountField("0");
    JComboBox<String> currencyFromBox = new JComboBox<>(currencies);
    JButton swapButton = new JButton();
    AmountField amountFieldB = new AmountField("0");
    JComboBox<String> currencyToBox = new JComboBox<>(currencies);


    public ConverterGUI() throws IOException {
        //Window prop settings
        setTitle("Przelicznik Walut");
        setSize(windowWidth, windowHeight);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        //TitlePanel prop settigs
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(detailsLabel, BorderLayout.CENTER);

        //ConverterPanel prop settings
        converterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        converterPanel.add(amountFieldA);
        converterPanel.add(currencyFromBox);
        converterPanel.add(swapButton);
        converterPanel.add(amountFieldB);
        converterPanel.add(currencyToBox);

        //TitleLabel prop settings
        titleLabel.setText("Przelicznik walut - Jakub Kasprzak");
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 32));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        //Calculating program launch time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM HH:mm", new Locale("pl", "PL"));
        String launchTime = LocalDateTime.now().format(formatter);

        //DetailsLabel prop settings
        detailsLabel.setText("Dane aktualizowane na stan : " + launchTime);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        detailsLabel.setHorizontalAlignment(JLabel.CENTER);
        detailsLabel.setForeground(gray);


        //ConverterPanel components settings
        amountFieldA.setPreferredSize(new Dimension(225, 45));
        amountFieldA.setFont(new Font("Arial", Font.PLAIN, 20));

        currencyFromBox.setPreferredSize(new Dimension(75, 45));
        currencyFromBox.setSelectedIndex(0);

        swapButton.setText("⇄");
        swapButton.setPreferredSize(new Dimension(50, 45));
        swapButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        swapButton.setFocusable(false);

        amountFieldB.setPreferredSize(new Dimension(225, 45));
        amountFieldB.setFont(new Font("Arial", Font.PLAIN, 20));
        amountFieldB.setEditable(false);
        amountFieldB.setFocusable(false);

        currencyToBox.setPreferredSize(new Dimension(75, 45));
        currencyToBox.setSelectedIndex(1);


        //CenterContainer prop settings
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.add(converterPanel, new GridBagConstraints());

        //Listening for currency change
        currencyFromBox.addActionListener(e -> {
            try { updateConversion(); } catch (IOException ex) { System.out.println("Err.:" + ex.getMessage()); }
        });
        currencyToBox.addActionListener(e -> {
            try { updateConversion(); } catch (IOException ex) { System.out.println("Err.:" + ex.getMessage()); }
        });

        //Listening for swap
        swapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Swapping currencies
                Object buffer = currencyFromBox.getSelectedItem();

                currencyFromBox.setSelectedItem(currencyToBox.getSelectedItem());
                currencyToBox.setSelectedItem(buffer);

                //Updating the conversion
                try {
                    updateConversion();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //Listening for changes in the AmountField
        amountFieldA.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try { updateConversion(); } catch (IOException ex) { System.out.println("Err.:" + ex.getMessage()); }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try { updateConversion(); } catch (IOException ex) { System.out.println("Err.:" + ex.getMessage()); }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try { updateConversion(); } catch (IOException ex) { System.out.println("Err.:" + ex.getMessage()); }
            }
        });



        //Adding components & making them visible
        add(titlePanel, BorderLayout.NORTH);
        add(centerContainer, BorderLayout.CENTER);

        setVisible(true);

//        Testing the updateConversion() method
//        amountFieldA.setText("1.48");
//        currencyFromBox.setSelectedItem("EUR");
//        currencyToBox.setSelectedItem("GBP");

//        updateConversion();
    }

    //Taking all the data - checking if it can be converted - usingAPI - calculating the result
    //handles all data validation + takes all the data
    void updateConversion() throws IOException {
        String from = Objects.requireNonNull(currencyFromBox.getSelectedItem()).toString();
        String to = Objects.requireNonNull(currencyToBox.getSelectedItem()).toString();
        String text = amountFieldA.getText().trim();

        double amount;
        try {
            amount = Double.parseDouble(text.isEmpty() || text.equals(".") ? "0" : text);
        } catch (NumberFormatException e) {
            amountFieldB.setText("0");
            return;
        }

        if (from.equals(to)) { amountFieldB.setText(removeZeroDecimal(amount)); return; }
        if (amount == 0) { amountFieldB.setText("0"); return; }

        double rate = new RateAPI().getRate(from, to);
        double result = Math.round(rate * amount * 100.0) / 100.0; // zaokrąglenie do 2 miejsc
        amountFieldB.setText(removeZeroDecimal(result));
    }


    //Removes the .0 in the decimal number if it's not necessary
    String removeZeroDecimal(double n) {
        if (n % 1 == 0) {
            return Integer.toString((int) n);
        }
        return Double.toString(n);
    }

}