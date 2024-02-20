package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Restaurant

interface RestaurantDAOCallback {
    fun dataLoaded(restaurants: ArrayList<Restaurant>)
}