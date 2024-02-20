package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Station
import be.vives.vivesplus.screens.traffic.train.TrainFragment
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import org.json.JSONArray
import org.json.JSONObject

class TrainSearchDAO(val context: Context, val callback: TrainFragment): WebServiceCallback {

    private val url = "${context.getString(R.string.vivesplus_api_link)}/stations"
    private val fileName = "stations.json"

    fun getStation() {
        WebService(context, fileName, this).getJsonArrayFromUrl(url)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list: ArrayList<Station> = ArrayList()

        for (i in 0 until jsonArray.length()) {
            try {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getInt("id")
                val name = obj.getString("name")
                val campusId = obj.get("campusId")

                list.add(Station(name, id, campusId))
            } catch (ex: Exception) {
                println(ex.toString())
            }
        }
       callback.dataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {

    }

}