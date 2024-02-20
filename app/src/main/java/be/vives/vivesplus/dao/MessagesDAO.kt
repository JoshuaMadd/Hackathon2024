package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Message
import be.vives.vivesplus.util.DateTimeHelper
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDateTime

class MessagesDAO(val context: Context, val callback: MessagesDAOCallback) : WebServiceCallback {

    val fileName = "notices.json"
    val url = "${context.getString(R.string.vivesplus_api_link)}/notices"

    fun getMessages() {
        WebService(context, fileName, this).getJsonArrayFromUrl(url)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list: ArrayList<Message> = ArrayList()
        for (i in 0 until jsonArray.length()) {
            try {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getString("id")
                val category = obj.getJSONObject("category")
                val categoryicon = category.getString("icon")
                val categoryDescription = category.getString("description")
                val categoryCode = category.getString("code")
                val sender = obj.getString("sender")
                val subject = obj.getString("subject")
                val description = obj.getString("description")
                val link = obj.getString("link")

                var startEvent: LocalDateTime?
                var endEvent: LocalDateTime?
                var eventLocation: String?

                if(obj.getString("scheduleInfo").contains("null"))
                {
                 startEvent = null
                 endEvent = null
                 eventLocation = null
                }
                else{
                    val scheduleInfo = obj.getJSONObject("scheduleInfo")
                    startEvent = DateTimeHelper().formatStringToDateTime(scheduleInfo.getString("startEventDate"))
                    endEvent = DateTimeHelper().formatStringToDateTime(scheduleInfo.getString("endEventDate"))
                    eventLocation = if(scheduleInfo.getString("eventLocation").isEmpty()) {
                        null
                    } else{
                        scheduleInfo.getString("eventLocation")
                    }
                }


                val viewDate =  DateTimeHelper().formatStringToDateTime(obj.getString("viewDate"))

                val startDate = DateTimeHelper().formatStringToDateTime(obj.getString("startDate"))

                val priority = obj.getString("priority")
                var buttonTextMore = obj.getString("moreButtonText")
                if(buttonTextMore.contains("null"))
                {
                    buttonTextMore = "null"
                }

                list.add(Message(id,sender,subject,description,link,startDate,priority, buttonTextMore, categoryicon,categoryDescription,categoryCode,startEvent, endEvent, eventLocation, viewDate))

            } catch (ex: Exception) {
                println(ex.toString())
            }
        }
        callback.dataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {}
}