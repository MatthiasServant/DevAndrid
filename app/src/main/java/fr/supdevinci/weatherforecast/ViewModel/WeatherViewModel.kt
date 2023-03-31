package fr.supdevinci.weatherforecast.ViewModel

import androidx.lifecycle.ViewModel
import fr.supdevinci.weatherforecast.Model.Weather
import fr.supdevinci.weatherforecast.Model.WeatherAPI
import fr.supdevinci.weatherforecast.Repository.RetrofitHelper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
}