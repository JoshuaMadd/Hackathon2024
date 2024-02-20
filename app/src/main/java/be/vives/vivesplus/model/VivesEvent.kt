package be.vives.vivesplus.model

import android.content.Context
import be.vives.vivesplus.R

class VivesEvent (
    val id :String,
    val startDateTimeString :String,
    val endDateTimeString :String,
    val title :String,
    val location :String,
    val allDay :Boolean,
    val type :String) {

    fun getColor(context: Context) : Int {
        when (type) {
            "academic" -> return context.getColor(R.color.colorUurroosterAcademic)
            "course" -> return context.getColor(R.color.colorUurroosterCourse)
            "cancelled" -> return context.getColor(R.color.colorUurroosterAbsence)
            else -> {
                return context.getColor(R.color.colorUurroosterCourse)
            }
        }

    }

}