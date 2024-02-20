package be.vives.vivesplus.screens.modernDashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ModernDashboardViewmodelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModernDashboardViewModel ::class.java)) {
            return ModernDashboardViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}