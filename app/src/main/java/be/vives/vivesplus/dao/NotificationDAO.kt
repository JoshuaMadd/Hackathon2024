package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.JsonBody
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import be.vives.vivesplus.util.WebservicePostCallback
import org.json.JSONArray
import org.json.JSONObject

class NotificationDAO(val context: Context, val callback: NotificationDAOCallback): WebServiceCallback ,WebservicePostCallback {

    val fileName = "notifications.json"
    val url = "${context.getString(R.string.vivesplus_api_link)}/notifications/android"

    fun post(handle: String?) {
        if(handle != ""){
            WebService(context, fileName, this).postJsonObjectToUrl(url,
                mutableListOf(
                    JsonBody("deviceToken", null, handle, null, null)
                ), this@NotificationDAO)
        } else {
            print("error")
        }
    }

    override fun postSuccesfull() {
        callback.postOrPutSuccesful()
    }

    override fun postSuccesfull(response: String) {
        callback.postOrPutSuccesful()
    }

    override fun dataLoaded(jsonArray: JSONArray) {
    }

    override fun dataLoaded(jsonObject: JSONObject) {
    }

    override fun setErrorPost(error: Int) {
        callback.setErrorPost(error)
    }
}