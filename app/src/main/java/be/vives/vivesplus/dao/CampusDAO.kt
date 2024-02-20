package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class CampusDAO(val context: Context, private val callback: CampusDAOCallback): WebServiceCallback {

    private val url = "${context.getString(R.string.vivesplus_api_link)}/campus"
    private val fileName = "campus.json"

    fun get() {
        WebService(context, fileName, this).getJsonArrayFromUrl(url)
    }

    fun get(fieldOfStudy:Int) {
        val urlFiltered = "$url?fieldOfStudyId=$fieldOfStudy"
        WebService(context, fileName, this).getJsonArrayFromUrl(urlFiltered)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list = ArrayList<Campus>()
        for (i in 0 until jsonArray.length()) {
            try {
                val campus = jsonArray.getJSONObject(i)
                val id = campus.getInt("id")
                val name = campus.getString("name")
                list.add(Campus(id, name))
            } catch (ex: Exception) {
                println(ex.toString())
            }
        }
        callback.campusDataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) { }
}