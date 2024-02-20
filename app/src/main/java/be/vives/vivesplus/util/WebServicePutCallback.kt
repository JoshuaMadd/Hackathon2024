package be.vives.vivesplus.util

import org.json.JSONObject

interface WebServicePutCallback {
    fun putSuccesfull()
    fun putSuccesfull(jsonObject: JSONObject)
    fun setError(error: Int){}
}