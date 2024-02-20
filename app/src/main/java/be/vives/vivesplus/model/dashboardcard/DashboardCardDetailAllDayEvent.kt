package be.vives.vivesplus.model.dashboardcard

data class DashboardCardDetailAllDayEvent(
    val date: String,
    val description: String,
    val location: String
) : DashboardCardDetail() {}