package be.vives.vivesplus.screens.identity

import android.content.Context
import android.util.Log
import android.widget.Toast
import be.vives.vivesplus.R
import be.vives.vivesplus.model.CodeQR
import be.vives.vivesplus.model.JsonBody
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import be.vives.vivesplus.util.WebservicePostCallback
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class PresenceQRDAO  (val context: Context, val callback: PresenceQrDAOCallback):
    WebServiceCallback, WebservicePostCallback {



    private val url = "${context.getString(R.string.vivesplus_api_link)}/whoami" //endpoint veranderen
    private val fileName = "identity-qr.json"




    fun getQRCode(){
        WebService(context, fileName, this).getJsonObjectFromUrl(url)
    }

    override fun dataLoaded(jsonArray: JSONArray) {}

    override fun dataLoaded(jsonObject: JSONObject) {

        try {
            val kulNumber = jsonObject.getString("kulNumber")
            val firstName = jsonObject.getString("firstName")
            val lastName = jsonObject.getString("lastName")
            callback.dataLoaded(CodeQR(kulNumber, firstName, lastName))
        }
        catch (ex: Exception){
            println(ex.toString())
            callback.dataLoaded(CodeQR("","",""))
        }
    }

    override fun setError(error: Int) {
        callback.setErrorPost(error)
    }

    fun postPresence(kulNumber: String, firstName: String, lastName: String, url: String) {
        WebService(context, fileName, this).postJsonObjectToUrl(
            url,
            mutableListOf(
                JsonBody("kulNumber", null, kulNumber, null, null),
                JsonBody("firstName", null, firstName, null, null),
                JsonBody("lastName", null, lastName, null, null)
            ),
            this
        )

    }

    override fun postSuccesfull() {

    }

    override fun postSuccesfull(response: String) {
        dataLoaded(JSONObject(response))
    }

    override fun setErrorPost(error: Int) {
        callback.setErrorPost(error)
    }
}





