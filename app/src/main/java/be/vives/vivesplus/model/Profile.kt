package be.vives.vivesplus.model

import be.vives.vivesplus.enum.ProfileRole

data class Profile (
    val role: ProfileRole?,
    val kulNumber: String?
)