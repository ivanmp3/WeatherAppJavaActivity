package com.example.weatherappjavaactivity.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherappjavaactivity.data.ForecastItem;
import com.example.weatherappjavaactivity.data.WeatherResponse;
import com.example.weatherappjavaactivity.network.RetrofitInstance;
import com.example.weatherappjavaactivity.network.WeatherApiService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {

    // Ваш API ключ
    private final String apiKey = "16007cf6e5b4229927ec316ffda5427b";

    // LiveData для стану UI
    private final MutableLiveData<WeatherUiState> _weatherState = new MutableLiveData<>();
    public LiveData<WeatherUiState> getWeatherState() {
        return _weatherState;
    }

    private final WeatherApiService apiService;

    public WeatherViewModel() {
        apiService = RetrofitInstance.getApiService();
        // За замовчуванням Одеса
        fetchWeatherForecast("Odesa,UA");
    }

    public void fetchWeatherForecast(String city) {
        _weatherState.setValue(new WeatherUiState.Loading());

        Call<WeatherResponse> call = apiService.getWeatherForecast(city, apiKey, "metric", "uk");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ForecastItem> allItems = response.body().getList();
                    if (allItems != null && !allItems.isEmpty()) {
                        // Групуємо за датами та беремо до 4 днів по 4 інтервали кожен
                        Map<String, List<ForecastItem>> dailyMap = new LinkedHashMap<>();
                        for (ForecastItem item : allItems) {
                            String date = item.getDateTimeText().substring(0, 10); // YYYY-MM-DD
                            // Якщо вже зібрано 4 дні — виходимо
                            if (!dailyMap.containsKey(date) && dailyMap.size() == 4) break;
                            // Ініціалізуємо список для дати
                            dailyMap.computeIfAbsent(date, k -> new ArrayList<>());
                            List<ForecastItem> dayList = dailyMap.get(date);
                            // Додаємо до 4 прогнозів на день
                            if (dayList.size() < 4) {
                                dayList.add(item);
                            }
                        }
                        // Об'єднуємо в один список
                        List<ForecastItem> result = new ArrayList<>();
                        for (List<ForecastItem> dayList : dailyMap.values()) {
                            result.addAll(dayList);
                        }
                        if (!result.isEmpty()) {
                            _weatherState.setValue(new WeatherUiState.Success(result));
                        } else {
                            _weatherState.setValue(new WeatherUiState.Error("Не знайдено прогнозів"));
                        }
                    } else {
                        _weatherState.setValue(new WeatherUiState.Error("Отримана пуста відповідь від сервера"));
                    }
                } else {
                    _weatherState.setValue(new WeatherUiState.Error(
                            "Помилка сервера: " + response.code() + " " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                _weatherState.setValue(new WeatherUiState.Error("Помилка мережі: " + t.getMessage()));
            }
        });
    }
}
