package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.*
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import be.vives.vivesplus.util.WebServicePutCallback
import com.android.volley.VolleyError
import org.json.JSONArray
import org.json.JSONObject

class MembersDAO(val context: Context, private val callback: MembersDAOCallback): WebServiceCallback, WebServicePutCallback {

    private val fileName = "members.json"
    private val url = "${context.getString(R.string.vivesplus_api_link)}/members"

    fun getInfo() {
        val getMeUrl = "$url/me"
        WebService(context, fileName, this).getJsonObjectFromUrl(getMeUrl)
    }

    fun getInfoBasedOnKulNumber(kulNumber: String) {
        val getMeUrl = "$url/$kulNumber"
        WebService(context, fileName, this).getJsonObjectFromUrl(getMeUrl)
    }

    fun getMembersBasedOnFieldOfStudy(fieldOfStudyId: Int) {
        val fosurl = "$url/search?fieldOfStudyId=$fieldOfStudyId"
        WebService(context, fileName, this).getJsonArrayFromUrl(fosurl)
    }

    fun put(member: Member, campusIds: ArrayList<Int>, fieldOfStudiesIds: ArrayList<Int>, meansOfTransportationIds: ArrayList<Int>) {
        WebService(context, fileName, this).putJsonObjectToUrl(
            "$url/me",
            mutableListOf(
                JsonBody("firstName", null, member.firstName, null, null),
                JsonBody("phoneNumber", null, member.phoneNumber, null, null),
                JsonBody("functieOmschrijving", null, member.functieOmschrijving, null, null),
                JsonBody("isBachelor", null, null, null, member.bachelor),
                JsonBody("isGraduaat", null, null, null, member.graduaat),
                JsonBody("campusIds", null, null, campusIds, null),
                JsonBody("fieldOfStudiesIds", null, null, fieldOfStudiesIds, null),
                JsonBody("meansOfTransportationIds", null, null, meansOfTransportationIds, null),
            ),
            this
        )
    }

    fun putRotateProfile() {
        val putMeUrl = "$url/me/photo/rotate"
println("turn picture")
        try {
            WebService(context, fileName, this).putJsonObjectToUrl(putMeUrl, mutableListOf(),this)
        }catch (ve: VolleyError){
            callback.setError(ve.networkResponse.statusCode)
            println(ve.localizedMessage)
        }catch (ex: java.lang.Exception){
            println(ex)
        }
    }


