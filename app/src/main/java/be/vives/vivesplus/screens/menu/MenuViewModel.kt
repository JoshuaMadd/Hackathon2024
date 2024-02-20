package be.vives.vivesplus.screens.menu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MenuViewModel (application: Application) : AndroidViewModel(application) {

    private var _name = MutableLiveData<String>()
    val name : LiveData<String>
        get() {return _name}

    private var _kulNumber = MutableLiveData<String?>()
    val kulNumber : LiveData<String?>
        get() {return _kulNumber}

    private var _versionNumber = MutableLiveData<String>()
    val versionNumber : LiveData<String>
        get() {return _versionNumber}

    private val _btnLogoutClicked = MutableLiveData<Boolean>()
    val btnLogoutClicked : LiveData<Boolean>
        get() {return _btnLogoutClicked}

    private val _btnSettingsClicked = MutableLiveData<Boolean>()
    val btnSettingsClicked : LiveData<Boolean>
        get() {return _btnSettingsClicked}

    private val _navigateToTransport = MutableLiveData<Boolean>()
    val navigateToTransport : LiveData<Boolean>
        get() {return _navigateToTransport}

    private val _navigateToRestaurants = MutableLiveData<Boolean>()
    val navigateToRestaurants : LiveData<Boolean>
        get() {return _navigateToRestaurants}

    private val _navigateToSupport = MutableLiveData<Boolean>()
    val navigateToSupport : LiveData<Boolean>
        get() {return _navigateToSupport}

    private val _navigateToWebshop = MutableLiveData<Boolean>()
    val navigateToWebshop : LiveData<Boolean>
        get() {return _navigateToWebshop}

    private val _navigateToMyAbsences = MutableLiveData<Boolean>()
    val navigateToMyAbsences : LiveData<Boolean>
        get() {return _navigateToMyAbsences}

    private val _navigateToSearchDocent = MutableLiveData<Boolean>()
    val navigateToSearchDocent : LiveData<Boolean>
        get() {return _navigateToSearchDocent}

    private val _navigateToScanner = MutableLiveData<Boolean>()
    val navigateToScanner : LiveData<Boolean>
        get() {return _navigateToScanner}

    private val _navigateToPresence = MutableLiveData<Boolean>()
    val navigateToPresence : LiveData<Boolean>
        get() {return _navigateToPresence}

    init {
        _name.value = ""
        _kulNumber.value = ""
        _versionNumber.value = ""
        _btnLogoutClicked.value = false
        _navigateToTransport.value = false
        _navigateToRestaurants.value = false
        _navigateToMyAbsences.value = false
        _navigateToSearchDocent.value = false
        _navigateToSupport.value = false
        _navigateToWebshop.value = false
        _navigateToScanner.value = false
        _navigateToPresence.value = false
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setKulNumber(kulNumber: String?) {
        _kulNumber.value = kulNumber
    }

    fun setVersionNumber(version: String) {
        _versionNumber.value = version
    }

    fun btnLogoutClicked() {
        _btnLogoutClicked.value = true
    }

    fun btnSettingsClicked() {
        _btnSettingsClicked.value = true
    }

    fun btnLogoutClickedFinished() {
        _btnLogoutClicked.value = false
    }

    fun btnSettingsClickedFinished() {
        _btnSettingsClicked.value = false
    }

    fun btnTransportClicked(){
        _navigateToTransport.value = true
    }

    fun btnTransportFinished(){
        _navigateToTransport.value = false
    }

    fun btnMyAbsencesClicked(){
        _navigateToMyAbsences.value = true
    }

    fun btnMyAbsencesFinished(){
        _navigateToMyAbsences.value = false
    }

    fun btnRestaurantsClicked(){
        _navigateToRestaurants.value = true
    }

    fun btnRestaurantsFinished(){
        _navigateToRestaurants.value = false
    }

    fun btnsearchdocentClicked(){
        _navigateToSearchDocent.value = true
    }

    fun btnsearchdocentFinished(){
        _navigateToSearchDocent.value = false
    }

    fun btnSupportClicked(){
        _navigateToSupport.value = true
    }

    fun btnSupportFinished(){
        _navigateToSupport.value = false
    }

    fun btnWebshopClicked(){
        _navigateToWebshop.value = true
    }

    fun btnWebshopFinished(){
        _navigateToWebshop.value = false
    }

    fun btnScannerClicked(){
        _navigateToScanner.value = true
    }

    fun btnPresenceClicked(){
        _navigateToPresence.value = true
    }

    fun btnScannerFinished(){
        _navigateToScanner.value = false
    }

    fun btnPresenceFinished(){
        _navigateToPresence.value = false
    }
}
