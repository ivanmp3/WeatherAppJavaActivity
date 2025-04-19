package com.example.weatherappjavaactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider; // Для отримання ViewModel
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView; // Імпорт RecyclerView

import android.os.Bundle;
import android.view.View; // Імпорт View для керування видимістю
// Імпорти твоїх класів:
import com.example.weatherappjavaactivity.databinding.ActivityMainBinding; // Імпорт згенерованого Binding
import com.example.weatherappjavaactivity.ui.WeatherAdapter;
import com.example.weatherappjavaactivity.ui.WeatherUiState;
import com.example.weatherappjavaactivity.ui.WeatherViewModel;


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
    private void observeViewModel() {
        // Отримуємо LiveData зі станом UI з ViewModel і підписуємося на її зміни
        // `this` - власник життєвого циклу (MainActivity), спостереження буде активним, поки Activity жива
        // `state -> { ... }` - лямбда-вираз (Observer), який буде викликатися при кожній зміні стану
        viewModel.getWeatherState().observe(this, state -> {
            // Перевіряємо тип поточного стану UI
            if (state instanceof WeatherUiState.Loading) {
                // Стан завантаження: показуємо ProgressBar, ховаємо список і помилку
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.tvError.setVisibility(View.GONE);
                binding.rvWeatherForecast.setVisibility(View.GONE);

            } else if (state instanceof WeatherUiState.Success) {
                // Стан успіху: ховаємо ProgressBar і помилку, показуємо список
                binding.progressBar.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.GONE);
                binding.rvWeatherForecast.setVisibility(View.VISIBLE);

                // Отримуємо список прогнозу зі стану Success
                // Потрібно явно привести тип state до WeatherUiState.Success
                WeatherUiState.Success successState = (WeatherUiState.Success) state;
                // Передаємо отриманий список в адаптер для відображення
                weatherAdapter.submitList(successState.getForecastList());

            } else if (state instanceof WeatherUiState.Error) {
                // Стан помилки: ховаємо ProgressBar і список, показуємо повідомлення про помилку
                binding.progressBar.setVisibility(View.GONE);
                binding.rvWeatherForecast.setVisibility(View.GONE);
                binding.tvError.setVisibility(View.VISIBLE);

                // Отримуємо повідомлення про помилку зі стану Error
                WeatherUiState.Error errorState = (WeatherUiState.Error) state;
                // Встановлюємо текст помилки у відповідний TextView
                binding.tvError.setText(errorState.getMessage());
            }
        });
    }
}