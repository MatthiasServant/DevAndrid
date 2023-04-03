package fr.supdevinci.weatherforecast.View

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.supdevinci.weatherforecast.Model.DailyWeatherModel
import fr.supdevinci.weatherforecast.R

class WeatherAdapter(
    private val weatherList: List<DailyWeatherModel>
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return WeatherViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = weatherList[position]
        holder.weatherDay.text = currentItem.time
        holder.weatherIcon.setImageResource(getWeatherIcon(currentItem.weathercode))
        holder.weatherDate.text = currentItem.date
        holder.weatherDegree.text = currentItem.temperature.toString() + "°C"
    }

    override fun getItemCount() = weatherList.size

    fun getWeatherIcon(weatherCode: Int?): Int {
        return when(weatherCode) {
            0 -> R.drawable.ensoleille
            1, 2, 3 -> R.drawable.nuage
            45, 48 -> R.drawable.nuageux
            51, 53, 55 -> R.drawable.nuageux
            56, 57 -> R.drawable.nuageux
            61, 63, 65 -> R.drawable.nuageux
            66, 67 -> R.drawable.nuageux
            71, 73, 75 -> R.drawable.nuageux
            77 -> R.drawable.nuageux
            80, 81, 82 -> R.drawable.nuageux
            85, 86 ->  R.drawable.nuageux
            95 -> R.drawable.nuageux
            96, 99 -> R.drawable.nuageux
            else -> R.drawable.nuageux
        }
    }

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherDay: TextView = itemView.findViewById(R.id.day_view)
        val weatherIcon: ImageView = itemView.findViewById(R.id.image_view)
        val weatherDate: TextView = itemView.findViewById(R.id.date_view)
        val weatherDegree: TextView = itemView.findViewById(R.id.temp_view)
    }
}
