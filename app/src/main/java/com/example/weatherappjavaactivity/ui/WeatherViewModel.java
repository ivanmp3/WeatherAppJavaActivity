package com.example.weatherappjavaactivity.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherappjavaactivity.data.ForecastItem;
import com.example.weatherappjavaactivity.data.WeatherResponse;
import com.example.weatherappjavaactivity.network.RetrofitInstance;
import com.example.weatherappjavaactivity.network.WeatherApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {

    private final String apiKey = "16007cf6e5b4229927ec316ffda5427b";

    private final MutableLiveData<WeatherUiState> _weatherState = new MutableLiveData<>();
    public LiveData<WeatherUiState> getWeatherState() {
        return _weatherState;
    }

    private final WeatherApiService apiService;

    public WeatherViewModel() {
        apiService = RetrofitInstance.getApiService();
        fetchWeatherForecast("Odesa,UA");  // Місто за замовчуванням
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
                        int maxCount = Math.min(32, allItems.size());
                        List<ForecastItem> fourDaysForecast = allItems.subList(0, maxCount);
                        _weatherState.setValue(new WeatherUiState.Success(fourDaysForecast));
                    } else {
                        _weatherState.setValue(new WeatherUiState.Error("Порожня відповідь від сервера"));
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
