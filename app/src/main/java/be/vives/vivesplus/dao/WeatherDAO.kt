package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Weather
import be.vives.vivesplus.util.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class WeatherDAO(val context: Context, val callback: WeatherDAOCallback): WebServiceCallback {

    private val fileName = "EersteAanzetDashboard.json"
    private val url = "${context.getString(R.string.vivesplus_base_link)}/api/campus"

    fun getWeather(campusId: Int) {
        val fullurl = "$url/$campusId/weather"
        WebService(context, fileName, this).getJsonObjectFromUrl(fullurl)
    }

    override fun dataLoaded(jsonArray: JSONArray) {}

    override fun dataLoaded(jsonObject: JSONObject) {
        try {
            val description = jsonObject.getString("description")
            val icon = jsonObject.getString("icon")
            val temp = jsonObject.getString("temp")

            callback.dataLoaded(Weather(description, icon, temp))
        }
        catch (ex: Exception){
            println(ex.toString())
        }
    }
}
