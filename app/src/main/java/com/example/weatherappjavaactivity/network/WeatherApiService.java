package com.example.weatherappjavaactivity.network;

import com.example.weatherappjavaactivity.data.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * OpenWeatherMap 5-day / 3-hour forecast endpoint
 */
public interface WeatherApiService {

    @GET("data/2.5/forecast")
    Call<WeatherResponse> getWeatherForecast(
            @Query("q") String city,       // city name, e.g. "Odesa,UA"
            @Query("appid") String apiKey, // your API key
            @Query("units") String units,  // "metric" or "imperial"
            @Query("lang") String lang     // e.g. "uk", "en"
    );
}
