package be.vives.vivesplus.screens.profilesettings.docent

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DocentProfileSettingsViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(DocentProfileSettingsViewModel::class.java)) {
           return DocentProfileSettingsViewModel(application) as T
       }
       throw IllegalArgumentException("Unknown ViewModel class")
   }
}