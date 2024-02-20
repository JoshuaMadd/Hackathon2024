package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.VivesEvent
import be.vives.vivesplus.util.DateTimeHelper
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import com.android.volley.VolleyError
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDate
import kotlin.collections.ArrayList

class VivesEventDAO(val context: Context, private val callback: VivesEventDAOCallback): WebServiceCallback {

    private val url = "${context.getString(R.string.vivesplus_api_link)}/events"
    private val fileName = "vivesevents.json"
    private val webservice = WebService(context, fileName, this)

    fun get(fromDateTime:LocalDate) {
        try{
            webservice.getJsonArrayFromUrl("$url${DateTimeHelper().getUurroosterRangeAsString(fromDateTime)}")
        } catch(ve: VolleyError){
            setError(ve.networkResponse.statusCode)
        } catch (ex: Exception) {
            println(ex);
        }
    }

    override fun setError(error: Int) {
        callback.setError(error)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list = ArrayList<VivesEvent>()
        for (i in 0 until jsonArray.length()) {
            try {
                val json = jsonArray.getJSONObject(i)
                val id = json.getString("id")
                val startDateTimeString = json.getString("startDateTime")
                val endDateTimeString = json.getString("endDateTime")
                val title = json.getString("title")
                var location = json.getString("location")
                if (location.equals("null")) {
                    location = ""
                }
                val type = json.getString("type")
                val allDay = json.getBoolean("allDay")
                list.add(VivesEvent(id,startDateTimeString, endDateTimeString, title, location, allDay, type))
            } catch (v : VolleyError) {
                callback.setError(v.networkResponse.statusCode)
            } catch (ex: Exception) {
                println(ex.toString())
            }
        }
        callback.vivesEventDataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) { }

}