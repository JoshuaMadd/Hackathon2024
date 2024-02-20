package be.vives.vivesplus.model

import java.time.LocalDateTime

data class AdminAbsence(
    val id: Int,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val remark: String?
)