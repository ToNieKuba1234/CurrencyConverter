package gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    //vars
    boolean ratesAreLoaded = false;

    public ConverterGUI() {
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

        swapButton.setText("⇄");
        swapButton.setPreferredSize(new Dimension(50, 45));
        swapButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
        swapButton.setFocusable(false);

        amountFieldB.setPreferredSize(new Dimension(225, 45));
        amountFieldB.setFont(new Font("Arial", Font.PLAIN, 20));
        amountFieldB.setEditable(false);
        amountFieldB.setFocusable(false);

        currencyToBox.setPreferredSize(new Dimension(75, 45));


        //CenterContainer prop settings
        centerContainer.setLayout(new GridBagLayout());
        centerContainer.add(converterPanel, new GridBagConstraints());


        //Adding components & making them visible
        add(titlePanel, BorderLayout.NORTH);
        add(centerContainer, BorderLayout.CENTER);

        setVisible(true);
    }
}