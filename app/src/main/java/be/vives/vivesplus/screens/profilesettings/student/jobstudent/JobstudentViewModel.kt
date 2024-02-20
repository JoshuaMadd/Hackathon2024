package be.vives.vivesplus.screens.profilesettings.student.jobstudent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class JobstudentViewModel (application: Application) : AndroidViewModel(application) {

    private var _jobstudentBoolean = MutableLiveData<Boolean>()
    val jobstudentBoolean : LiveData<Boolean>
        get() { return _jobstudentBoolean}

    private val _save = MutableLiveData<Boolean>()
    val save : LiveData<Boolean>
        get() {return _save}

    init {
        _jobstudentBoolean.value = false
        _save.value = false
    }

    fun setJobstudent() {
        _jobstudentBoolean.value = true
    }

    fun btnSaveClicked(){
        _save.value = true
    }

    fun btnSaveFinished(){
        _save.value = false
    }
}