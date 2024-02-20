package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.model.Connection
import be.vives.vivesplus.model.ConnectionOverstap
import be.vives.vivesplus.model.ConnectionStop
import be.vives.vivesplus.screens.traffic.train.TrainFragment
import be.vives.vivesplus.util.DateTimeHelper
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class TrainDAO(val context: Context, val callback: TrainFragment): WebServiceCallback {

    private val url = "https://api.irail.be/connections/"
    private val fileName = "connections.json"

    fun getConnection(from: String, to: String, results: Int) {
        WebService(context, fileName, this).getJsonObjectFromUrl("$url${buildParams(from, to, results)}")
    }

    private fun buildParams(from: String, to: String, results: Int): String {
        val helper = DateTimeHelper()
        return "?from=$from&to=$to&date=${helper.getNMBSDate()}&time=${helper.getNMBSTime()}&timesel=departure&format=json&lang=en&alerts=true&typeOfTransport=automatic&results=$results"
    }

    override fun dataLoaded(jsonArray: JSONArray) { }

    override fun dataLoaded(jsonObject: JSONObject) {
        val list: ArrayList<Connection> = ArrayList()
        val array = jsonObject.getJSONArray("connection")

        for(i in 0 until array.length()) {
            try {
                val obj = array.getJSONObject(i)
                val departure = buildMainStopFromJson(obj.getJSONObject("departure"))
                val arrival = buildMainStopFromJson(obj.getJSONObject("arrival"))

                val stopsList: MutableList<ConnectionStop> = mutableListOf()
                val overstapList: MutableList<ConnectionOverstap> = mutableListOf()

                if(obj.getJSONObject("departure").has("stops")) {
                    val stops = obj.getJSONObject("departure").getJSONObject("stops").getJSONArray("stop")
                    for(s in 0 until stops.length()) {
                        stopsList.add(buildStopFromJson(stops.getJSONObject(s)))
                    }
                }

                if(obj.has("vias")) {
                    val vias = obj.getJSONObject("vias").getJSONArray("via")
                    for(v in 0 until vias.length()) {
                        val station = vias.getJSONObject(v).getString("station")
                        val arrivalPerron = vias.getJSONObject(v).getJSONObject("arrival").getString("platform")
                        val departurePerron = vias.getJSONObject(v).getJSONObject("departure").getString("platform")
                        val time = vias.getJSONObject(v).getString("timeBetween").toInt() / 60 //word in sec teruggegeven

                        overstapList.add(ConnectionOverstap(station, arrivalPerron, departurePerron, time.toString()))

                        stopsList.add(buildOverstapStopFromJson(vias.getJSONObject(v)))

                        if(vias.getJSONObject(v).getJSONObject("departure").has("stops")) {
                            val stops = vias.getJSONObject(v).getJSONObject("departure").getJSONObject("stops").getJSONArray("stop")
                            for(s in 0 until stops.length()) {
                                stopsList.add(buildStopFromJson(stops.getJSONObject(s)))
                            }
                        }
                    }
                }

                list.add(Connection(departure, arrival, stopsList, overstapList))

            } catch (ex: Exception) {
                println(ex.toString())
            }
        }

        callback.dataLoaded(list)
    }

    private fun buildMainStopFromJson(jsonObject: JSONObject): ConnectionStop {
        val station = jsonObject.getString("station")
        val time = DateTimeHelper().epochToDateTimeString(jsonObject.getString("time"))
        val perron = jsonObject.getString("platform")
        val delay = jsonObject.getString("delay").toInt() / 60

        return ConnectionStop(station, time, perron, delay.toString())
    }

    private fun buildStopFromJson(jsonObject: JSONObject): ConnectionStop {
        val station = jsonObject.getString("station")
        val time = DateTimeHelper().epochToDateTimeString(jsonObject.getString("scheduledArrivalTime"))
        val delay = jsonObject.getString("arrivalDelay").toInt() / 60

        return ConnectionStop(station, time, null, delay.toString())
    }

    private fun buildOverstapStopFromJson(jsonObject: JSONObject): ConnectionStop {
        val station = jsonObject.getString("station")
        val time = DateTimeHelper().epochToDateTimeString(jsonObject.getJSONObject("departure").getString("time"))
        val delay = jsonObject.getJSONObject("departure").getString("delay").toInt() / 60

        return ConnectionStop(station, time, null, delay.toString())
    }


}