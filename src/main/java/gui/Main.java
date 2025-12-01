package gui;

public class Main {
    public static void main(String[] args) {
        //Running the Main Window
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ConverterGUI();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}