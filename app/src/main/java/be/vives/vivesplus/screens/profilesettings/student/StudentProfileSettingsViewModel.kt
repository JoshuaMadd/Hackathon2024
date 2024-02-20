package be.vives.vivesplus.screens.profilesettings.student

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.vives.vivesplus.R

class StudentProfileSettingsViewModel  (application: Application) : AndroidViewModel(application) {

    private var _name = MutableLiveData<String>()
    val name : LiveData<String>
        get() {return _name}

    private var _kulNumber = MutableLiveData<String?>()
    val kulNumber : LiveData<String?>
        get() {return _kulNumber}

    private var _opleidingsnaam = MutableLiveData<String?>()
    val opleidingsnaam : LiveData<String?>
        get() {return _opleidingsnaam}

    private val _btnGeneralInfoClicked = MutableLiveData<Boolean>()
    val btnGeneralInfoClicked : LiveData<Boolean>
        get() {return _btnGeneralInfoClicked}

    private val _btnFOSClicked = MutableLiveData<Boolean>()
    val btnFOSClicked : LiveData<Boolean>
        get() {return _btnFOSClicked}

    private val _btnCampusClicked = MutableLiveData<Boolean>()
    val btnCampusClicked : LiveData<Boolean>
        get() {return _btnCampusClicked}

    private val _btnTransportClicked = MutableLiveData<Boolean>()
    val btnTransportClicked : LiveData<Boolean>
        get() {return _btnTransportClicked}

    private val _btnJobstudentClicked = MutableLiveData<Boolean>()
    val btnJobstudentClicked : LiveData<Boolean>
        get() {return _btnJobstudentClicked}

    private val _btnDeleteAccountClicked = MutableLiveData<Boolean>()
    val btnDeleteAccountClicked : LiveData<Boolean>
        get() = _btnDeleteAccountClicked

    init {
        _name.value = ""
        _kulNumber.value = ""
        _opleidingsnaam.value = ""
        _btnGeneralInfoClicked.value = false
        _btnFOSClicked.value = false
        _btnCampusClicked.value = false
        _btnTransportClicked.value = false
        _btnJobstudentClicked.value = false
        _btnDeleteAccountClicked.value = false
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setKulNumber(kulNumber: String?) {
        _kulNumber.value = kulNumber
    }

    fun setOpleidingsnaam(opleidingsnaam: String?) {
        if(opleidingsnaam.isNullOrEmpty()){
            _opleidingsnaam.value = getApplication<Application>().resources.getString(R.string.no_education_found)
        }
        else {
            _opleidingsnaam.value = opleidingsnaam
        }
    }

    fun btnGeneralInfoClicked() {
        _btnGeneralInfoClicked.value = true
    }

    fun btnGeneralInfoClickedFinished() {
        _btnGeneralInfoClicked.value = false
    }

    fun btnFOSClicked() {
        _btnFOSClicked.value = true
    }

    fun btnFOSClickedFinished() {
        _btnFOSClicked.value = false
    }

    fun btnCampusClicked() {
        _btnCampusClicked.value = true
    }

    fun btnCampusClickedFinished() {
        _btnCampusClicked.value = false
    }

    fun btnTransportClicked() {
        _btnTransportClicked.value = true
    }

    fun btnTransportClickedFinished() {
        _btnTransportClicked.value = false
    }

    fun btnJobstudentClicked(){
        _btnJobstudentClicked.value = true
    }

    fun btnJobstudentClickedFinish() {
        _btnJobstudentClicked.value = false
    }

    fun btnDeleteAccountClicked(){
        _btnDeleteAccountClicked.value = true
    }

    fun btnDeleteAccountClickedFinished(){
        _btnDeleteAccountClicked.value = false
    }

}