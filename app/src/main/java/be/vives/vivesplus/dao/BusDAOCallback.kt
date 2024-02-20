package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Bus

interface BusDAOCallback {
    fun dataLoaded(bussen: ArrayList<Bus>)
}