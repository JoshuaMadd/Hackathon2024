package be.vives.vivesplus.util

import be.vives.vivesplus.enum.DashboardType
import be.vives.vivesplus.enum.Severity
import be.vives.vivesplus.model.dashboardcard.*
import org.json.JSONObject
import java.lang.Exception

class DashboardMapper {

    fun mapJsonToDashboardType(jsonValue: String) : DashboardType? {
        return when (jsonValue) {
            "DAYDELIMITER" -> {
                DashboardType.DAYDELIMITER
            }
            "CURRENTCOURSE" -> {
                DashboardType.CURRENTCOURSE
            }
            "NEXTCOURSETODAY" -> {
                DashboardType.NEXTCOURSETODAY
            }
            "LATERCOURSETODAY" -> {
                DashboardType.LATERCOURSETODAY
            }
            "NEXTCOURSELATER" -> {
                DashboardType.NEXTCOURSELATER
            }
            "WEATHER" -> {
                DashboardType.WEATHER
            }
            "MESSAGE" -> {
                DashboardType.MESSAGE
            }
            "CANCELLEDCOURSE" -> {
                DashboardType.CANCELLEDCOURSE
            }
            "CATERING" -> {
                DashboardType.CATERING
            }
            "BUS" -> {
                DashboardType.BUS
            }
            "TRAIN" -> {
                DashboardType.TRAIN
            }
            "EVENT" -> {
                DashboardType.EVENT
            }
            "ALLDAYEVENT" -> {
                DashboardType.ALLDAYEVENT
            }
            "PARKING" -> {
                DashboardType.PARKING
            }
            else -> {
                null
            }
        }
    }

    fun mapTypeToDashboardCardDetail(cardType: DashboardType, detailJson: JSONObject) : DashboardCardDetail? {
        return when (cardType){
            DashboardType.DAYDELIMITER -> {
                try {
                    val date = detailJson.getString("date")
                    DashboardCardDetailDayDelimeter(date)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.CURRENTCOURSE -> {
                try {
                    val date = detailJson.getString("date")
                    val startTime = detailJson.getString("startTime")
                    val endTime = detailJson.getString("endTime")
                    val longDescription = detailJson.getString("longDescription")
                    val roomName = detailJson.getString("roomName")
                    DashboardCardDetailCurrentCourse(date, startTime, endTime, longDescription, roomName)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.NEXTCOURSETODAY -> {
                try {
                    val date = detailJson.getString("date")
                    val startTime = detailJson.getString("startTime")
                    val endTime = detailJson.getString("endTime")
                    val longDescription = detailJson.getString("longDescription")
                    val roomName = detailJson.getString("roomName")
                    DashboardCardDetailNextCourseToday(date, startTime, endTime, longDescription, roomName)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.LATERCOURSETODAY -> {
                try {
                    val date = detailJson.getString("date")
                    val startTime = detailJson.getString("startTime")
                    val endTime = detailJson.getString("endTime")
                    val longDescription = detailJson.getString("longDescription")
                    val roomName = detailJson.getString("roomName")
                    DashboardCardDetailLaterCourseToday(date, startTime, endTime, longDescription, roomName)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.NEXTCOURSELATER -> {
                try {
                    val date = detailJson.getString("date")
                    val startTime = detailJson.getString("startTime")
                    val endTime = detailJson.getString("endTime")
                    val longDescription = detailJson.getString("longDescription")
                    val roomName = detailJson.getString("roomName")
                    DashboardCardDetailNextCourseLaterData(date, startTime, endTime, longDescription, roomName)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.WEATHER -> {
                try {
                    val description = detailJson.getString("description")
                    val icon = detailJson.getString("icon")
                    val temp = detailJson.getInt("temp")
                    val campusName = detailJson.getString("campusName")
                    DashboardCardDetailWeather(description, icon, temp, campusName)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.MESSAGE -> {
                try {
                    val title = detailJson.getString("title")
                    val description = detailJson.getString("description")
                    val severityJson = detailJson.getString("severity")
                    val severity = mapJsonToSeverityType(severityJson)
                    val url : String? = if (detailJson.isNull("url")){
                        null
                    } else {
                        detailJson.getString("url")
                    }
                    DashboardCardDetailMessage(title, description, severity!!, url)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.CANCELLEDCOURSE -> {
                try {
                    val date = detailJson.getString("date")
                    val startTime = detailJson.getString("startTime")
                    val endTime = detailJson.getString("endTime")
                    val longDescription = detailJson.getString("longDescription")
                    val roomName = detailJson.getString("roomName")
                    val cancelledMessage = detailJson.getString("cancelledMessage")
                    DashboardCardDetailCancelledCourse(date, startTime, endTime, longDescription, roomName, cancelledMessage)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.CATERING -> {
                try {
                    val text = detailJson.getString("text")
                    DashboardCardDetailCatering(text)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.BUS -> {
                try {
                    val text = detailJson.getString("text")
                    DashboardCardDetailBus(text)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.TRAIN -> {
                try {
                    val text = detailJson.getString("text")
                    DashboardCardDetailTrain(text)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.EVENT -> {
                try {
                    val date = detailJson.getString("date")
                    val startTime = detailJson.getString("startTime")
                    val endTime = detailJson.getString("endTime")
                    val description = detailJson.getString("description")
                    val location = detailJson.getString("location")
                    DashboardCardDetailEvent(date, startTime, endTime, description, location)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.ALLDAYEVENT -> {
                try {
                    val date = detailJson.getString("date")
                    val description = detailJson.getString("description")
                    val location = detailJson.getString("location")
                    DashboardCardDetailAllDayEvent(date, description, location)
                } catch (ex: Exception) {
                    null
                }
            }
            DashboardType.PARKING -> {
                try {
                    val title = detailJson.getString("text")
                    DashboardCardDetailParking(title)
                } catch  (ex: Exception) {
                    null
                }
            }

        }
    }

    private fun mapJsonToSeverityType(jsonValue: String) : Severity? {
        return when (jsonValue) {
            "INFO" -> {
                Severity.INFO
            }
            "SUCCESS" -> {
                Severity.SUCCESS
            }
            "DANGER" -> {
                Severity.DANGER
            }
            "WARNING" -> {
                Severity.WARNING
            }
            "PRIMARY" -> {
                Severity.PRIMARY
            }
            "NORMAL" -> {
                Severity.NORMAL
            }
            else -> {
                Severity.NORMAL
            }
        }
    }

}