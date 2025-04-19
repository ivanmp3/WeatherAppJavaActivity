package com.example.weatherappjavaactivity.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Клас для створення єдиного екземпляра Retrofit (Singleton)
public class RetrofitInstance {

    // Базова URL API
    private static final String BASE_URL = "https://api.openweathermap.org/";

    // Статична змінна для зберігання єдиного екземпляра Retrofit
    private static Retrofit retrofit;

    // Статичний метод для отримання екземпляра Retrofit
    // Використовує ліниву ініціалізацію (створюється тільки при першому виклику)
    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Встановлюємо базову URL
                    // Додаємо конвертер Gson для перетворення JSON у Java об'єкти
                    .addConverterFactory(GsonConverterFactory.create())
                    .build(); // Створюємо екземпляр Retrofit
        }
        return retrofit;
    }

    // Статичний метод для отримання готового до використання API сервісу
    public static WeatherApiService getApiService() {
        // Створюємо реалізацію інтерфейсу WeatherApiService за допомогою Retrofit
        return getRetrofitInstance().create(WeatherApiService.class);
    }
}