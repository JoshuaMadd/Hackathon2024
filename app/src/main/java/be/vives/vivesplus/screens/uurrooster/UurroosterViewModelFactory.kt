package be.vives.vivesplus.screens.uurrooster

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UurroosterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UurroosterViewModel::class.java)) {
            return UurroosterViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}