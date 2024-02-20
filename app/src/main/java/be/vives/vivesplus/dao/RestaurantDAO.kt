package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Restaurant
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import org.json.JSONArray
import org.json.JSONObject

class RestaurantDAO(val context: Context, val callback: RestaurantDAOCallback): WebServiceCallback {

    private val url = "${context.getString(R.string.vivesplus_api_link)}/restaurants?language=nl"
    private val fileName = "restos.json"

    fun getRestos(campusId: Int) {
        val fullurl = "$url&campusId=$campusId"
        WebService(context, fileName, this).getJsonArrayFromUrl(fullurl)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list = ArrayList<Restaurant>()

        for (i in 0 until jsonArray.length()) {
            try {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getInt("id")
                val name = obj.getString("name")

                val description : String?
                if (obj.isNull("description")){
                    description = null
                }
                else {
                    description = obj.getString("description")
                }

                val link: String?
                if (obj.isNull("link")){
                    link = null
                }
                else {
                    link = obj.getString("link")
                }

                val openingsInfo: String?
                if (obj.isNull("openingsInfo")) {
                   openingsInfo = null
                }
                else {
                    openingsInfo = obj.getString("openingsInfo")
                }

                val moreButtonText: String?
                if (obj.isNull("moreButtonText")) {
                    moreButtonText = null
                }
                else {
                    moreButtonText = obj.getString("moreButtonText")
                }

                list.add(Restaurant(id, name, description, link, openingsInfo, moreButtonText))
            } catch (ex: Exception) {
                println(ex.toString())
            }
        }

        callback.dataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {}
}