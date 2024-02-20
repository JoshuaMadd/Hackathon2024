package be.vives.vivesplus.screens.profilesettings.student.campussettings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StudentCampusSettingsViewModel (application: Application) : AndroidViewModel(application)  {

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