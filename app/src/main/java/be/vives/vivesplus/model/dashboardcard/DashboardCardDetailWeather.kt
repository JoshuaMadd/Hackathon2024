package be.vives.vivesplus.model.dashboardcard

data class DashboardCardDetailWeather  (
    val description: String?,
    val icon: String?,
    val temp: Int?,
    var campusName: String?,
    ) : DashboardCardDetail() {}