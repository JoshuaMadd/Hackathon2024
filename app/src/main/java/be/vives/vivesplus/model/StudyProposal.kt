package be.vives.vivesplus.model

import android.os.Parcelable
import be.vives.vivesplus.enum.Status
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudyProposal(
    val id: Int,
    val name: String,
    val status: Status
) : Parcelable