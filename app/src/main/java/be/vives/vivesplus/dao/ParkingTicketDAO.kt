package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.ParkeerTicket
import be.vives.vivesplus.util.*
import org.json.JSONArray
import org.json.JSONObject

class ParkingTicketDAO(val context: Context, val callback: ParkingTicketDAOCallback): WebServiceCallback {

    private val url = "${context.getString(R.string.vivesplus_api_link)}/qrcodes/parking/xpo"
    private val fileName = "ticket.json"


    fun getTicketCode(){
        WebService(context, fileName, this).getJsonObjectFromUrl(url)
    }

    override fun dataLoaded(jsonArray: JSONArray) {}

    override fun dataLoaded(jsonObject: JSONObject) {

        try {
            val code = jsonObject.getString("code")
            val validDate = jsonObject.getString("validDate")
            callback.dataLoaded(ParkeerTicket(code, validDate))
        }
        catch (ex: Exception){
            println(ex.toString())
            callback.dataLoaded(ParkeerTicket("",""))
        }
    }

    override fun setError(error: Int) {
        callback.setError(error)
    }
}