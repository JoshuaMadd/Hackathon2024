package be.vives.vivesplus.screens.traffic.bus

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class BusViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BusViewModel::class.java)) {
            return BusViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}