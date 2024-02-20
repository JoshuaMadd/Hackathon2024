package be.vives.vivesplus.model.dashboardcard

import be.vives.vivesplus.enum.Severity

data class DashboardCardDetailMessage (
    val title: String,
    val description: String,
    val severity: Severity,
    val url: String?
) : DashboardCardDetail() {}