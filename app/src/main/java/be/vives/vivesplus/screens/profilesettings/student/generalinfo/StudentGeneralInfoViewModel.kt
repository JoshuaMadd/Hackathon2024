package be.vives.vivesplus.screens.profilesettings.student.generalinfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class StudentGeneralInfoViewModel (application: Application) : AndroidViewModel(application)  {

    private var _name = MutableLiveData<String>()
    val name : LiveData<String>
        get() {return _name}

    private var _email = MutableLiveData<String>()
    val email : LiveData<String>
        get() {return _email}

    private var _education = MutableLiveData<String>()
    val education : LiveData<String>
        get() {return _education}

    init{
        _name.value = ""
        _email.value = ""
        _education.value = ""
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setEducation(education: String) {
        _education.value = education
    }
}