package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.model.FieldOfStudy
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FieldOfStudyDAO(val context: Context, private val callback: FieldOfStudyDAOCallback): WebServiceCallback {

    private val url = "${context.getString(R.string.vivesplus_api_link)}/fieldofstudies"
    private val fileName = "studiegebied.json"

    fun getAll() {
        WebService(context, fileName, this).getJsonArrayFromUrl(url)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list: ArrayList<FieldOfStudy> = ArrayList()
        for(i in 0 until jsonArray.length()) {
            try {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getInt("id")
                val name = obj.getString("name")
                val campusLst = ArrayList<Campus>()

                for (c in 0 until obj.getJSONArray("campusses").length()) {
                    val campus = obj.getJSONArray("campusses").getJSONObject(c)
                    val cId = campus.getInt("id")
                    val cName = campus.getString("name")
                    campusLst.add(Campus(cId, cName))
                }

                list.add(FieldOfStudy(id, name))
            } catch (ex: Exception) {
                println(ex.toString())
            }
        }
        callback.dataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {}
}