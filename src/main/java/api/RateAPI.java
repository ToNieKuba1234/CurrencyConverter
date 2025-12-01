package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class RateAPI {
    static final String API_URL = "https://api.frankfurter.app/latest?from=USD&to=PLN";

    final OkHttpClient client = new OkHttpClient();
    final Gson gson = new Gson();


    public double getRate(String from, String to) throws IOException {

        if (from == to) {
            return 1;
        }

        String url = String.format(
                "https://api.frankfurter.app/latest?from=%s&to=%s",
                from.toUpperCase(),
                to.toUpperCase()
        );

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Błąd połączenia z API. Kod: " + response.code());
            }

            String jsonBody = response.body().string();
            JsonObject jsonObject = gson.fromJson(jsonBody, JsonObject.class);

            JsonObject rates = jsonObject.getAsJsonObject("rates");

            return Math.round(rates.get(to.toUpperCase()).getAsDouble() * 100.0) / 100.0;
        }
    }
}
