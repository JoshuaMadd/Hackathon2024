package be.vives.vivesplus.screens.profilesettings.student.fielofstudysettings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class StudentFieldOfStudySettingsViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentFieldOfStudySettingsViewModel::class.java)) {
            return StudentFieldOfStudySettingsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}