package com.example.weatherappjavaactivity.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// Імпорти для СТАРИХ моделей і UiState, що працює з ForecastItem
import com.example.weatherappjavaactivity.data.ForecastItem;
import com.example.weatherappjavaactivity.data.WeatherResponse;
import com.example.weatherappjavaactivity.network.RetrofitInstance;
import com.example.weatherappjavaactivity.network.WeatherApiService;

import java.util.ArrayList; // Потрібен для створення нового списку
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {

    private final String apiKey = "YOUR_API_KEY"; // !!! НЕ ЗАБУДЬ СВІЙ КЛЮЧ !!!

    // LiveData тепер знову працює з WeatherUiState<ForecastItem> (неявно через Success)
    private final MutableLiveData<WeatherUiState> _weatherState = new MutableLiveData<>();
    public LiveData<WeatherUiState> getWeatherState() {
        return _weatherState;
    }

    private final WeatherApiService apiService;

    public WeatherViewModel() {
        apiService = RetrofitInstance.getApiService();
        // Викликаємо старий метод з назвою міста
        fetchWeatherForecast("Odesa,UA"); // Або інше місто
    }

    // Метод для завантаження 5-денного прогнозу
    public void fetchWeatherForecast(String city) { // Приймає місто
        _weatherState.setValue(new WeatherUiState.Loading());

        // Викликаємо метод getWeatherForecast зі СТАРОГО WeatherApiService
        Call<WeatherResponse> call = apiService.getWeatherForecast(city, apiKey, "metric", "uk");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Отримуємо список ForecastItem
                    List<ForecastItem> allItems = response.body().getList();
                    if (allItems != null && !allItems.isEmpty()) {
                        // --- Фільтрація для отримання денних даних ---
                        List<ForecastItem> filteredList = new ArrayList<>();
                        for (int i = 0; i < allItems.size(); i += 8) { // Беремо кожен 8-й запис (раз на 24 години)
                            filteredList.add(allItems.get(i));
                            if (filteredList.size() >= 5) { // Обмежуємо 5 днями (максимум, що дає API)
                                break;
                            }
                        }
                        // -----------------------------------------
                        if (!filteredList.isEmpty()){
                            // Оновлюємо стан з відфільтрованим списком ForecastItem
                            _weatherState.setValue(new WeatherUiState.Success(filteredList));
                        } else {
                            _weatherState.setValue(new WeatherUiState.Error("Не вдалося відфільтрувати дані"));
                        }
                    } else {
                        _weatherState.setValue(new WeatherUiState.Error("Отримана пуста відповідь від сервера (5-day)"));
                    }
                } else {
                    _weatherState.setValue(new WeatherUiState.Error("Помилка сервера (5-day): " + response.code() + " " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                _weatherState.setValue(new WeatherUiState.Error("Помилка мережі (5-day): " + t.getMessage()));
            }
        });
    }
}