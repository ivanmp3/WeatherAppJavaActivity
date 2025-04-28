package com.example.weatherappjavaactivity.data;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Клас для одного запису прогнозу (на кожні 3 години)
public class ForecastItem {
    // "dt" - час прогнозу у форматі Unix timestamp (секунди)
    @SerializedName("dt")
    private long dateTimeUnix;

    // "main" - об'єкт з основною інформацією (температура, тиск)
    @SerializedName("main")
    private MainInfo main;

    // "weather" - масив (список) з описом погоди (зазвичай один елемент)
    @SerializedName("weather")
    private List<WeatherInfo> weather;


    // "wind" - об'єкт з інформацією про вітер
    @SerializedName("wind")
    private WindInfo wind;

    // "dt_txt" - дата і час у текстовому форматі "YYYY-MM-DD HH:MM:SS"
    @SerializedName("dt_txt")
    private String dateTimeText;

    // Getters для всіх полів
    public long getDateTimeUnix() { return dateTimeUnix; }
    public MainInfo getMain() { return main; }
    public List<WeatherInfo> getWeather() { return weather; }
    public WindInfo getWind() { return wind; }
    public String getDateTimeText() { return dateTimeText; }
}
