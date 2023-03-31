package fr.supdevinci.weatherforecast.Repository

import androidx.annotation.WorkerThread
import fr.supdevinci.weatherforecast.DAO.WeatherCityDao
import fr.supdevinci.weatherforecast.Entity.WeatherCityEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository(private val wordDao: WeatherCityDao) {

    val allCities: Flow<List<WeatherCityEntity>> = wordDao.getWeatherCities()

    @WorkerThread
    suspend fun insert(city: WeatherCityEntity) {
        wordDao.insert(city)
    }

}