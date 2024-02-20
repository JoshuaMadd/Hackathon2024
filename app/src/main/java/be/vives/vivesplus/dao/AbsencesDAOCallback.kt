package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Absence

interface AbsencesDAOCallback {
    fun dataLoaded(absences: ArrayList<Absence>)
}