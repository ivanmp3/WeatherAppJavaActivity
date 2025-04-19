package com.example.weatherappjavaactivity.data;
import com.google.gson.annotations.SerializedName;

// Клас для опису погоди
public class WeatherInfo {
    // "description" - текстовий опис погоди (наприклад, "невеликий дощ")
    @SerializedName("description")
    private String description;

    // "icon" - код іконки погоди (наприклад, "10d")
    @SerializedName("icon")
    private String icon;

    // Getters
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
}