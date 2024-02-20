package be.vives.vivesplus.screens.uurroosterdetail

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UurroosterDetailViewModelFactory(private val application: Application, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UurroosterDetailViewModel::class.java)){
            return UurroosterDetailViewModel(application, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}