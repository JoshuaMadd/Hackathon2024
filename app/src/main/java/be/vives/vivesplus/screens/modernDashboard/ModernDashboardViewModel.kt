package be.vives.vivesplus.screens.modernDashboard

import android.app.Application
import android.view.Menu
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ModernDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val _navigateToMessages = MutableLiveData<Boolean>()
    val navigateToMessages : LiveData<Boolean>
        get() {return _navigateToMessages}

    private val _navigateToQr = MutableLiveData<Boolean>()
    val navigateToQr : LiveData<Boolean>
        get() {return _navigateToQr}

    var tokenSend : Boolean
    var counter = MutableLiveData<Int>()
    var menu = MutableLiveData<Menu?>()

    init {
        _navigateToQr.value = false
        tokenSend = false
        counter.value = 0
        menu.value = null
    }


    private val _temperatuur = MutableLiveData<String>()
    val temperatuur : LiveData<String>
        get() {return _temperatuur}

    init {
        _temperatuur.value = ""
    }

    fun setTemperatuur (temperatuur: String) {
        _temperatuur.value = temperatuur
    }

    fun btnMessagesClicked(){

    }

    fun btnQrClicked(){
        _navigateToQr.value = true
    }

    fun btnQrFinished(){
        _navigateToQr.value = false
    }
}