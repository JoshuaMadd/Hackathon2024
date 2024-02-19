package com.joshuamaddelein.hackathon2024.data.model

import java.time.LocalDate
import java.time.LocalTime

data class Les (
    var naam: String,
    var lokaal: String,
    var datum: LocalDate,
    var startTijd: LocalTime,
    var eindTijd: LocalTime
)
