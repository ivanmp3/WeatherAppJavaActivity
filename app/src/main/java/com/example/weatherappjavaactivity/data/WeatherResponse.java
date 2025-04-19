package com.example.weatherappjavaactivity.data;
import com.google.gson.annotations.SerializedName;
import java.util.List;

// Головний клас відповіді API
public class WeatherResponse {
    // Поле "list" у JSON містить список прогнозів
    @SerializedName("list")
    private List<ForecastItem> list;

    // Getter для доступу до списку
    public List<ForecastItem> getList() {
        return list;
    }

    // Можна додати інші поля з відповіді, якщо потрібно (наприклад, city)
    // @SerializedName("city")
    // private CityInfo city;
    // public CityInfo getCity() { return city; }
}