package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Station

interface TrainSearchDAOCallback {
    fun dataLoaded(stations: MutableList<Station>)
}