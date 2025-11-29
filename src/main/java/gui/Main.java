package gui;

public class Main {
    public static void main(String[] args) {
        //Running the Main Window
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ConverterGUI();
            }
        });
    }
}