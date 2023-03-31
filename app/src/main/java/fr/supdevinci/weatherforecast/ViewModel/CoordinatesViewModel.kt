package fr.supdevinci.weatherforecast.ViewModel

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import fr.supdevinci.weatherforecast.MainActivity
import fr.supdevinci.weatherforecast.Model.Coordinates

class CoordinatesViewModel(mainActivity: MainActivity) : ViewModel(), LocationListener {
    var gpsText = "Vos Coordonnées GPS sont : "
    var coordinates: Coordinates? = null
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    fun getFormattedCoordinates(coords: Coordinates): String {
        return "$gpsText ${coords.latitude} / ${coords.longitude}"
    }

    fun getGPSLocation(context: Context): List<Double> {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationPermissionCode = 1 // Define your permission code here
        var latitude = 0.0
        var longitude = 0.0

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    // update latitude and longitude values when location changes
                    latitude = location.latitude
                    longitude = location.longitude
                    locationManager.removeUpdates(this)
                }
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            })
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                latitude = lastKnownLocation.latitude
                longitude = lastKnownLocation.longitude
            }
        }
        return listOf(latitude, longitude)
    }

    override fun onLocationChanged(p0: Location) {
        coordinates = Coordinates(p0.latitude.toFloat(), p0.longitude.toFloat())
    }

}
