package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Study
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import org.json.JSONArray
import org.json.JSONObject

class StudiesDAO(val context: Context, private val callback: StudiesDAOCallback): WebServiceCallback {
    private val filename = "studies.json"
    private val url = "${context.getString(R.string.vivesplus_api_link)}/studies"

    fun getStudies(){
        WebService(context, filename, this).getJsonArrayFromUrl(url)
    }

    fun getStudiesByName(name: String){
        val studiesUrl = "${url}?name=${name}&showMyStudies=false"
        WebService(context, filename, this).getJsonArrayFromUrl(studiesUrl)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list : ArrayList<Study> = ArrayList()
        for(i in 0 until jsonArray.length()){
            try{
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getInt("id")
                val name = obj.getString("name")

                list.add(Study(id, name))

            } catch (ex: Exception) {
                print(ex.toString())
            }
        }
        callback.studyDataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {
        try {
            val id = jsonObject.getInt("id")
            val name = jsonObject.getString("name")

            callback.studyDataLoaded(Study(id, name))
        } catch (ex: Exception) {
            print(ex.toString())
        }
    }
}