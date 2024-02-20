package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.enum.EventType
import be.vives.vivesplus.model.*
import be.vives.vivesplus.util.EventTypeMapper
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import com.android.volley.VolleyError
import org.json.JSONArray
import org.json.JSONObject

class EventDetailDAO(val context: Context, private val callback: EventDetailDAOCallback) : WebServiceCallback {

    private val url = "${context.getString(R.string.vivesplus_api_link)}/events"
    private val fileName = "detailevents.json"

    fun getAnEventDetail(type : String, id : String){
        try{
            WebService(context, fileName, this).getJsonObjectFromUrl("${url}/${type}/${id}")
        } catch (ve: VolleyError){
            setError(ve.networkResponse.statusCode)
        } catch (ex: Exception){
            println(ex)
        }
    }

    override fun dataLoaded(jsonArray: JSONArray) {
    }

    override fun dataLoaded(jsonObject: JSONObject) {
        val eventTypeMapper = EventTypeMapper()

        try{
            val id = jsonObject.getString("id")
            val startDateTime = jsonObject.getString("startDateTime")
            val endDateTime = jsonObject.getString("endDateTime")
            val description = jsonObject.getString("description")
            val allDay = jsonObject.getBoolean("allDay")
            val locations : ArrayList<Location> = ArrayList()
            try{
                val locationsJson = jsonObject.getJSONArray("locations")
                for(l in 0 until locationsJson.length()){
                    val locationJSON = locationsJson.getJSONObject(l)
                    val coordinates : Coordinates
                    if(locationJSON.isNull("coordinates")) {
                        coordinates = Coordinates(0.0, 0.0)
                    } else {
                        coordinates = Coordinates(
                            locationJSON.getJSONObject("coordinates").getDouble("latitude"),
                            locationJSON.getJSONObject("coordinates").getDouble("longitude")
                        )
                    }
                    val location = Location(
                        locationJSON.getString("description"),
                        locationJSON.getString("buildingName"),
                        locationJSON.getString("address"),
                        coordinates
                    )
                    locations.add(location)
                }
            } catch (ex: Exception){
                println(ex)
            }



            val eventTypeJson = jsonObject.getString("type")
            val type = eventTypeMapper.mapJsonToEventType(eventTypeJson)

            when (type) {
                EventType.ACADEMIC -> {
                    callback.eventDetailDataLoaded(
                        GenericEventDetail(id, startDateTime, endDateTime, description, allDay, locations, type)
                    )
                }
                EventType.COURSE -> {
                    val ectsCode = jsonObject.getString("ectsCode")
                    val groupInfo = jsonObject.getString("groupInfo")
                    val teachers : ArrayList<Teacher> = ArrayList()
                    try {
                        val teachersJson = jsonObject.getJSONArray("teachers")
                        for (t in 0 until teachersJson.length()){
                            val teacherJson = teachersJson.getJSONObject(t)
                            val teacher = Teacher(
                                teacherJson.getString("id"),
                                teacherJson.getString("name"),
                                teacherJson.getString("kulNumber"),
                                teacherJson.getBoolean("absent")
                            )
                            teachers.add(teacher)
                        }
                    } catch (ex: Exception){
                        println(ex)
                    }

                    callback.eventDetailDataLoaded(
                        CourseEventDetail(id, startDateTime, endDateTime, description, allDay, locations, teachers, type, ectsCode, groupInfo)
                    )
                }
                EventType.CANCELLED -> {
                    //no teacher and no location
                    val ectsCode = jsonObject.getString("ectsCode")
                    val groupInfo = jsonObject.getString("groupInfo")

                    callback.eventDetailDataLoaded(
                        CourseEventDetail(id, startDateTime, endDateTime, description, allDay, locations, null, type, ectsCode, groupInfo)
                    )

                }
            }
        } catch (ex: Exception){
            println(ex.message)
        }
    }

    override fun setError(error: Int) {
        callback.setError(error)
    }
}