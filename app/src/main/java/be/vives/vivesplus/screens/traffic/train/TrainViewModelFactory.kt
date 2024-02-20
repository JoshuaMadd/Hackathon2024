package be.vives.vivesplus.screens.traffic.train

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class TrainViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainViewModel::class.java)) {
            return TrainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}