package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Profile

// Deprecated mag weg als profiel af is
interface UsersDAOCallback {
    fun dataLoaded(profile: Profile)
    fun putSucces()
}