package be.vives.vivesplus.screens.profilesettings.docent.studies

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DocentEducationsViewModelFactory (private val application: Application, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DocentEducationsViewModel::class.java)) {
            return DocentEducationsViewModel(application, context) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}