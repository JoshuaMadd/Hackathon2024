package be.vives.vivesplus.dao

import be.vives.vivesplus.model.dashboardcard.DashboardCard


interface DashboardDAOCallback {
    fun dashboardDataLoaded(dashboardCards: ArrayList<DashboardCard>)
    fun setError(error: Int){ }
}