package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Study

interface MemberStudiesDAOCallback {
    fun dataLoaded(studies: MutableList<Study>)
    fun dataLoaded(study: Study)
    fun postSucces()
    fun setError(error: Int) {}
}