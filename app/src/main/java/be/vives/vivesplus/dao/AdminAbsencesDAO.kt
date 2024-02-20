package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.AdminAbsence
import be.vives.vivesplus.model.JsonBody
import be.vives.vivesplus.util.*
import org.json.JSONArray
import org.json.JSONObject

class AdminAbsencesDAO(val context: Context, val callback: AdminAbsencesDAOCallback): WebServiceCallback, WebServicePutCallback, WebservicePostCallback {

    val fileName = "adminabsences.json"
    private val fileEdit = "adminabsencesedit.json"
    val url = "${context.getString(R.string.vivesplus_api_link)}/admin/absences"

    fun get() {
        WebService(context, fileName, this).getJsonArrayFromUrl(url)
    }

    fun getById(id: Int) {
        WebService(context, fileEdit, this).getJsonObjectFromUrl("$url/$id")
    }

    fun post(start: String, end: String, remark: String) {
        WebService(context, fileName, this).postJsonObjectToUrl(
            url,
            mutableListOf(
                JsonBody("startDate", null, start, null, null),
                JsonBody("endDate", null, end, null, null),
                JsonBody("remark", null, remark, null, null)
            ),
            this@AdminAbsencesDAO
        )
    }

    fun put(start: String, end: String, remark: String, id: Int) {
        WebService(context, fileName, this).putJsonObjectToUrl(
            "$url/$id",
            mutableListOf(
                JsonBody("startDate", null, start, null, null),
                JsonBody("endDate", null, end, null, null),
                JsonBody("remark", null, remark, null, null)
            ),
            this
        )
    }

    fun delete(id: Int) {
        WebService(context, fileName, this).deleteJsonObjectToUrl("$url/$id")
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list = ArrayList<AdminAbsence>()

        for (i in 0 until jsonArray.length()) {
            try {
                val obj = jsonArray.getJSONObject(i)
                val start = DateTimeHelper().formatStringToLocalDateTime(obj.getString("startDate"))
                val end = DateTimeHelper().formatStringToLocalDateTime(obj.getString("endDate"))
                val id = obj.getInt("id")
                var remark: String? = obj.getString("remark")
                if (remark == "null") {
                    remark = null
                }
                list.add(AdminAbsence(id, start, end, remark))
            } catch (ex: Exception) {
                println(ex.toString())
            }
        }
        callback.dataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {
        val list = ArrayList<AdminAbsence>()
        try {
            val start = DateTimeHelper().formatStringToLocalDateTime(jsonObject.getString("startDate"))
            val end = DateTimeHelper().formatStringToLocalDateTime(jsonObject.getString("endDate"))
            val id = jsonObject.getInt("id")
            var remark: String? = jsonObject.getString("remark")
            if (remark == "null") {
                remark = null
            }
            list.add(AdminAbsence(id, start, end, remark))
        } catch (ex: Exception) {
            println(ex.toString())
        }
        callback.dataLoaded(list)
    }

    override fun putSuccesfull() {
        callback.postOrPutSuccesful()
    }

    override fun putSuccesfull(jsonObject: JSONObject) {
        callback.postOrPutSuccesful()
    }

    override fun postSuccesfull() {
        callback.postOrPutSuccesful()
    }

    override fun postSuccesfull(response: String) {
        callback.postOrPutSuccesful()
    }

    override fun setError(error: Int) {
    }
}