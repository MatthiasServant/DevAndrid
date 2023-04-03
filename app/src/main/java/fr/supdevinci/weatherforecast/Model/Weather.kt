package fr.supdevinci.weatherforecast.Model

import android.icu.util.Calendar
import com.google.gson.annotations.SerializedName
import fr.supdevinci.weatherforecast.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class Weather(
    val latitude: Float = 0F,
    val longitude: Float = 0F,
    val elevation: Float = 0F,
    val hourly: Hourly?,
    val daily: Daily?
    ){

    fun getDateToString() : String{
        val formatterInput = DateTimeFormatter.ISO_DATE_TIME
        val formatterOutput = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("fr"))

        val date = LocalDate.parse(this.hourly?.time?.get(0).toString(), formatterInput)
        val outputDate = date.format(formatterOutput)

        return outputDate;
    }

    fun getDegreeToString() : String{
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var temp = 0F
        for(i in 0 ..(this.hourly?.time?.size ?: 0)){
            if(i == currentHour){
                temp = this.hourly?.temperature?.get(i) ?: 0F
            }
        }
        return temp.toString() + "°C"
    }

    fun getActualWeather() : String{
        var weather = ""
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var weatherCode = 0
        for(i in 0 ..(this.hourly?.time?.size ?: 0)){
            if(i == currentHour){
                weatherCode = (this.hourly?.weathercode?.get(i) ?: 0)
            }
        }
        weather = getWeatherDescription(weatherCode).first
        return weather
    }

    fun getWeatherIcon() : Int{
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var weatherCode = 0
        for(i in 0 ..(this.hourly?.time?.size ?: 0)){
            if(i == currentHour){
                weatherCode = (this.hourly?.weathercode?.get(i) ?: 0)
            }
        }

        return getWeatherDescription(weatherCode).second
    }

    fun getWeatherDescription(weatherCode: Int?): Pair<String,Int> {
        return when(weatherCode) {
            0 -> "Ensoleillé" to R.drawable.ensoleille
            1, 2, 3 -> "Nuageux" to R.drawable.nuage
            45, 48 -> "Brouillard" to R.drawable.nuageux
            51, 53, 55 -> "Bruine" to R.drawable.nuageux
            56, 57 -> "Bruine verglaçante" to R.drawable.nuageux
            61, 63, 65 -> "Pluvieux" to R.drawable.nuageux
            66, 67 -> "Pluie verglaçante" to R.drawable.nuageux
            71, 73, 75 -> "Neige" to R.drawable.nuageux
            77 -> "Grêlons" to R.drawable.nuageux
            80, 81, 82 -> "Averses de pluie" to R.drawable.nuageux
            85, 86 -> "Averses de neige" to R.drawable.nuageux
            95 -> "Orage" to R.drawable.nuageux
            96, 99 -> "Orage avec grêle" to R.drawable.nuageux
            else -> "Temps inconnu" to R.drawable.nuageux
        }
    }

    fun getHumidity() : String{
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var humidity = 0
        for(i in 0 ..(this.hourly?.time?.size ?: 0)){
            if(i == currentHour){
                humidity = (this.hourly?.humidity?.get(i) ?: 0)
            }
        }
        return humidity.toString() + "%"
    }

    fun getWindSpeed() : String{
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var windSpeed = 0F
        for(i in 0 ..(this.hourly?.time?.size ?: 0)){
            if(i == currentHour){
                windSpeed = (this.hourly?.windSpeed?.get(i) ?: 0F)
            }
        }
        return windSpeed.toString() + "km/h"
    }

    fun getUVIndex() : String{
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var uvIndex = 0
        for(i in 0 ..(this.hourly?.time?.size ?: 0)){
            if(i == currentHour){
                uvIndex = (this.hourly?.UVIndex?.get(i) ?: 0)
            }
        }
        return uvIndex.toString()
    }

}

data class Hourly(
    val time: List<String>,
    @SerializedName("temperature_2m")
    val temperature: List<Float>,
    @SerializedName("relativehumidity_2m")
    val humidity: List<Int>?,
    val precipitation: List<Float>?,
    val rain: List<Float>?,
    @SerializedName("windspeed_80m")
    val windSpeed: List<Float>?,
    val weathercode: List<Int>?,
    @SerializedName("uv_index")
    val UVIndex: List<Int>?
)

data class Daily(
    val time: List<String>,
    val weathercode: List<Int>,
    @SerializedName("temperature_2m_max")
    val temperature: List<Float>
)

