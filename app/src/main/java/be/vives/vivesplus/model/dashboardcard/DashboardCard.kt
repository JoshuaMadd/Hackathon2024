package be.vives.vivesplus.model.dashboardcard

import be.vives.vivesplus.enum.DashboardType

data class DashboardCard(
    val type : DashboardType,
    val action : String,
    val detail : DashboardCardDetail
) {
}