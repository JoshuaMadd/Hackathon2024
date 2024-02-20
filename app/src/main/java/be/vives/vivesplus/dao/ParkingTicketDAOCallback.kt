package be.vives.vivesplus.dao

import be.vives.vivesplus.model.ParkeerTicket

interface ParkingTicketDAOCallback {
    fun dataLoaded(ticket: ParkeerTicket)
    fun setError(error: Int){ }

}