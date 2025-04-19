package com.example.weatherappjavaactivity.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherappjavaactivity.data.ForecastItem;
import com.example.weatherappjavaactivity.databinding.ListItemWeatherBinding; // Важливо: імпортуємо згенерований Binding клас

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    // Список для зберігання даних прогнозу
    private List<ForecastItem> forecastList = new ArrayList<>();

    // --- ViewHolder ---
    // Внутрішній клас, що представляє один елемент списку (один рядок)
    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        // Використовуємо ViewBinding для доступу до View елементів макету
        private final ListItemWeatherBinding binding;

        // Конструктор ViewHolder
        public WeatherViewHolder(@NonNull ListItemWeatherBinding binding) {
            super(binding.getRoot()); // Передаємо корінний View до батьківського конструктора
            this.binding = binding;   // Зберігаємо binding для подальшого використання
        }

        // Метод для заповнення View елементів даними з об'єкта ForecastItem
        public void bind(ForecastItem item) {
            // Встановлюємо текст для кожного TextView, використовуючи дані з item
            binding.tvDateTime.setText(formatDateTime(item.getDateTimeUnix()));

            // Форматуємо та встановлюємо температуру (округлюємо до цілого)
            binding.tvTemperatureValue.setText(
                    String.format(Locale.getDefault(), "%.0f °C", item.getMain().getTemperature())
            );
            // Форматуємо та встановлюємо швидкість вітру (округлюємо до цілого)
            binding.tvWindValue.setText(
                    String.format(Locale.getDefault(), "%.0f м/с", item.getWind().getSpeed())
            );
            // Форматуємо та встановлюємо тиск (округлюємо до цілого)
            binding.tvPressureValue.setText(
                    String.format(Locale.getDefault(), "%.0f гПа", item.getMain().getPressure())
            );

            // Отримуємо та встановлюємо опис погоди
            String description = "N/A"; // Значення за замовчуванням
            // Перевіряємо наявність опису
            if (item.getWeather() != null && !item.getWeather().isEmpty() && item.getWeather().get(0).getDescription() != null) {
                String rawDescription = item.getWeather().get(0).getDescription();
                // Робимо першу літеру великою (для краси)
                description = rawDescription.substring(0, 1).toUpperCase(Locale.getDefault()) + rawDescription.substring(1);
            }
            binding.tvDescription.setText(description);
        }

        // Допоміжний метод для форматування Unix timestamp у читабельний рядок дати/часу
        private String formatDateTime(long unixTimestamp) {
            try {
                // Unix timestamp в секундах, Date працює з мілісекундами
                Date date = new Date(unixTimestamp * 1000L);
                // Створюємо форматтер для української локалі (наприклад, "Сб, 19 Кві 18:00")
                SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM HH:mm", new Locale("uk", "UA"));
                return sdf.format(date);
            } catch (Exception e) {
                // Обробка можливої помилки форматування
                return "Невірна дата";
            }
        }
    }
    // --- Кінець ViewHolder ---


    // --- Методи RecyclerView.Adapter ---

    // Створює новий екземпляр ViewHolder (викликається LayoutManager'ом)
    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Створюємо LayoutInflater з контексту батьківського елемента
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // "Надуваємо" (створюємо) View Binding для нашого макету елемента списку
        ListItemWeatherBinding binding = ListItemWeatherBinding.inflate(inflater, parent, false);
        // Створюємо та повертаємо новий екземпляр ViewHolder
        return new WeatherViewHolder(binding);
    }

    // Заповнює ViewHolder даними для конкретної позиції (викликається LayoutManager'ом)
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        // Отримуємо об'єкт ForecastItem для поточної позиції
        ForecastItem currentItem = forecastList.get(position);
        // Викликаємо метод bind у ViewHolder для заповнення View
        holder.bind(currentItem);
    }

    // Повертає загальну кількість елементів у списку (викликається LayoutManager'ом)
    @Override
    public int getItemCount() {
        // Повертаємо розмір списку (або 0, якщо список null)
        return forecastList == null ? 0 : forecastList.size();
    }

    // Метод для оновлення даних в адаптері
    public void submitList(List<ForecastItem> list) {
        this.forecastList = list; // Оновлюємо внутрішній список
        notifyDataSetChanged(); // Повідомляємо RecyclerView, що весь набір даних змінився
        // Для великих списків краще використовувати DiffUtil,
        // але для простоти тут використовуємо notifyDataSetChanged().
    }
    // --- Кінець методів RecyclerView.Adapter ---
}

