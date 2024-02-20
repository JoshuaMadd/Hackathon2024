package be.vives.vivesplus.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Message(
    val id: String,
    val sender: String,
    val subject: String,
    val description: String,
    val link: String?,
    val startDate: LocalDateTime?,
    val priority: String,
    val textButtonMore: String?,
    val categorieIcon: String,
    val categorieBeschrijving: String,
    val categoryCode: String,
    val startEventDate: LocalDateTime?,
    val endEventDate: LocalDateTime?,
    val location: String?,
    val viewDate: LocalDateTime) : Parcelable