package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Campus

interface CampusDAOCallback {
    fun campusDataLoaded(campusList: ArrayList<Campus>)
}