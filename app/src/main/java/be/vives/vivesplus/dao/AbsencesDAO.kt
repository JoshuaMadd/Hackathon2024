package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Absence
import be.vives.vivesplus.util.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class AbsencesDAO(val context: Context, val callback: AbsencesDAOCallback): WebServiceCallback {

    val fileName = "absences.json"
    val url="${context.getString(R.string.vivesplus_api_link)}/absences"

    fun getAbsences() {
        WebService(context, fileName, this).getJsonArrayFromUrl(url)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list: ArrayList<Absence> = ArrayList()

        for(i in 0 until jsonArray.length()) {
            try {
                val obj = jsonArray.getJSONObject(i)
                list.add(Absence(
                    "${obj.getJSONObject("lecturer").get("firstName")} ${obj.getJSONObject("lecturer").get("lastName")}",
                    DateTimeHelper().formatStringToDateTime(obj.getString("startDate")),
                    DateTimeHelper().formatStringToDateTime(obj.getString("endDate"))
                ))
            } catch (ex: Exception) {
                println(ex.toString())
            }
        }
        callback.dataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) { }
}