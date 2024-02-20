package be.vives.vivesplus.model.dashboardcard

data class DashboardCardDetailNextCourseToday (
    val date: String,
    val startTime: String,
    val endTime: String,
    val longDescription: String,
    val roomName: String
) : DashboardCardDetail() {}