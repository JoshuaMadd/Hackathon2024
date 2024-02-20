package be.vives.vivesplus.screens.profilesettings.docent.studyproposals

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DocentStudyProposalViewModelFactory(private val application: Application, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DocentStudyProposalViewModel::class.java)){
            return DocentStudyProposalViewModel(application, context) as T
        }else{
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}