package com.example.weatherappjavaactivity.network; // ЗАМІНИ на свій пакет

// Імпортуємо нову модель відповіді (створимо її далі)
import com.example.weatherappjavaactivity.data.onecall.OneCallWeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    // Змінюємо ендпоінт на One Call API 3.0
    @GET("data/3.0/onecall")
    Call<OneCallWeatherResponse> getOneCallForecast( // Новий метод та тип відповіді
                                                     @Query("lat") double latitude,      // Параметр: широта
                                                     @Query("lon") double longitude,     // Параметр: довгота
                                                     @Query("appid") String apiKey,       // Параметр: API ключ
                                                     @Query("exclude") String exclude,    // Параметр: що виключити (напр., "minutely,hourly,alerts")
                                                     @Query("units") String units,        // Параметр: одиниці виміру (metric)
                                                     @Query("lang") String lang           // Параметр: мова відповіді (uk)
    );
}