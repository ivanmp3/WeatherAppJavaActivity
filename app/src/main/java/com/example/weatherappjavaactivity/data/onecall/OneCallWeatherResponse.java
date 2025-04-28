package com.example.weatherappjavaactivity.data.onecall; // Створи підпапку onecall

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OneCallWeatherResponse {
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("daily") // Нас цікавить саме цей список денних прогнозів
    private List<DailyForecast> daily;

    // Getters
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public String getTimezone() { return timezone; }
    public List<DailyForecast> getDaily() { return daily; }
}