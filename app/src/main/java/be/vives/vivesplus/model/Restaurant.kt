package be.vives.vivesplus.model

data class Restaurant(
    val id: Int,
    val name: String,
    val description: String?,
    val link: String?,
    val openingsInfo: String?,
    val moreButtonText: String?
)