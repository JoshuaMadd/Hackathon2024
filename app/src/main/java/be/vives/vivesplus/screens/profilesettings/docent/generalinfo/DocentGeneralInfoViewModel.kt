package be.vives.vivesplus.screens.profilesettings.docent.generalinfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DocentGeneralInfoViewModel(application: Application) : AndroidViewModel(application)  {

    private var _name = MutableLiveData<String>()
    val name : LiveData<String>
        get() {return _name}

    private var _firstName = MutableLiveData<String>()
    val firstname : LiveData<String>
        get() {return _firstName}

    private var _email = MutableLiveData<String>()
    val email : LiveData<String>
        get() {return _email}

    private var _phone = MutableLiveData<String>()
    val phone : LiveData<String>
        get() {return _phone}

    private var _jobTitle = MutableLiveData<String>()
    val jobTitle : LiveData<String>
        get() {return _jobTitle}

    private var _bachelor = MutableLiveData<Boolean>()
    val bachelor : LiveData<Boolean>
        get() {return _bachelor}

    private var _graduate = MutableLiveData<Boolean>()
    val graduate : LiveData<Boolean>
        get() {return _graduate}

    private val _save = MutableLiveData<Boolean>()
    val save : LiveData<Boolean>
        get() {return _save}

    init {
        _name.value = ""
        _firstName.value = ""
        _email.value = ""
        _phone.value = ""
        _jobTitle.value = ""
        _bachelor.value = false
        _graduate.value = false
        _save.value = false
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setFirstName(firstName: String) {
        _firstName.value = firstName
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPhone(phone: String) {
        _phone.value = phone
    }

    fun setJobTitle(title: String) {
        _jobTitle.value = title
    }

    fun setBachelor() {
        _bachelor.value = true
    }

    fun setGraduate() {
        _graduate.value = true
    }

    fun btnSaveClicked(){
        _save.value = true
    }

    fun btnSaveFinished(){
        _save.value = false
    }
}