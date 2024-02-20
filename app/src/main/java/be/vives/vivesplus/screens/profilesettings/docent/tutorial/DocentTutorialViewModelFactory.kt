package be.vives.vivesplus.screens.profilesettings.docent.tutorial

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DocentTutorialViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DocentTutorialViewModel::class.java)) {
            return DocentTutorialViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}