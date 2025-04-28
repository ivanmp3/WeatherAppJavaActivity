package com.example.weatherappjavaactivity.ui;

import com.example.weatherappjavaactivity.data.ForecastItem; // <-- Імпорт старої моделі
import java.util.List;

public abstract class WeatherUiState {
    private WeatherUiState() {}
    public static final class Loading extends WeatherUiState {}

    // Переконайся, що тут використовується ForecastItem
    public static final class Success extends WeatherUiState {
        private final List<ForecastItem> forecastList; // <-- МАЄ БУТИ ForecastItem

        public Success(List<ForecastItem> list) { // <-- МАЄ БУТИ ForecastItem
            this.forecastList = list;
        }

        public List<ForecastItem> getForecastList() { // <-- МАЄ БУТИ ForecastItem
            return forecastList;
        }
    }

    public static final class Error extends WeatherUiState {
        private final String message;
        public Error(String msg) { this.message = msg; }
        public String getMessage() { return message; }
    }
}