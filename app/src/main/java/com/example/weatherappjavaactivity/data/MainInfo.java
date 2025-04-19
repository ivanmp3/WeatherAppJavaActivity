package com.example.weatherappjavaactivity.data;
import com.google.gson.annotations.SerializedName;

// Клас для основної погодної інформації
public class MainInfo {
    // "temp" - температура (в Цельсіях, якщо units=metric)
    @SerializedName("temp")
    private double temperature;

    // "pressure" - атмосферний тиск (в гПа)
    @SerializedName("pressure")
    private double pressure;

    // Можна додати інші поля: feels_like, humidity, temp_min, temp_max

    // Getters
    public double getTemperature() { return temperature; }
    public double getPressure() { return pressure; }
}