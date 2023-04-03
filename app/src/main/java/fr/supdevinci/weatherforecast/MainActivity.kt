package fr.supdevinci.weatherforecast

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.supdevinci.weatherforecast.Database.CityRoomDatabase
import fr.supdevinci.weatherforecast.Entity.WeatherCityEntity
import fr.supdevinci.weatherforecast.Model.DailyWeatherModel
import fr.supdevinci.weatherforecast.Model.Weather
import fr.supdevinci.weatherforecast.Repository.WeatherRepository
import fr.supdevinci.weatherforecast.View.WeatherAdapter
import fr.supdevinci.weatherforecast.ViewModel.CoordinatesViewModel
import fr.supdevinci.weatherforecast.ViewModel.WeatherViewModel
import fr.supdevinci.weatherforecast.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(){
    private lateinit var weatherViewModel: WeatherViewModel
    private var weather : Weather? = null
    private var dailyWeather : MutableList<DailyWeatherModel>? = null
    private val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    private lateinit var loadingLayout: FrameLayout

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

        val searchView = findViewById<SearchView>(R.id.search_view)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    val address = geocoder.getFromLocationName(query, 1)?.get(0)
                    val lat = address?.latitude
                    val long = address?.longitude

                    val scope = CoroutineScope(Dispatchers.Main)
                    scope.launch {
                        weather = weatherViewModel.getWeather(lat!!, long!!)
                        dailyWeather = weatherViewModel.getDailyWeather(lat!!, long!!)
                        println(dailyWeather)
                        setData(weather)
                        setDailyWeather(dailyWeather)
                        city.setText(query)
                    }
                }
                // Gérez l'action soumise ici
                // Par exemple, vous pouvez effectuer une requête à votre API
                // en utilisant la chaîne de requête `query` comme paramètre
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Gérez les changements de texte ici
                // Par exemple, vous pouvez effectuer des suggestions de recherche
                // en utilisant la chaîne de requête `newText` comme paramètre
                return true
            }
        })

            // Get weather from weatherViewModel
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            weather = weatherViewModel.getWeather(latitude, longitude)
            dailyWeather = weatherViewModel.getDailyWeather(latitude, longitude)
            setData(weather)
            setDailyWeather(dailyWeather)
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
    }

    fun setDailyWeather(dailyWeather: List<DailyWeatherModel>?){
        val adapter = dailyWeather?.let { WeatherAdapter(it) }
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        println(adapter)

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        hideLoadingScreen()
    }

    private fun hideLoadingScreen() {
        loadingLayout = findViewById(R.id.loadingLayout)
        loadingLayout.visibility = View.GONE
    }

}