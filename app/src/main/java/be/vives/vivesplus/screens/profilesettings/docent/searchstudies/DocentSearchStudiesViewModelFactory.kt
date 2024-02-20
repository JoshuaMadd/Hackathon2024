package be.vives.vivesplus.screens.profilesettings.docent.searchstudies

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DocentSearchStudiesViewModelFactory(private val application: Application, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DocentSearchStudiesViewModel::class.java)){
            return DocentSearchStudiesViewModel(application, context) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}