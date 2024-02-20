package be.vives.vivesplus.screens.profilesettings.docent.fielofstudysettings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DocentFieldOfStudySettingsViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DocentFieldOfStudySettingsViewModel::class.java)) {
            return DocentFieldOfStudySettingsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}