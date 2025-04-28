package com.example.weatherappjavaactivity.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Імпорт Glide
import com.bumptech.glide.Glide;

// Імпортуємо НОВУ модель даних
import com.example.weatherappjavaactivity.data.ForecastItem;
import com.example.weatherappjavaactivity.data.onecall.DailyForecast;
import com.example.weatherappjavaactivity.data.onecall.WeatherDescription;
// Імпортуємо Binding для НОВОГО макету
import com.example.weatherappjavaactivity.databinding.ListItemWeatherBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Змінюємо тип списку на DailyForecast
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<DailyForecast> forecastList = new ArrayList<>();

    // --- ViewHolder ---
    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        private final ListItemWeatherBinding binding; // Використовуємо той самий Binding

        public WeatherViewHolder(@NonNull ListItemWeatherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Метод bind тепер приймає DailyForecast
        public void bind(DailyForecast item) {
            // Встановлюємо дату (тільки день тижня і число/місяць буде доречніше)
            binding.tvDateTime.setText(formatDateOnly(item.getDt()));

            // Встановлюємо температуру (Макс/Мін)
            binding.tvTemperatureValue.setText(
                    String.format(Locale.getDefault(), "%.0f°C / %.0f°C",
                            item.getTemp().getMax(), item.getTemp().getMin())
            );
            // Встановлюємо швидкість вітру
            binding.tvWindValue.setText(
                    String.format(Locale.getDefault(), "%.0f м/с", item.getWindSpeed())
            );
            // Встановлюємо тиск
            binding.tvPressureValue.setText(
                    String.format(Locale.getDefault(), "%d гПа", item.getPressure())
            );

            // Отримуємо опис та іконку
            String description = "N/A";
            String iconCode = null;
            if (item.getWeather() != null && !item.getWeather().isEmpty()) {
                WeatherDescription weatherDesc = item.getWeather().get(0);
                if (weatherDesc.getDescription() != null){
                    String rawDescription = weatherDesc.getDescription();
                    description = rawDescription.substring(0, 1).toUpperCase(Locale.getDefault()) + rawDescription.substring(1);
                }
                iconCode = weatherDesc.getIcon(); // Отримуємо код іконки (напр., "01d")
            }
            binding.tvDescription.setText(description);

            // --- ЗАВАНТАЖЕННЯ ІКОНКИ З GLIDE ---
            if (iconCode != null) {
                String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png"; // Формуємо URL іконки
                Glide.with(binding.ivWeatherIcon.getContext()) // Контекст беремо з будь-якого View всередині ViewHolder
                        .load(iconUrl) // Завантажуємо URL
                        .placeholder(R.mipmap.ic_launcher) // Опціонально: Placeholder поки вантажиться
                        .error(R.mipmap.ic_launcher_round) // Опціонально: Картинка якщо помилка завантаження
                        .into(binding.ivWeatherIcon); // В який ImageView завантажити
            } else {
                // Якщо коду іконки немає, можна показати стандартну картинку або сховати ImageView
                binding.ivWeatherIcon.setImageResource(R.mipmap.ic_launcher_round); // Наприклад
            }
            // ----------------------------------
        }

        // Форматуємо тільки дату (без часу)
        private String formatDateOnly(long unixTimestamp) {
            try {
                Date date = new Date(unixTimestamp * 1000L);
                // Формат: Наприклад, "Сб, 19 Квітня"
                SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMMM", new Locale("uk", "UA"));
                return sdf.format(date);
            } catch (Exception e) {
                return "Невірна дата";
            }
        }
    }
    // --- Кінець ViewHolder ---

    // Методи onCreateViewHolder, onBindViewHolder, getItemCount залишаються структурно такими ж

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemWeatherBinding binding = ListItemWeatherBinding.inflate(inflater, parent, false);
        return new WeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.bind(forecastList.get(position)); // Передаємо DailyForecast
    }

    @Override
    public int getItemCount() {
        return forecastList == null ? 0 : forecastList.size();
    }

    // Метод submitList тепер приймає List<DailyForecast>
    public void submitList(List<ForecastItem> list) {
        this.forecastList = list;
        notifyDataSetChanged();
    }
}