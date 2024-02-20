package be.vives.vivesplus.dao

import be.vives.vivesplus.model.AdminAbsence

interface AdminAbsencesDAOCallback {
    fun dataLoaded(absences: ArrayList<AdminAbsence>)
    fun postOrPutSuccesful()
}