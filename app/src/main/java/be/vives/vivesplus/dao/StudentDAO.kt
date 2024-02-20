package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.*
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import be.vives.vivesplus.util.WebServicePutCallback
import org.json.JSONArray
import org.json.JSONObject

class StudentDAO(val context: Context, val callback: StudentDAOCallback?): WebServiceCallback, WebServicePutCallback {

    private val url = "${context.getString(R.string.vivesplus_api_link)}/students"
    private val fileName = "student.json"
    private val webservice = WebService(context, fileName, this)

    fun getMe() {
        val urlGetMe = "${url}/me"
        webservice.getJsonObjectFromUrl(urlGetMe)
    }

    fun put(campusId: Int?, fosId: Int?, meansOfTransportationIds: ArrayList<Int>, optInJob : Boolean) {
        WebService(context, fileName, this).putJsonObjectToUrl(
            "$url/me",
            mutableListOf(
                JsonBody("campusId", campusId, null, null, null),
                JsonBody("fieldOfStudyId", fosId, null, null, null),
                JsonBody("meansOfTransportationIds", null, null, meansOfTransportationIds, null),
                JsonBody("optInJob", null, null, null, optInJob),
                ),
            this
        )
    }

    override fun dataLoaded(jsonArray: JSONArray) {}

    override fun dataLoaded(jsonObject: JSONObject) {
        try {
            val kulNumber = jsonObject.getString("kulNumber")
            val email = jsonObject.getString("email")
            val firstName = jsonObject.getString("firstName")
            val lastName = jsonObject.getString("lastName")

            val study : Study? = try {
                val studyJson = jsonObject.getJSONObject("study")
                Study(studyJson.getInt("id"), studyJson.getString("name"))
            } catch (ex: Exception) {
                null
            }

            val campus : Campus? = try {
                val campusJson = jsonObject.getJSONObject("campus")
                Campus(campusJson.getInt("id"), campusJson.getString("name"))
            } catch (ex: Exception) {
                null
            }

            val fieldOfStudy : FieldOfStudy? = try {
                val fieldOfStudyJson = jsonObject.getJSONObject("fieldOfStudy")
                FieldOfStudy(fieldOfStudyJson.getInt("id"), fieldOfStudyJson.getString("name"))
            } catch (ex: Exception) {
                null
            }

            val transportOptions : ArrayList<TransportOption> = ArrayList()
            val transportarrayjson = jsonObject.getJSONArray("meansOfTransportations")
            for(p in 0 until transportarrayjson.length()) {
                val transportJSON = transportarrayjson.getJSONObject(p)
                val transportOption = TransportOption(transportJSON.getInt("id"), transportJSON.getString("name"))
                transportOptions.add(transportOption)
            }

            val optInJob = jsonObject.getBoolean("optInJob")

            val student = Student(kulNumber, email, firstName, lastName, study, campus, fieldOfStudy, transportOptions, optInJob)
            callback!!.dataLoaded(student)
        } catch (ex: Exception) {
            callback!!.dataLoadedFailed()
        }
    }

    override fun putSuccesfull() {}

    override fun setError(error: Int) {
    }

    override fun putSuccesfull(jsonObject: JSONObject) {
        callback!!.putSucces()
    }
}