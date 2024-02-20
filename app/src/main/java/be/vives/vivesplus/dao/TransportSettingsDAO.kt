package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.TransportOption
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import org.json.JSONArray
import org.json.JSONObject

class TransportSettingsDAO(val context: Context, private val callback: TransportSettingsDAOCallback): WebServiceCallback{

    private val url = "${context.getString(R.string.vivesplus_api_link)}/meansoftransportation"
    private val fileName = "transportOptions.json"

    fun getAll() {
        WebService(context, fileName, this).getJsonArrayFromUrl(url)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list = ArrayList<TransportOption>()
        for (i in 0 until jsonArray.length()) {
            try {
                val transportOptie = jsonArray.getJSONObject(i)
                val id = transportOptie.getInt("id")
                val name = transportOptie.getString("name")
                list.add(TransportOption(id, name))
            } catch (ex: Exception) {
                println(ex.toString())
            }
        }
        callback.transportSettingsDataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {
    }

}