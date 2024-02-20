package be.vives.vivesplus.model.dashboardcard

data class DashboardCardDetailCancelledCourse (
    val date: String,
    val startTime: String,
    val endTime: String,
    val longDescription: String,
    val roomName: String,
    val cancelledMessage: String
) : DashboardCardDetail() {}