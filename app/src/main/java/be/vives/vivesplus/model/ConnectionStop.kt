package be.vives.vivesplus.model

data class ConnectionStop(
    val station: String,
    val time: String,
    val perron: String?,
    val delay: String
)