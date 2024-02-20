package be.vives.vivesplus.util

import org.json.JSONArray
import org.json.JSONObject

interface  WebServiceCallback {
    fun dataLoaded(jsonArray: JSONArray)
    fun dataLoaded(jsonObject: JSONObject)
    fun setError(error: Int){}
}