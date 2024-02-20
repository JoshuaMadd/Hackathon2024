package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Study

interface StudiesDAOCallback {
    fun studyDataLoaded(studies: MutableList<Study>)
    fun studyDataLoaded(study: Study)
}