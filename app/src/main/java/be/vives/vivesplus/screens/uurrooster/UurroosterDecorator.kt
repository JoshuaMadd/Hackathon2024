package be.vives.vivesplus.screens.uurrooster

import com.alamkanak.weekview.DateFormatter
import com.alamkanak.weekview.WeekView
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class UurroosterDecorator : DateFormatter {

    fun setupWeekview(weekView: WeekView) {
        weekView.scrollToTime(8, 0)
        weekView.setDateFormatter(this)
    }

    fun interpretDate(date: Calendar): String {
        val format = SimpleDateFormat("EEE d/M", Locale("NL"))
        return format.format(date.time).toString()
    }

    fun interpretTime(hour: Int): String  {
        return if(hour < 10)
            "0$hour:00"
        else
            "$hour:00"
    }

    fun interpretTimeHourAndMinutes(time: LocalDateTime) : String{
        return if(time.hour < 10)
            "0${time.hour}:${interpretMinutes(time.minute)}"
        else
            "${time.hour}:${interpretMinutes(time.minute)}"
    }
    private fun interpretMinutes(minutes: Int) : String{
        return if(minutes < 10)
            "0$minutes"
        else
            "$minutes"
    }
    override fun invoke(p1: Calendar): String {
        return interpretDate(p1)
    }

}