package be.vives.vivesplus.dao

import android.content.Context
import be.vives.vivesplus.R
import be.vives.vivesplus.model.StudyProposal
import be.vives.vivesplus.util.WebService
import be.vives.vivesplus.util.WebServiceCallback
import be.vives.vivesplus.util.WebServicePutCallback
import org.json.JSONArray
import org.json.JSONObject
import be.vives.vivesplus.enum.Status as Status

class StudyProposalsDAO(val context: Context, private val callback: StudyProposalsDAOCallback) : WebServiceCallback, WebServicePutCallback {
    private val filename = "studyproposals.json"
    private val url = "${context.getString(R.string.vivesplus_api_link)}/members/me/studyproposals"

    fun getProposals(){
        WebService(context, filename, this).getJsonArrayFromUrl(url)
    }

    fun put(id: Int, status : String){
        WebService(context, filename, this).putJsonObjectToUrl("$url/$id/$status",
            mutableListOf(),
            this)
    }

    override fun dataLoaded(jsonArray: JSONArray) {
        val list : ArrayList<StudyProposal> = ArrayList()
        for (i in 0 until jsonArray.length()){
            try {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getInt("id")
                val name = obj.getString("name")

                val status = getStatus(obj.getString(("status")))

                list.add(StudyProposal(id, name, status))

            } catch (ex: Exception){
                print(ex.toString())
            }
        }
        callback.proposalDataLoaded(list)
    }

    override fun dataLoaded(jsonObject: JSONObject) {
        try{
            val id = jsonObject.getInt("id")
            val name = jsonObject.getString("name")
            val status = getStatus(jsonObject.getString(("status")))

            callback.dataLoaded(StudyProposal(id, name, status))

        }catch (ex: Exception){
            print(ex.toString())
        }
    }

    override fun setError(error: Int) {}

    private fun getStatus(statusString: String) : Status{
        var status : Status = Status.PROPOSAL
        when (statusString) {
            "PROPOSAL" -> {
                status = Status.PROPOSAL
            }
            "DECLINED" -> {
                status = Status.DECLINED
            }
            "ACCEPTED" -> {
                status = Status.ACCEPTED
            }
        }

        return status
    }

    override fun putSuccesfull() {
        callback.putSucces()
    }

    override fun putSuccesfull(jsonObject: JSONObject) {
        callback.putSucces()
    }
}