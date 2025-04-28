package com.example.weatherappjavaactivity.data.onecall;

import com.google.gson.annotations.SerializedName;

public class DailyTemperature {
    @SerializedName("day")
    private double day; // Денна температура
    @SerializedName("min")
    private double min; // Мінімальна денна
    @SerializedName("max")
    private double max; // Максимальна денна
    @SerializedName("night")
    private double night;
    @SerializedName("eve")
    private double eve;
    @SerializedName("morn")
    private double morn;

    // Getters (додай потрібні)
    public double getDay() { return day; }
    public double getMin() { return min; }
    public double getMax() { return max; }
}