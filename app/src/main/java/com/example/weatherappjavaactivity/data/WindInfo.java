package com.example.weatherappjavaactivity.data;
import com.google.gson.annotations.SerializedName;

// Клас для інформації про вітер
public class WindInfo {
    // "speed" - швидкість вітру (в м/с, якщо units=metric)
    @SerializedName("speed")
    private double speed;

    // Можна додати "deg" - напрямок вітру в градусах

    // Getter
    public double getSpeed() { return speed; }
}
