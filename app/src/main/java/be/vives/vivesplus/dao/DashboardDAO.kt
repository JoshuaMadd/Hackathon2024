package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.dashboardcard.DashboardCard
import be.vives.vivesplus.model.dashboardcard.DashboardCardDetail
import be.vives.vivesplus.util.DashboardMapper
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import com.android.volley.VolleyError
import org.json.JSONArray
import org.json.JSONObject

class DashboardDAO(val context: Context, private val callback: DashboardDAOCallback): WebServiceCallback {

    private val url = "${context.getString(R.string.vivesplus_base_link)}api/dashboard"
    private val fileName = "dashboard.json"

    fun getAll() {
        WebService(context, fileName, this).getJsonObjectFromUrl(url)
    }

    override fun setError(error: Int) {
        callback.setError(error)
    }

    override fun dataLoaded(jsonArray: JSONArray) {

    }

    override fun dataLoaded(jsonObject: JSONObject) {

        val dashboardMapper = DashboardMapper()

        val dashboardCardsList : ArrayList<DashboardCard> = ArrayList()
        try {
            val dashboardCardsarrayjson = jsonObject.getJSONArray("dashboardCards")
            for(i in 0 until dashboardCardsarrayjson.length()) {
                val dashboardCardJSON = dashboardCardsarrayjson.getJSONObject(i)

                // get type
                val dashboardTypeJson = dashboardCardJSON.getString("type")
                val dashboardType = dashboardMapper.mapJsonToDashboardType(dashboardTypeJson)

                // get action
                val action = dashboardCardJSON.getString("action")

                // get detail
                val dashboardCardDetail: DashboardCardDetail? =
                if(dashboardType != null) {
                    dashboardMapper.mapTypeToDashboardCardDetail(
                        dashboardType, dashboardCardJSON.getJSONObject("detail"))
                }
                else {
                    null
                }

                // make the object with type and detail
                val dashboardCard : DashboardCard? = if (dashboardCardDetail != null  && dashboardType != null) {
                    DashboardCard(dashboardType, action, dashboardCardDetail)
                } else {
                    null
                }

                if (dashboardCard != null) {
                    dashboardCardsList.add(dashboardCard)
                }
            }
            callback.dashboardDataLoaded(dashboardCardsList)
        }catch (ve: VolleyError) {
            setError(ve.networkResponse.statusCode)
        }catch (ex: Exception) {
            println("DashboardDAO" + ex.toString())
        }
    }
}