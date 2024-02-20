package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Student

interface StudentDAOCallback {
    fun dataLoaded(student: Student)
    fun putSucces()
    fun dataLoadedFailed()
}