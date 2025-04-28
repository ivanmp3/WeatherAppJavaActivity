package com.example.weatherappjavaactivity.data.onecall;

import com.google.gson.annotations.SerializedName;

public class WeatherDescription {
    @SerializedName("id")
    private int id;
    @SerializedName("main")
    private String main; // Група параметрів погоди (Rain, Snow, Clouds etc.)
    @SerializedName("description")
    private String description; // Опис погоди
    @SerializedName("icon")
    private String icon; // Код іконки погоди

    // Getters
    public int getId() { return id; }
    public String getMain() { return main; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
}