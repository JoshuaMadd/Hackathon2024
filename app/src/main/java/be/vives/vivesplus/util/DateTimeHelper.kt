package be.vives.vivesplus.util

import android.annotation.SuppressLint
import be.vives.vivesplus.screens.uurrooster.UurroosterDecorator
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAdjusters
import java.util.*

class DateTimeHelper {

    fun formatStringToDateTime(str: String): LocalDateTime {
        return LocalDateTime.parse(str)
    }

    fun formatDateTimeToString(d: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return d.format(formatter)
    }

    fun formatDateTimeToDateString(d: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return d.format(formatter)
    }

    fun getUurroosterRangeAsString(): String {
        val now = LocalDate.now()
        val start = now.minusWeeks(2)
        val end = now.plusMonths(4)

        return "?from=${start}&to=$end"
    }

    fun getUurroosterRangeAsString(fromDate: LocalDate): String {

        val start = fromDate.minusWeeks(6)
        val end = fromDate.plusMonths(2)

        return "?from=${start}&to=$end"
    }

    fun getUurroosterRangeAsStringOneWeek(): String {
        val now = LocalDate.now()
        val end = now.plusWeeks(2)

        return "?from=$now&to=$end"
    }

    fun getNMBSDate(): String {
        val now = LocalDate.now()
        val day = if (now.dayOfMonth < 10)
            "0${now.dayOfMonth}"
        else
            "${now.dayOfMonth}"

        val month = if (now.monthValue < 10)
            "0${now.monthValue}"
        else
            "${now.monthValue}"

        val year = now.year.toString().substring(now.year.toString().length - 2, now.year.toString().length)

        return "$day$month$year"
    }

    fun getNMBSTime(): String {
        val now = LocalTime.now()
        val hour = if (now.hour < 10)
            "0${now.hour}"
        else
            "${now.hour}"

        val minute = if (now.minute < 10)
            "0${now.minute}"
        else
            "${now.minute}"

        return "$hour$minute"
    }

    @SuppressLint("SimpleDateFormat")
    fun epochToDateTimeString(epoch: String): String {
        return try {
            val e = epoch.toLong() * 1000
            val date = SimpleDateFormat("HH:mm")
                .format(Date(e))

            date
        } catch (e: Exception) {
            "N/A"
        }
    }

    fun formatLocalDatetimeStringToTimeString(ldtStr: String) : String? {
        val list = ldtStr.split("T")
        val time = list[list.size - 1]
        if(time == "null")
            return null

        return time.substring(0, time.length - 3)
    }

    fun formatMealsStartDate(): String {
        val previousMonday: LocalDate = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY))

        val day = if (previousMonday.dayOfMonth < 10)
            "0${previousMonday.dayOfMonth}"
        else
            "${previousMonday.dayOfMonth}"

        val month = if (previousMonday.monthValue < 10)
            "0${previousMonday.monthValue}"
        else
            "${previousMonday.monthValue}"

        val year = previousMonday.year.toString()

        return "$day$month$year"
    }

    fun formatCalendarToDateTimeString(c: Calendar): String {
        val day =
            if(c[Calendar.DAY_OF_MONTH] < 10) "0${c[Calendar.DAY_OF_MONTH]}"
            else c[Calendar.DAY_OF_MONTH]

        val calMonth = c[Calendar.MONTH] + 1
        val month=
            if(calMonth < 10) "0${calMonth}"
            else calMonth


        val hour = if(c[Calendar.HOUR_OF_DAY] < 10) "0${c[Calendar.HOUR_OF_DAY]}"
        else c[Calendar.HOUR_OF_DAY]

        val minute = if(c[Calendar.MINUTE] < 10) "0${c[Calendar.MINUTE]}"
        else c[Calendar.MINUTE]

        return "$day/$month/${c[Calendar.YEAR]} $hour:$minute"
    }

    fun calendarToLocalDate(date:Calendar): LocalDate {
        val strDate = formatCalendarToDateTimeString(date)
        return formatStringToLocalDateTimeWeekView(strDate)
    }

    fun formatDateToFullString(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
        return date.format(formatter)
    }

    fun formatStringToLongNameDateString(date: String) : String {
        val localDateTime = formatStringToLocalDateTime(date)
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return localDateTime.format(formatter)
    }

    fun formatStringToHourMinutesString(date: String) : String{
        val localDateTime = formatStringToLocalDateTime(date)
        return UurroosterDecorator().interpretTimeHourAndMinutes(localDateTime)
    }

    fun formatTimeToString(time: LocalTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return time.format(formatter)
    }

    fun formatDateAndTimeToString(date: LocalDate, time: LocalTime): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val dateTime = LocalDateTime.of(date, time)
        return dateTime.format(formatter)
    }

    fun formatDateTimeToFullString(d: LocalDateTime): String {
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
        return d.format(formatter)
    }

    fun formatStringToLocalDateTime(str: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return LocalDateTime.parse(str, formatter)
    }

    private fun formatStringToLocalDateTimeWeekView(str: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        return LocalDate.parse(str, formatter)
    }



}