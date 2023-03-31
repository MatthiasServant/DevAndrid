package fr.supdevinci.weatherforecast

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import fr.supdevinci.weatherforecast.Database.CityRoomDatabase
import fr.supdevinci.weatherforecast.Entity.WeatherCityEntity
import fr.supdevinci.weatherforecast.Model.Weather
import fr.supdevinci.weatherforecast.Repository.WeatherRepository
import fr.supdevinci.weatherforecast.ViewModel.CoordinatesViewModel
import fr.supdevinci.weatherforecast.ViewModel.WeatherViewModel
import fr.supdevinci.weatherforecast.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(){
    private lateinit var weatherViewModel: WeatherViewModel
    private var weather : Weather? = null

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val latitude: Double = CoordinatesViewModel(this).getGPSLocation(this)[0]
        val longitude: Double = CoordinatesViewModel(this).getGPSLocation(this)[1]
        val geocoder = Geocoder(this)
        val list = geocoder.getFromLocation(latitude, longitude, 1)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        val city: TextView = findViewById(R.id.city)
        city.setText(list?.get(0)?.locality.toString())

        weatherViewModel = WeatherViewModel()

        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        // Database Room
        val applicationScope = CoroutineScope(SupervisorJob())
        val database = CityRoomDatabase.getDatabase(this, applicationScope)
        val repository = WeatherRepository(database.cityDao())
        applicationScope.launch(Dispatchers.IO) {
            repository.insert(WeatherCityEntity("Hello"))
            repository.insert(WeatherCityEntity("World!"))
        }

        // Get weather from weatherViewModel
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            weather = weatherViewModel.getWeather(latitude, longitude)
            setData(weather)
        }
    }

    fun setData(weather: Weather?){
        //set day in view
        val date : TextView = findViewById(R.id.date)
        date.setText(weather?.getDateToString())
        val temp : TextView = findViewById(R.id.degree)
        temp.setText(weather?.getDegreeToString())
        val weatherCode : TextView = findViewById(R.id.weatherCode)
        weatherCode.setText(weather?.getActualWeather())
        val weatherIcon : ImageView = findViewById(R.id.weatherIcon)
        weatherIcon.setBackgroundResource(weather?.getWeatherIcon()!!)

        /*val humidity : TextView = findViewById(R.id.humidity)
        humidity.setText(weather?.getHumidity())
        val wind : TextView = findViewById(R.id.windSpeed)
        wind.setText(weather?.getWindSpeed())
        val UVIndex : TextView = findViewById(R.id.UVIndex)
        UVIndex.setText(weather?.getUVIndex())*/
    }
}