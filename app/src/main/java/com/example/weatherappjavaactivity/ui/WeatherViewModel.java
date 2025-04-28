package com.example.weatherappjavaactivity.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// Імпортуємо НОВІ моделі даних
import com.example.weatherappjavaactivity.data.onecall.DailyForecast;
import com.example.weatherappjavaactivity.data.onecall.OneCallWeatherResponse;
import com.example.weatherappjavaactivity.network.RetrofitInstance;
import com.example.weatherappjavaactivity.network.WeatherApiService;

import java.util.List;
import java.util.stream.Collectors; // Для take(7)

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Клас WeatherUiState залишається майже таким же, але Success приймає List<DailyForecast>
public abstract class WeatherUiState {
    private WeatherUiState() {}
    public static final class Loading extends WeatherUiState {}
    public static final class Success extends WeatherUiState {
        // Змінюємо тип списку на DailyForecast
        private final List<DailyForecast> forecastList;
        public Success(List<DailyForecast> list) { this.forecastList = list; }
        public List<DailyForecast> getForecastList() { return forecastList; }
    }
    public static final class Error extends WeatherUiState {
        private final String message;
        public Error(String msg) { this.message = msg; }
        public String getMessage() { return message; }
    }
}


public class WeatherViewModel extends ViewModel {

    private final String apiKey = "YOUR_API_KEY"; // !!! ЗНОВУ ПЕРЕВІР СВІЙ КЛЮЧ !!!

    // Змінюємо тип MutableLiveData для відповідності новому Success стану
    private final MutableLiveData<WeatherUiState> _weatherState = new MutableLiveData<>();
    public LiveData<WeatherUiState> getWeatherState() { return _weatherState; }

    private final WeatherApiService apiService;

    public WeatherViewModel() {
        apiService = RetrofitInstance.getApiService();
        // Координати Одеси (приклад)
        double latitude = 46.4825;
        double longitude = 30.7233;
        fetchWeatherForecast(latitude, longitude);
    }

    // Метод тепер приймає координати
    public void fetchWeatherForecast(double lat, double lon) {
        _weatherState.setValue(new WeatherUiState.Loading());

        // Параметр exclude, щоб не отримувати зайві дані
        String exclude = "minutely,hourly,current,alerts";
        // Викликаємо НОВИЙ метод API сервісу
        Call<OneCallWeatherResponse> call = apiService.getOneCallForecast(
                lat, lon, apiKey, exclude, "metric", "uk"
        );

        call.enqueue(new Callback<OneCallWeatherResponse>() {
            @Override
            public void onResponse(Call<OneCallWeatherResponse> call, Response<OneCallWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Отримуємо список денних прогнозів
                    List<DailyForecast> dailyItems = response.body().getDaily();
                    if (dailyItems != null && !dailyItems.isEmpty()) {
                        // Беремо перші 7 днів (API може повернути 8)
                        List<DailyForecast> weeklyForecast = dailyItems.stream().limit(7).collect(Collectors.toList());
                        _weatherState.setValue(new WeatherUiState.Success(weeklyForecast));
                    } else {
                        _weatherState.setValue(new WeatherUiState.Error("Отримано пустий денний прогноз"));
                    }
                } else {
                    _weatherState.setValue(new WeatherUiState.Error("Помилка сервера OneCall: " + response.code() + " " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<OneCallWeatherResponse> call, Throwable t) {
                _weatherState.setValue(new WeatherUiState.Error("Помилка мережі OneCall: " + t.getMessage()));
            }
        });
    }
}