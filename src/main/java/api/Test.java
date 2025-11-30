package api;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        RateAPI test = new RateAPI();
        Double rates = test.getRate("USD", "EUR");

        System.out.println(rates);
    }
}