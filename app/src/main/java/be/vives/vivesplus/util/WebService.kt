package be.vives.vivesplus.util

import android.content.Context
import android.util.Log
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.AdminAbsencesDAO
import be.vives.vivesplus.model.HttpHeader
import be.vives.vivesplus.model.JsonBody
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class WebService(val context: Context, val filename: String, val callback: WebServiceCallback) {

    private var queue: RequestQueue = Volley.newRequestQueue(context)
    private val parameters: MutableMap<String, String> = HashMap()

    init {
        var version =
            context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName
        var kulnummer = PreferencesManager().getStringFromPreferences(
            context,
            context.getString(R.string.kulNumber)
        ).toString()
        val authorization = PreferencesManager().getStringFromPreferences(
            context,
            context.getString(R.string.pref_jwt)
        )!!
        val os = context.getString(R.string.os_android)
        val language = Locale.getDefault().displayLanguage

        var headers = mutableListOf<HttpHeader>()
        var langheadervalue = "nl"

        if (version.isEmpty()) {
            version = context.getString(R.string.undefined)
        }

        if (kulnummer.isEmpty()) {
            kulnummer = context.getString(R.string.undefined)
        }

        if (language == "English") {
            langheadervalue = "en-US"
        }
        if (filename != "connections.json") {
            headers = mutableListOf(
                HttpHeader("Authorization", authorization),
                HttpHeader("Accept-Language", langheadervalue),
                HttpHeader("os", os),
                HttpHeader("version", version),
                HttpHeader("kulnumber", kulnummer)
            )
        }
        for (i in 0 until headers.size) {
            parameters[headers[i].key] = headers[i].value
        }
    }

    fun getJsonArrayFromUrl(url: String) {
        println("--------------webservice-------------" + url)
        if (CheckerConnection(context).hasInternetConnection()) {
            val jsonArrayRequest = object : JsonArrayRequest(Method.GET, url, null,
                Response.Listener { response ->
                    LocalFileManager(context, filename).saveStringToFile(response.toString())
                    if(url.contains("/members/me")) PreferencesManager().writeIntToPreferences(context,"aantalRotaties",0)
                    callback.dataLoaded(response)
                },
                Response.ErrorListener {
                    if(it.networkResponse == null){
                        loadDataFromLocalArrayFile()
                    } else if(it.networkResponse.statusCode == 429){
                        loadDataFromLocalArrayFile()
                    } else {
                        callback.setError(it.networkResponse.statusCode)
                    }
                }
            )
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return parameters
                }
            }

            if (url.contains("/dashboard") || url.contains("/events")) {
                jsonArrayRequest.retryPolicy = DefaultRetryPolicy(
                    TimeUnit.SECONDS.toMillis(60).toInt(),
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                );
            }
            // Add the request to the RequestQueue.
            queue.add(jsonArrayRequest)
        } else {
            loadDataFromLocalArrayFile()
        }
    }

    fun getJsonObjectFromUrl(url: String) {
        println("--------------webservice-------------" + url)
        if (CheckerConnection(context).hasInternetConnection()) {
            val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
                Response.Listener { response ->
                    LocalFileManager(context, filename).saveStringToFile(response.toString())
                    callback.dataLoaded(response as JSONObject)
                },
                Response.ErrorListener {
                    if(it.networkResponse == null){
                        loadDataFromLocalJsonFile()
                    } else if(it.networkResponse.statusCode == 429){
                        loadDataFromLocalJsonFile()
                    } else {
                        callback.setError(it.networkResponse.statusCode)
                    }
                }
            )
            {
                override fun getHeaders(): MutableMap<String, String> {
                    return parameters
                }
            }

            if(url.contains("/dashboard") || url.contains("/events")){
                jsonObjectRequest.retryPolicy = DefaultRetryPolicy(
                    TimeUnit.SECONDS.toMillis(60).toInt(),
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                );
            }
            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)
        } else {
            loadDataFromLocalJsonFile()

        }
    }

    fun putJsonObjectToUrl(url: String, jsonContent: MutableList<JsonBody>, putCallback: WebServicePutCallback) {
        println("--------------webservice-------------" + url)
        val jsonObject = JSONObject()
        try {
            for (line in 0 until jsonContent.size) {
                val currentLine = jsonContent[line]
                if (jsonContent[line].valueInt != null)
                    jsonObject.put(currentLine.name, currentLine.valueInt)
                else if (jsonContent[line].valueArray != null) {
                    jsonObject.put(currentLine.name, JSONArray("${jsonContent[line].valueArray}"))
                } else if (jsonContent[line].valueString != null) {
                    jsonObject.put(currentLine.name, currentLine.valueString)
                } else if (jsonContent[line].valueBoolean != null) {
                    jsonObject.put(currentLine.name, currentLine.valueBoolean)
                }
            }
            Log.i("Settingstest", jsonObject.toString())

        } catch (ex: java.lang.Exception) {
            println(ex.toString())
        }
        if (CheckerConnection(context).hasInternetConnection()) {
            val jsonObjectRequest = object : JsonObjectRequest(Method.PUT, url, null,
                Response.Listener {
                    if (it.toString().isNotEmpty()) {
                        putCallback.putSuccesfull(it)
                    } else {
                        putCallback.putSuccesfull()
                    }
                },
                Response.ErrorListener {}
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    return parameters
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getBody(): ByteArray {
                    return try {
                        jsonObject.toString().toByteArray(Charset.defaultCharset())
                    } catch (ex: java.lang.Exception) {
                        println(ex.toString())
                        byteArrayOf()
                    }
                }
            }

            queue.add(jsonObjectRequest)
        }
    }

    fun postJsonObjectToUrl(url: String, jsonContent: MutableList<JsonBody>, postCallback: WebservicePostCallback) {
        println("--------------webservice-------------" + url)
        val jsonObject = JSONObject()
        try {
            for (line in 0 until jsonContent.size) {
                val currentLine = jsonContent[line]
                if (jsonContent[line].valueString != null)
                    jsonObject.put(currentLine.name, currentLine.valueString)
                else if (jsonContent[line].valueArray != null) {
                    //val arr = currentLine.array?.toTypedArray()
                    val arr = currentLine.valueArray?.get(0).toString()
                    jsonObject.put(currentLine.name, arr)
                }
            }
        } catch (ex: java.lang.Exception) {
            println(ex.toString())
        }

        if (CheckerConnection(context).hasInternetConnection()) {
            val jsonObjectRequest = object : StringRequest(Method.POST, url,
                Response.Listener {
                    Log.i("RESPONSE", it.toString());
                    if (url.contains("registrations")){
                        postCallback.postSuccesfull(jsonObject.toString())
                    } else if (it.toString().isNotEmpty()) {
                        postCallback.postSuccesfull(it)
                    } else {
                        postCallback.postSuccesfull()
                    }
                },
                Response.ErrorListener {
                    if(it.networkResponse != null){
                        postCallback.setErrorPost(it.networkResponse.statusCode)

                    }
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    return parameters
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }

                override fun getBody(): ByteArray {
                    return try {
                        jsonObject.toString().toByteArray(Charset.defaultCharset())
                    } catch (ex: java.lang.Exception) {
                        println(ex.toString())
                        byteArrayOf()
                    }
                }
            }

            queue.add(jsonObjectRequest)
        }
    }

    fun deleteJsonObjectToUrl(url: String) {

        if (CheckerConnection(context).hasInternetConnection()) {
            val jsonObjectRequest = object : JsonObjectRequest(Method.DELETE, url, null,
                Response.Listener {},
                Response.ErrorListener {}
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    return parameters
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

            queue.add(jsonObjectRequest)
        }
    }

    private fun loadDataFromLocalArrayFile() {
        val str = LocalFileManager(context, filename).getStringFromFile()
        if (str.isEmpty()) {
            callback.dataLoaded(JSONArray())
        } else {
            var jsonArray = JSONArray()
            try {
                jsonArray = JSONArray(str)
            } catch (ex: Exception) {
                println(ex.toString())
            }
            callback.dataLoaded(jsonArray)
        }
    }

    private fun loadDataFromLocalJsonFile() {
        val str = LocalFileManager(context, filename).getStringFromFile()
        if(str.isEmpty()) {
            callback.dataLoaded(JSONObject())
        } else {
            var jsonObject = JSONObject()
            try {
                jsonObject = JSONObject(str)
            } catch (ex: Exception) {
                println(ex.toString())
            }
            callback.dataLoaded(jsonObject)
        }
    }
}