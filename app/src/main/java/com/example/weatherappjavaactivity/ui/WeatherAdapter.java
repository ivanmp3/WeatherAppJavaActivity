package com.example.weatherappjavaactivity.ui; // ЗАМІНИ на свій пакет

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView; // Переконайся, що імпортовано ImageView
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Імпорт Glide
import com.bumptech.glide.Glide;

// Імпортуємо СТАРІ моделі даних
import com.example.weatherappjavaactivity.data.ForecastItem;
import com.example.weatherappjavaactivity.data.WeatherInfo;
// Імпортуємо Binding для макету з CardView та ImageView
import com.example.weatherappjavaactivity.databinding.ListItemWeatherBinding;
// Імпортуємо R клас
import com.example.weatherappjavaactivity.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Адаптер працює з List<ForecastItem>
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<ForecastItem> forecastList = new ArrayList<>();

    // --- ViewHolder ---
    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        private final ListItemWeatherBinding binding; // Використовуємо Binding для макету з CardView

        public WeatherViewHolder(@NonNull ListItemWeatherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // Метод bind тепер приймає ForecastItem
        public void bind(ForecastItem item) {
            // Встановлюємо дату і час (з 3-годинного прогнозу)
            binding.tvDateTime.setText(formatDateTime(item.getDateTimeUnix()));

            // Встановлюємо температуру (з об'єкта MainInfo)
            if (item.getMain() != null) {
                binding.tvTemperatureValue.setText(
                        String.format(Locale.getDefault(), "%.0f °C", item.getMain().getTemperature())
                );
                // Встановлюємо тиск
                binding.tvPressureValue.setText(
                        String.format(Locale.getDefault(), "%.0f гПа", item.getMain().getPressure())
                );
            } else {
                binding.tvTemperatureValue.setText("N/A");
                binding.tvPressureValue.setText("N/A");
            }

            // Встановлюємо швидкість вітру (з об'єкта WindInfo)
            if (item.getWind() != null) {
                binding.tvWindValue.setText(
                        String.format(Locale.getDefault(), "%.0f м/с", item.getWind().getSpeed())
                );
            } else {
                binding.tvWindValue.setText("N/A");
            }


            // --- Отримання опису та іконки з WeatherInfo ---
            String description = "N/A";
            String iconCode = null;
            // Перевіряємо, чи є список погоди і чи він не пустий
            if (item.getWeather() != null && !item.getWeather().isEmpty()) {
                // Беремо перший елемент опису погоди
                WeatherInfo weatherInfo = item.getWeather().get(0);
                if (weatherInfo != null) { // Додаткова перевірка
                    if (weatherInfo.getDescription() != null){
                        String rawDescription = weatherInfo.getDescription();
                        // Робимо першу літеру великою
                        description = rawDescription.substring(0, 1).toUpperCase(Locale.getDefault()) + rawDescription.substring(1);
                    }
                    // Отримуємо код іконки
                    iconCode = weatherInfo.getIcon();
                }
            }
            binding.tvDescription.setText(description);
            // ----------------------------------------------


            // --- ЗАВАНТАЖЕННЯ ІКОНКИ З GLIDE ---
            if (iconCode != null) {
                // Формуємо повний URL для іконки OpenWeatherMap
                // @2x означає подвійний розмір для кращої якості
                String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                Glide.with(binding.ivWeatherIcon.getContext()) // Беремо контекст
                        .load(iconUrl) // Вказуємо URL для завантаження
                        // Опціонально: що показувати, поки картинка вантажиться
                        .placeholder(R.mipmap.ic_launcher)
                        // Опціонально: що показувати, якщо сталася помилка завантаження
                        .error(R.mipmap.ic_launcher_round)
                        // Куди завантажити зображення
                        .into(binding.ivWeatherIcon); // Наш ImageView в макеті
            } else {
                // Якщо коду іконки немає, встановлюємо стандартне зображення
                binding.ivWeatherIcon.setImageResource(R.mipmap.ic_launcher_round); // Наприклад
            }
            // ----------------------------------
        }

        // Форматуємо дату та час з Unix timestamp
        private String formatDateTime(long unixTimestamp) {
            try {
                Date date = new Date(unixTimestamp * 1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM HH:mm", new Locale("uk", "UA"));
                return sdf.format(date);
            } catch (Exception e) {
                return "Невірна дата";
            }
        }
    }
    // --- Кінець ViewHolder ---


    // Методи onCreateViewHolder, onBindViewHolder, getItemCount залишаються такими ж,
    // оскільки вони працюють з ViewHolder і розміром списку

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Використовуємо Binding для макету list_item_weather.xml (з CardView)
        ListItemWeatherBinding binding = ListItemWeatherBinding.inflate(inflater, parent, false);
        return new WeatherViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        // Передаємо об'єкт ForecastItem у метод bind
        holder.bind(forecastList.get(position));
    }

    @Override
    public int getItemCount() {
        return forecastList == null ? 0 : forecastList.size();
    }

    // Метод submitList тепер приймає List<ForecastItem>
    public void submitList(List<ForecastItem> list) {
        this.forecastList = list;
        notifyDataSetChanged();
    }
}