package com.example.weatherappjavaactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider; // Для отримання ViewModel
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView; // Імпорт RecyclerView

import android.os.Bundle;
import android.view.View; // Імпорт View для керування видимістю
// Імпорти твоїх класів:
import com.example.weatherappjavaactivity.data.ForecastItem;
import com.example.weatherappjavaactivity.data.onecall.DailyForecast;
import com.example.weatherappjavaactivity.databinding.ActivityMainBinding; // Імпорт згенерованого Binding
import com.example.weatherappjavaactivity.ui.WeatherAdapter;
import com.example.weatherappjavaactivity.ui.WeatherUiState;
import com.example.weatherappjavaactivity.ui.WeatherViewModel;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    // Змінна для View Binding (для доступу до елементів макету activity_main.xml)
    private ActivityMainBinding binding;
    // Змінна для нашої ViewModel
    private WeatherViewModel viewModel;
    // Змінна для нашого RecyclerView Adapter
    private WeatherAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ініціалізація View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Встановлюємо корінний елемент з binding як контент для Activity
        setContentView(binding.getRoot());

        // Ініціалізація ViewModel
        // Використовуємо ViewModelProvider для отримання/створення ViewModel,
        // це забезпечує правильний життєвий цикл ViewModel (переживає повороти екрану)
        viewModel = new ViewModelProvider(this).get(WeatherViewModel.class);

        // Налаштування RecyclerView
        setupRecyclerView();

        // Налаштування спостереження за змінами стану в ViewModel
        observeViewModel();
    }

    // Метод для налаштування RecyclerView
    private void setupRecyclerView() {
        // Створюємо екземпляр нашого адаптера
        weatherAdapter = new WeatherAdapter();
        // Встановлюємо LayoutManager для RecyclerView (визначає, як елементи будуть розташовані - лінійно)
        binding.rvWeatherForecast.setLayoutManager(new LinearLayoutManager(this));
        // Встановлюємо адаптер для RecyclerView
        binding.rvWeatherForecast.setAdapter(weatherAdapter);
    }

    // Метод для спостереження за LiveData з ViewModel
    // --- ПОВНІСТЮ ПЕРЕПИСАНИЙ МЕТОД observeViewModel ---
    private void observeViewModel() {
        // Отримуємо LiveData об'єкт зі стану UI з ViewModel.
        // viewModel.getWeatherState() повертає LiveData<WeatherUiState>.
        // Метод observe() приймає LifecycleOwner (в даному випадку Activity - this)
        // та Observer (реалізований тут як лямбда-вираз).
        viewModel.getWeatherState().observe(this, state -> {
            // Цей блок коду виконується щоразу, коли дані в LiveData змінюються.
            // 'state' - це поточний об'єкт WeatherUiState.

            // Використовуємо instanceof для визначення конкретного типу стану.

            if (state instanceof WeatherUiState.Loading) {
                // Стан: Завантаження даних
                // Показуємо індикатор прогресу.
                binding.progressBar.setVisibility(View.VISIBLE);
                // Ховаємо текст помилки (якщо він був видимий).
                binding.tvError.setVisibility(View.GONE);
                // Ховаємо список (RecyclerView).
                binding.rvWeatherForecast.setVisibility(View.GONE);

            } else if (state instanceof WeatherUiState.Success) {
                // Стан: Дані успішно завантажені
                // Ховаємо індикатор прогресу.
                binding.progressBar.setVisibility(View.GONE);
                // Ховаємо текст помилки.
                binding.tvError.setVisibility(View.GONE);
                // Показуємо список (RecyclerView).
                binding.rvWeatherForecast.setVisibility(View.VISIBLE);

                // Оскільки ми впевнені, що 'state' має тип WeatherUiState.Success,
                // ми можемо безпечно привести (cast) його до цього типу.
                WeatherUiState.Success successState = (WeatherUiState.Success) state;

                // Отримуємо список денних прогнозів з об'єкта стану.
                // Метод getForecastList() повертає List<DailyForecast>.
                List<DailyForecast> forecastList = successState.getForecastList();

                // Передаємо отриманий список до нашого адаптера RecyclerView.
                // Адаптер оновить відображення на основі нових даних.
                weatherAdapter.submitList(forecastList);

            } else if (state instanceof WeatherUiState.Error) {
                // Стан: Сталася помилка під час завантаження даних
                // Ховаємо індикатор прогресу.
                binding.progressBar.setVisibility(View.GONE);
                // Ховаємо список (RecyclerView).
                binding.rvWeatherForecast.setVisibility(View.GONE);
                // Показуємо текстове поле для помилки.
                binding.tvError.setVisibility(View.VISIBLE);

                // Приводимо 'state' до типу WeatherUiState.Error, щоб отримати повідомлення.
                WeatherUiState.Error errorState = (WeatherUiState.Error) state;

                // Отримуємо текст повідомлення про помилку.
                String errorMessage = errorState.getMessage();

                // Встановлюємо отримане повідомлення в TextView.
                binding.tvError.setText(errorMessage);
            }
        });
    }
    }
