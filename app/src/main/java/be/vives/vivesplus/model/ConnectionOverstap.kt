package be.vives.vivesplus.model

data class ConnectionOverstap(
    val station: String,
    val arrivalPerron: String,
    val departurePerron: String,
    val time: String
)