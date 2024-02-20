package be.vives.vivesplus.screens.uurrooster

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.vives.vivesplus.dao.VivesEventDAO
import be.vives.vivesplus.dao.VivesEventDAOCallback
import be.vives.vivesplus.model.VivesEvent
import java.time.LocalDate

class UurroosterViewModel(val context: Context) : ViewModel(), VivesEventDAOCallback {

    private val _vivesEventDao = MutableLiveData<VivesEventDAO>()
    private var _vivesEvents = MutableLiveData<MutableList<VivesEvent>>()
    val vivesEvents : LiveData<MutableList<VivesEvent>>
        get() = _vivesEvents


    private val _errorLogOut = MutableLiveData<Boolean>()
    val errorLogOut : LiveData<Boolean>
        get() = _errorLogOut

    private val _errorForbidden = MutableLiveData<Boolean>()
    val errorForbidden : LiveData<Boolean>
        get() = _errorForbidden

    private val _navigateToWebToestemming = MutableLiveData<Boolean>()
    val navigateToWebToestemming : LiveData<Boolean>
        get() = _navigateToWebToestemming

    init {
        _vivesEventDao.value = VivesEventDAO(context, this)

        getVivesEvents(LocalDate.now())

        _errorLogOut.value = false
        _errorForbidden.value = false
        _navigateToWebToestemming.value = false
    }



    fun navigateToWebToestemmingFinished() {
        _navigateToWebToestemming.value = false
    }

    fun onClickToestemmingGeven() {
        _navigateToWebToestemming.value =true
    }

    override fun vivesEventDataLoaded(eventList: ArrayList<VivesEvent>) {
       _vivesEvents.value = eventList
    }


    fun getVivesEvents(fromDateTime: LocalDate) {
        _vivesEventDao.value!!.get(fromDateTime)
    }


    override fun setError(error: Int) {
        if(error == 401){
            _errorLogOut.value = true
        } else if (error == 403) {
            _errorForbidden.value = true
        }else {
            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
        }
    }
}