package be.vives.vivesplus.screens.profilesettings.deleteaccount

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DeleteAccountViewModelFactory (private val application:Application, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DeleteAccountViewModel::class.java)) {
            return DeleteAccountViewModel(application, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}