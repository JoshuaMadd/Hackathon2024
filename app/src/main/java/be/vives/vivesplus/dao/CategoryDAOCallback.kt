package be.vives.vivesplus.dao

import be.vives.vivesplus.model.Category

interface CategoryDAOCallback {
    fun dataLoaded(categories: MutableList<Category>)
}