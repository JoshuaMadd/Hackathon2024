package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.enum.ProfileRole
import be.vives.vivesplus.model.Profile
import be.vives.vivesplus.util.PreferencesManager
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import com.android.volley.VolleyError
import org.json.JSONArray
import org.json.JSONObject
import kotlin.Exception

class ProfileDAO (val c: Context, val callback: ProfileDAOCallback?): WebServiceCallback {

    private val fileName = "profile.json"
    private val url = "${c.getString(R.string.vivesplus_api_link)}/profile"
    private val webservice = WebService(c, fileName, this)

    fun getProfile() {
        try{
            webservice.getJsonObjectFromUrl(url)
        } catch(ve: VolleyError){
            setError(ve.networkResponse.statusCode)
        } catch (ex: Exception) {
            println(ex)
        }
    }


    fun deleteProfile(){
        try{
            webservice.deleteJsonObjectToUrl(url)
        }catch(ex: Exception){
            println(ex)
        }
    }

    override fun dataLoaded(jsonArray: JSONArray) {}

    override fun dataLoaded(jsonObject: JSONObject) {
        try {
            val roleString = jsonObject.getString("role")
            var role : ProfileRole = ProfileRole.STUDENT
            if(roleString == "student") {
                role = ProfileRole.STUDENT
            } else if (roleString == "member") {
                role = ProfileRole.MEMBER
            }

            val kulNumber = jsonObject.getString("kulNumber")

            PreferencesManager().writeStringToPreferences(c, c.getString(R.string.pref_role), role.name)
            PreferencesManager().writeStringToPreferences(c, c.getString(R.string.pref_kul_number), kulNumber)

            callback!!.dataLoaded(Profile(role, kulNumber))
        } catch (ex: Exception) {
            callback!!.dataLoaded(Profile(null, null))
        }
    }

   override fun setError(error: Int) {
       callback?.setError(error)
   }
}