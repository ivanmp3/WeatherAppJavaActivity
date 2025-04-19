package com.example.weatherappjavaactivity.ui;

import com.example.weatherappjavaactivity.data.ForecastItem;
import java.util.List;

// Клас-обгортка для представлення різних станів UI
// Використовуємо статичні вкладені класи замість sealed class з Kotlin
public abstract class WeatherUiState {

    // Приватний конструктор, щоб заборонити створення екземплярів WeatherUiState напряму
    private WeatherUiState() {}

    // Стан завантаження
    public static final class Loading extends WeatherUiState {}

    // Стан успішного завантаження даних
    public static final class Success extends WeatherUiState {
        private final List<ForecastItem> forecastList;

        public Success(List<ForecastItem> list) {
            this.forecastList = list;
        }

        public List<ForecastItem> getForecastList() {
            return forecastList;
        }
    }

    // Стан помилки
    public static final class Error extends WeatherUiState {
        private final String message;

        public Error(String msg) {
            this.message = msg;
        }

        public String getMessage() {
            return message;
        }
    }
}

