package be.vives.vivesplus.screens.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class NoInternetViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoInternetViewModel::class.java)) {
            return NoInternetViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}