package be.vives.vivesplus.screens.profilesettings.docent

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DocentProfileSettingsViewModel (application: Application) : AndroidViewModel(application) {

    private var _name = MutableLiveData<String>()
    val name : LiveData<String>
        get() {return _name}

    private var _kulNumber = MutableLiveData<String?>()
    val kulNumber : LiveData<String?>
        get() {return _kulNumber}

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

    private val _imageProfileClicked = MutableLiveData<Boolean>()
    val imageProfileClicked : LiveData<Boolean>
        get() {return _imageProfileClicked}

    private val _turnProfileImgClicked = MutableLiveData<Boolean>()
    val turnProfileImgClicked : LiveData<Boolean>
        get() {return _turnProfileImgClicked}

    private val _btnDeleteAccountClicked = MutableLiveData<Boolean>()
    val btnDeleteAccountClicked : LiveData<Boolean>
        get() = _btnDeleteAccountClicked

    private val _btnEducationsClicked = MutableLiveData<Boolean>()
    val btnEducationsClicked : LiveData<Boolean>
        get() = _btnEducationsClicked

    init {
        _name.value = ""
        _kulNumber.value = ""
        _btnGeneralInfoClicked.value = false
        _btnFOSClicked.value = false
        _btnCampusClicked.value = false
        _btnTransportClicked.value = false
        _imageProfileClicked.value = false
        _turnProfileImgClicked.value=false
        _btnDeleteAccountClicked.value = false
        _btnEducationsClicked.value = false
    }

    fun setName(name: String) {
        _name.value = name;
    }

    fun setKulNumber(kulNumber: String?) {
        _kulNumber.value = kulNumber;
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

    fun btnImageProfileClicked() {
        _imageProfileClicked.value = true
    }

    fun btnImageProfileClickedFinished() {
        _imageProfileClicked.value = false
    }
    fun btnTurnProfileImgClicked() {
        _turnProfileImgClicked.value = true
    }

    fun btnTurnProfileImgClickedFinished() {
        _turnProfileImgClicked.value = false
    }

    fun btnDeleteAccountClicked() {
        _btnDeleteAccountClicked.value = true
    }

    fun btnDeleteAccountClickedFinished(){
        _btnDeleteAccountClicked.value = false
    }

    fun btnEducationsClicked() {
        _btnEducationsClicked.value = true
    }
    fun btnEducationsClickedFinished(){
        _btnEducationsClicked.value = false
    }
}