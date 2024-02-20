package be.vives.vivesplus.dao

import be.vives.vivesplus.model.FieldOfStudy

interface FieldOfStudyDAOCallback {
    fun dataLoaded(fieldOfStudyLst: ArrayList<FieldOfStudy>)
}