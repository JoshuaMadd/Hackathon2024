package be.vives.vivesplus.screens.profilesettings.student.transportsettings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StudentTransportSettingsViewModel (application: Application) : AndroidViewModel(application)  {
    private val _btnSaveClicked = MutableLiveData<Boolean>()
    val btnSaveClicked : LiveData<Boolean>
        get() {return _btnSaveClicked}

    init {
        _btnSaveClicked.value = false
    }

    fun btnSaveClicked() {
        _btnSaveClicked.value = true
    }

    fun btnSaveClickedFinished() {
        _btnSaveClicked.value = false
    }
}