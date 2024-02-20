package be.vives.vivesplus.screens.uurrooster

import java.time.LocalDateTime

data class Event(
    val id: Long,
    var title: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String,
    var color: Int,
    val isAllDay: Boolean,
    val isCanceled: Boolean){}