package com.example.weatherappjavaactivity.data.onecall;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DailyForecast {
    @SerializedName("dt")
    private long dt; // Unix timestamp, UTC
    @SerializedName("temp")
    private DailyTemperature temp; // Об'єкт з температурою
    @SerializedName("pressure")
    private int pressure; // Тиск, гПа
    @SerializedName("humidity")
    private int humidity;
    @SerializedName("wind_speed")
    private double windSpeed; // Швидкість вітру, м/с
    @SerializedName("weather") // Список з описом погоди (зазвичай 1 елемент)
    private List<WeatherDescription> weather;
    @SerializedName("pop") // Імовірність опадів
    private double probabilityOfPrecipitation;

    // Getters
    public long getDt() { return dt; }
    public DailyTemperature getTemp() { return temp; }
    public int getPressure() { return pressure; }
    public int getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public List<WeatherDescription> getWeather() { return weather; }
    public double getProbabilityOfPrecipitation() { return probabilityOfPrecipitation; }
}