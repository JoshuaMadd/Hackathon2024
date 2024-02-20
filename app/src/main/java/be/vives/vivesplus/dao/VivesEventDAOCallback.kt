package be.vives.vivesplus.dao

import be.vives.vivesplus.model.VivesEvent

interface VivesEventDAOCallback {
    fun vivesEventDataLoaded(eventList: ArrayList<VivesEvent>)
    fun setError(error: Int) {}
}