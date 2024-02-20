package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Bus
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import org.json.JSONArray
import org.json.JSONObject

class BusDAO(val context: Context, val callback: BusDAOCallback): WebServiceCallback {

    private val url = "${context.getString(R.string.vivesplus_api_link)}/campus/"
    private val fileName = "busses.json"

    fun getBusses(campusId: Int) {
        val fullurl = "$url$campusId/bus"
        WebService(context, fileName, this).getJsonArrayFromUrl(fullurl)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list: ArrayList<Bus> = ArrayList()

        for (i in 0 until jsonArray.length()) {
            try {
                val bus = jsonArray.getJSONObject(i)
                val halteId = bus.getString("halteId")
                val halteNaam = bus.getString("halteNaam")
                val adres = bus.getString("adres")
                val infoUrl = bus.getString("infoUrl")

                list.add(Bus(halteId, halteNaam, adres, infoUrl))
            }
            catch(ex: Exception) {
                println(ex.toString())
            }
        }
        callback.dataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {}

}