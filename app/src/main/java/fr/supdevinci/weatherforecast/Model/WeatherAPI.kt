package fr.supdevinci.weatherforecast.Model

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("/v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourly: String = "temperature_2m,relativehumidity_2m,precipitation,rain,weathercode,windspeed_80m,uv_index",
        @Query("daily") daily: String = "weathercode,temperature_2m_max",
    ): Weather
}