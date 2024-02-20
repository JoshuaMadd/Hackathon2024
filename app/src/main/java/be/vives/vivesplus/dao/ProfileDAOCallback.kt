package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Profile

interface ProfileDAOCallback {
    fun dataLoaded(profile: Profile)
    fun setError(error: Int) {}
}