package be.vives.vivesplus.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NoInternetViewModel (application: Application) : AndroidViewModel(application)  {

    private var _btnTryAgain: MutableLiveData<Boolean> = MutableLiveData()
    val btnTryAgain: LiveData<Boolean>
        get() { return _btnTryAgain }

    fun btnTryAgainClicked() { _btnTryAgain.value = true }

    fun btnTryAgainFinished() {
        _btnTryAgain.value = false
    }
}