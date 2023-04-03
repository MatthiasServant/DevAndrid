package fr.supdevinci.weatherforecast.ViewModel

import androidx.lifecycle.ViewModel
import fr.supdevinci.weatherforecast.Model.DailyWeatherModel
import fr.supdevinci.weatherforecast.Model.Weather
import fr.supdevinci.weatherforecast.Model.WeatherAPI
import fr.supdevinci.weatherforecast.Repository.RetrofitHelper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class WeatherViewModel : ViewModel() {

    var weather: Weather? = null;

    val weatherApi = RetrofitHelper.getInstance().create(WeatherAPI::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getWeather(latitude: Double, longitude: Double): Weather? {
        GlobalScope.launch {
            weather = weatherApi.getWeather(latitude, longitude)
        }.join()
        return weather
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getDailyWeather(latitude: Double, longitude: Double): MutableList<DailyWeatherModel> {
        var dailyWeather: MutableList<DailyWeatherModel> = mutableListOf();
        GlobalScope.launch {
            weather = weatherApi.getWeather(latitude, longitude)
        }.join()
        for(i in 0 until (weather?.daily?.time?.size ?: 0)){
            val dateTime = weather?.daily?.time?.get(i)
            val dateRegex = Regex("^(\\d{4})-(\\d{2})-(\\d{2})")
            val dateMatch = dateRegex.find(dateTime ?: "")
            val year = dateMatch?.groupValues?.get(1) ?: ""
            val month = dateMatch?.groupValues?.get(2) ?: ""
            val day = dateMatch?.groupValues?.get(3) ?: ""
            val localDate = LocalDate.of(year.toInt(), month.toInt(), day.toInt())
            val dayStr = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.FRANCE)
            val daily = DailyWeatherModel(dayStr, "$day/$month", weather?.daily?.weathercode?.get(i) ?: 0, weather?.daily?.temperature?.get(i) ?: 0f)
            dailyWeather.add(daily)
        }
        return dailyWeather
    }
}