    override fun dataLoaded(jsonArray: JSONArray) {
        val list: MutableList<Member> = mutableListOf()
        for(i in 0 until jsonArray.length()) {
            try {
                val obj = jsonArray.getJSONObject(i)
                val firstName = obj.getString("firstName")
                val lastName = obj.getString("lastName")

                var photoUrl : String? = null
                if(!obj.getString("photoUrl").equals("null")) {
                    photoUrl = obj.getString("photoUrl")
                }

                val kulNumber = obj.getString("kulNumber")
                val phoneNumber = obj.getString("phoneNumber")
                val functieOmschrijving = obj.getString("functieOmschrijving")
                val email = obj.getString("email")

                val campusses : ArrayList<Campus> = ArrayList()
                try {
                    val campusarrayjson = obj.getJSONArray("campusses")
                    for(p in 0 until campusarrayjson.length()) {
                        val campusJSON = campusarrayjson.getJSONObject(p)
                        val campus = Campus(campusJSON.getInt("id"), campusJSON.getString("name"))
                        campusses.add(campus)
                    }
                } catch (ex: java.lang.Exception) {

                }

                val fieldOfStudies : ArrayList<FieldOfStudy> = ArrayList()
                try {
                    val fieldOfStudiesarrayjson = obj.getJSONArray("fieldOfStudies")
                    for(p in 0 until fieldOfStudiesarrayjson.length()) {
                        val fieldOfStudieJSON = fieldOfStudiesarrayjson.getJSONObject(p)
                        val fieldOfStudie = FieldOfStudy(fieldOfStudieJSON.getInt("id"), fieldOfStudieJSON.getString("name"))
                        fieldOfStudies.add(fieldOfStudie)
                    }
                } catch (ex: Exception) {

                }

                val studies : ArrayList<Study> = ArrayList()
                try {
                    val studiesarrayjson = obj.getJSONArray("studies")
                    for(p in 0 until studiesarrayjson.length()) {
                        val studieJSON = studiesarrayjson.getJSONObject(p)
                        val studie = Study(studieJSON.getInt("id"), studieJSON.getString("name"))
                        studies.add(studie)
                    }
                } catch (ex: java.lang.Exception) {

                }

                val transportOptions : ArrayList<TransportOption> = ArrayList()
                try {
                    val transportarrayjson = obj.getJSONArray("meansOfTransportations")
                    for(p in 0 until transportarrayjson.length()) {
                        val transportJSON = transportarrayjson.getJSONObject(p)
                        val transportOption = TransportOption(transportJSON.getInt("id"), transportJSON.getString("name"))
                        transportOptions.add(transportOption)
                    }
                } catch (ex: Exception) {

                }

                val bachelor: Boolean = try {
                    obj.getBoolean("bachelor")
                } catch (ex: Exception) {
                    false
                }

                val graduaat: Boolean = try {
                    obj.getBoolean("graduaat")
                } catch (ex: Exception) {
                    false
                }

                list.add(Member(firstName, lastName, photoUrl, kulNumber,phoneNumber, functieOmschrijving, email, campusses, fieldOfStudies, studies, transportOptions ,bachelor, graduaat))
            } catch (ex: Exception) {
                print(ex.toString())
            }
        }
        callback.dataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {
        try {
            val firstName = jsonObject.getString("firstName")
            val lastName = jsonObject.getString("lastName")

            var photoUrl : String? = null
            if(!jsonObject.getString("photoUrl").equals("null")) {
                photoUrl = jsonObject.getString("photoUrl")
            }

            val kulNumber = jsonObject.getString("kulNumber")
            val phoneNumber = jsonObject.getString("phoneNumber")
            val functieOmschrijving = jsonObject.getString("functieOmschrijving")
            val email = jsonObject.getString("email")

            val campusses: ArrayList<Campus> = ArrayList()
            val campusarrayjson = jsonObject.getJSONArray("campusses")
            for (p in 0 until campusarrayjson.length()) {
                val campusJSON = campusarrayjson.getJSONObject(p)
                val campus = Campus(campusJSON.getInt("id"), campusJSON.getString("name"))
                campusses.add(campus)
            }

            val fieldOfStudies: ArrayList<FieldOfStudy> = ArrayList()
            val fieldOfStudiesarrayjson = jsonObject.getJSONArray("fieldOfStudies")
            for (p in 0 until fieldOfStudiesarrayjson.length()) {
                val fieldOfStudieJSON = fieldOfStudiesarrayjson.getJSONObject(p)
                val fieldOfStudie = FieldOfStudy(
                    fieldOfStudieJSON.getInt("id"),
                    fieldOfStudieJSON.getString("name")
                )
                fieldOfStudies.add(fieldOfStudie)
            }

            val studies: ArrayList<Study> = ArrayList()
            val studiesarrayjson = jsonObject.getJSONArray("studies")
            for (p in 0 until studiesarrayjson.length()) {
                val studieJSON = studiesarrayjson.getJSONObject(p)
                val studie = Study(studieJSON.getInt("id"), studieJSON.getString("name"))
                studies.add(studie)
            }

            val transportOptions : ArrayList<TransportOption> = ArrayList()
            val transportarrayjson = jsonObject.getJSONArray("meansOfTransportations")
            for(p in 0 until transportarrayjson.length()) {
                val transportJSON = transportarrayjson.getJSONObject(p)
                val transportOption = TransportOption(transportJSON.getInt("id"), transportJSON.getString("name"))
                transportOptions.add(transportOption)
            }

            val bachelor = jsonObject.getBoolean("bachelor")
            val graduaat = jsonObject.getBoolean("graduaat")

            callback.dataLoaded(Member(firstName, lastName, photoUrl, kulNumber, phoneNumber, functieOmschrijving, email, campusses, fieldOfStudies, studies, transportOptions, bachelor, graduaat))

        } catch (ex: Exception) {
            print(ex.toString())
        }
    }

    override fun putSuccesfull() {
        callback.putSucces()
    }

    override fun putSuccesfull(jsonObject: JSONObject) {
        callback.putSucces()
    }

    override fun setError(error: Int) {
        callback.setError(error)
    }
}