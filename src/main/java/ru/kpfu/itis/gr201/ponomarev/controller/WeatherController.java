package ru.kpfu.itis.gr201.ponomarev.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kpfu.itis.gr201.ponomarev.http.HttpClient;
import ru.kpfu.itis.gr201.ponomarev.http.HttpClientImpl;
import ru.kpfu.itis.gr201.ponomarev.model.Weather;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private static final HttpClient HTTP;
    private static final String API_KEY;

    static {
        HTTP = new HttpClientImpl();
        API_KEY = "bb33039dd12d1198e828074c5db9610e";
    }

    @GetMapping
    public String weather() {
        Map<String, Object> params = Map.of(
                "q", "Kazan",
                "units", "metric",
                "appid", API_KEY
        );
        String response;
        try {
            response = HTTP.get("https://api.openweathermap.org/data/2.5/weather", params);
        } catch (IOException e) {
            return "ERROR: couldn't connect to API.";
        }
        try {
            JSONObject json = new JSONObject(response);
            String name = json.getString("name");
            double temp = json.getJSONObject("main").getDouble("temp");
            double humidity = json.getJSONObject("main").getDouble("humidity");
            String description = json.getJSONArray("weather").getJSONObject(0).getString("description");
            Weather weather = new Weather(name, temp, humidity, description);
            return weather.toString();
        } catch (JSONException e) {
            return "ERROR: couldn't parse response from API. Response: " + response;
        }
    }
}
