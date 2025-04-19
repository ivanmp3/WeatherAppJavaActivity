package com.example.weatherappjavaactivity.network;

import com.example.weatherappjavaactivity.data.WeatherResponse;
import retrofit2.Call; // Важливо: імпортуємо retrofit2.Call
import retrofit2.http.GET;
import retrofit2.http.Query;

// Інтерфейс, що описує запити до OpenWeatherMap API
public interface WeatherApiService {

    // Описуємо GET-запит до ендпоінту "data/2.5/forecast"
    @GET("data/2.5/forecast")
    Call<WeatherResponse> getWeatherForecast( // Метод повертає Call об'єкт для асинхронного запиту
                                              @Query("q") String city,             // Параметр запиту: назва міста
                                              @Query("appid") String apiKey,       // Параметр запиту: API ключ
                                              @Query("units") String units,        // Параметр запиту: одиниці виміру (metric/imperial)
                                              @Query("lang") String lang           // Параметр запиту: мова відповіді (uk/en/ru...)
    );

    // Можна додати інші методи для інших запитів API
}