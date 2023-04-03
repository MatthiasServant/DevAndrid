package fr.supdevinci.weatherforecast.Model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DailyWeatherModel(
    val time: String,
    val date: String,
    val weathercode: Int,
    val temperature: Float) {
    
    override fun toString(): String {
        return "DailyWeatherModel(time='$time', date='$date', weathercode=$weathercode, temperature=$temperature)"
    }
}