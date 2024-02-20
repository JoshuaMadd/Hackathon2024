package be.vives.vivesplus.dao

import be.vives.vivesplus.model.TransportOption

interface TransportSettingsDAOCallback {
    fun transportSettingsDataLoaded(transportList: ArrayList<TransportOption>)
}

