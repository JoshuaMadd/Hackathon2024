package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Connection

interface TrainDAOCallback {
    fun dataLoaded(connectios: ArrayList<Connection>)
}