package ru.kpfu.itis.gr201.ponomarev.model;

public record Weather(String city, double temp, double humidity, String description) {

    @Override
    public String toString() {
        return "Weather in " + city + ": " + description + ", temperature " + temp + "°C, humidity " + humidity + "%.";
    }
}
