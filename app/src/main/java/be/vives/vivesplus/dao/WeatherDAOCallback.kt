package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Weather

interface WeatherDAOCallback {
    fun dataLoaded(weather: Weather)

}