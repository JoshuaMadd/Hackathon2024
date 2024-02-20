package be.vives.vivesplus.screens.profilesettings.deleteaccount

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.vives.vivesplus.dao.ProfileDAO
import be.vives.vivesplus.dao.ProfileDAOCallback
import be.vives.vivesplus.model.Profile

class DeleteAccountViewModel (application: Application, context: Context) : AndroidViewModel(application), ProfileDAOCallback {

    private val _dao = MutableLiveData<ProfileDAO>()

    private var _btnDelete = MutableLiveData<Boolean>()
    val btnDelete : LiveData<Boolean>
        get() = _btnDelete


    init {
        _dao.value = ProfileDAO(context, this)
        _btnDelete.value = false
    }

    fun btnDeleteAccountClicked() {
        _btnDelete.value = true
    }

    fun btnDeleteAccountClickedFinished(){
        _btnDelete.value = false
    }


    fun deleteAccount(){
        _dao.value!!.deleteProfile()
    }

    override fun dataLoaded(profile: Profile) {
    }
}