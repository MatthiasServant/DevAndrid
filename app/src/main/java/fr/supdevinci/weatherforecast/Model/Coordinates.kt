package fr.supdevinci.weatherforecast.Model

data class Coordinates(val longitude: Float, val latitude: Float){
    override fun toString(): String {
        return "Coordinates(longitude=$longitude, latitude=$latitude)"
    }
}
