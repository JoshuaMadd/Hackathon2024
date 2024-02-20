package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Category
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import org.json.JSONArray
import org.json.JSONObject

class CategoryDAO(val context: Context, val callback: CategoryDAOCallback): WebServiceCallback {

    val fileName = "categories.json"
    val url =  "${context.getString(R.string.vivesplus_api_link)}/notices/categories"

    fun getCategory(){
        WebService(context, fileName, this).getJsonArrayFromUrl(url)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list: ArrayList<Category> = ArrayList()
        try{
            for(i in 0 until jsonArray.length()){
                val obj  = jsonArray.getJSONObject(i)
                val code = obj.getString("code")
                val description = obj.getString("description")
                val icon = obj.getString("icon")

                list.add(Category(code,description,icon))
            }
        }catch (ex: Exception) {
            println(ex.toString())
        }
        callback.dataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {}
}