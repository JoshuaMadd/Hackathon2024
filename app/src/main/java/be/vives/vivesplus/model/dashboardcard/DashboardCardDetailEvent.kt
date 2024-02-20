package be.vives.vivesplus.model.dashboardcard

data class DashboardCardDetailEvent (
    val date: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val location: String
) : DashboardCardDetail() {}