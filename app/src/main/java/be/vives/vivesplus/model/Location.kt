package be.vives.vivesplus.model

data class Location(
    val description : String?,
    val buildingName : String?,
    val address : String?,
    val coordinates: Coordinates?
) {
}