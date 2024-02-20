package be.vives.vivesplus.dao

import be.vives.vivesplus.model.EventDetail

interface EventDetailDAOCallback {
    fun eventDetailDataLoaded(eventDetail: EventDetail)
    fun setError(error: Int) {}
}