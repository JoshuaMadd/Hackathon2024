package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Study
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import be.vives.vivesplus.util.WebservicePostCallback
import com.android.volley.VolleyError
import org.json.JSONArray
import org.json.JSONObject

class MemberStudiesDAO(val context: Context, private val callback: MemberStudiesDAOCallback): WebServiceCallback, WebservicePostCallback {
    private val filename = "memberstudies.json"
    private val url = "${context.getString(R.string.vivesplus_api_link)}/members/me/studies"

    fun getStudies(){
        WebService(context, filename, this).getJsonArrayFromUrl(url)
    }

    fun postStudy(study: Study){
        val postUrl = "$url/${study.id}"
        try{
            WebService(context, filename, this).postJsonObjectToUrl(postUrl, mutableListOf(), this@MemberStudiesDAO)
        } catch (ve : VolleyError){
            callback.setError(ve.networkResponse.statusCode)
        } catch (ex: Exception){
            println(ex)
        }
    }


    fun deleteStudy(id: Int){
        try{
            WebService(context, filename, this).deleteJsonObjectToUrl("$url/$id")
        }catch (ex: Exception){
            println(ex)
        }
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list : ArrayList<Study> = ArrayList()
        for (i in 0 until jsonArray.length()){
            try {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getInt("id")
                val name = obj.getString("name")

                list.add(Study(id, name))
            } catch (ex: Exception){
                print(ex.toString())
            }
        }
        callback.dataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {
        try{
            val id = jsonObject.getInt("id")
            val name = jsonObject.getString("name")

            callback.dataLoaded(Study(id, name))
        } catch (ex: Exception){
            print(ex.toString())
        }
    }

    override fun postSuccesfull() {
        callback.postSucces()
    }

    override fun postSuccesfull(response: String) {
        callback.postSucces()
    }

    override fun setErrorPost(error: Int) {
        callback.setError(error)
    }
}