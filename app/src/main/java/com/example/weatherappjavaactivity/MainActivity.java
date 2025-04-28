package com.example.weatherappjavaactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.weatherappjavaactivity.data.ForecastItem;
import com.example.weatherappjavaactivity.databinding.ActivityMainBinding;
import com.example.weatherappjavaactivity.ui.WeatherAdapter;
import com.example.weatherappjavaactivity.ui.WeatherUiState;
import com.example.weatherappjavaactivity.ui.WeatherViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WeatherViewModel viewModel;
    private WeatherAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализируем ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Получаем ViewModel
        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        weatherAdapter = new WeatherAdapter();
        binding.rvWeatherForecast.setLayoutManager(new LinearLayoutManager(this));
        binding.rvWeatherForecast.setAdapter(weatherAdapter);
    }

    private void observeViewModel() {
        viewModel.getWeatherState().observe(this, state -> {
            if (state instanceof WeatherUiState.Loading) {
                // Показать прогресс
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.tvError.setVisibility(View.GONE);
                binding.rvWeatherForecast.setVisibility(View.GONE);

            } else if (state instanceof WeatherUiState.Success) {
                // Спрятать прогресс и ошибку, показать список
                binding.progressBar.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.GONE);
                binding.rvWeatherForecast.setVisibility(View.VISIBLE);

                // Получаем данные и передаём адаптеру
                WeatherUiState.Success success = (WeatherUiState.Success) state;
                List<ForecastItem> list = success.getForecastList();
                weatherAdapter.submitList(list);

            } else if (state instanceof WeatherUiState.Error) {
                // Спрятать прогресс и список, показать ошибку
                binding.progressBar.setVisibility(View.GONE);
                binding.rvWeatherForecast.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.VISIBLE);

                WeatherUiState.Error error = (WeatherUiState.Error) state;
                binding.tvError.setText(error.getMessage());
            }
        });
    }
}
