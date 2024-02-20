package be.vives.vivesplus.screens.profilesettings.student.jobstudent

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class JobstudentViewModelFactory (private val application: Application) : ViewModelProvider.Factory  {
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobstudentViewModel::class.java)) {
            return JobstudentViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}