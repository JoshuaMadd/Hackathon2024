package be.vives.vivesplus.screens.identity

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class PresenceQrViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PresenceQrViewModel::class.java)) {
            return PresenceQrViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
