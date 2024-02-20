package be.vives.vivesplus.model

data class Connection(
    val departure: ConnectionStop,
    val arrival: ConnectionStop,
    val stops: MutableList<ConnectionStop>,
    val overstaps: MutableList<ConnectionOverstap>
)