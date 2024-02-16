package ru.kpfu.itis.gr201.ponomarev.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.gr201.ponomarev.http.HttpClient;
import ru.kpfu.itis.gr201.ponomarev.http.HttpClientImpl;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/exchange")
public class ExchangeRateController {

    private static final HttpClient HTTP;
    private static final String API_KEY;

    static {
        HTTP = new HttpClientImpl();
        API_KEY = "19ee0ebf14893805de96f9c87f2a3048";
    }

    @GetMapping
    public String exchange() {
        Map<String, Object> params = Map.of(
                "access_key", API_KEY,
                "source", "RUB",
                "currencies", "USD,EUR"
        );
        String response;
        try {
            response = HTTP.get("http://api.currencylayer.com/live", params);
        } catch (IOException e) {
            return "ERROR: couldn't connect to API.";
        }
        try {
            JSONObject json = new JSONObject(response);
            JSONObject quotes = json.getJSONObject("quotes");
            StringBuilder resultBody = new StringBuilder();
            for (String key : quotes.keySet()) {
                double value = 1.0 / quotes.getDouble(key);
                resultBody.append(key.replace("RUB", "")).append(": ").append(value).append("; ");
            }
            return resultBody.toString();
        } catch (JSONException e) {
            return "ERROR: couldn't parse response from API. Response: " + response;
        }
    }
}
