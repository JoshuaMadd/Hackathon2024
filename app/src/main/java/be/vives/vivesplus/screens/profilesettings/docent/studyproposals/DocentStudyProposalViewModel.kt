package be.vives.vivesplus.screens.profilesettings.docent.studyproposals

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.vives.vivesplus.dao.StudyProposalsDAO
import be.vives.vivesplus.dao.StudyProposalsDAOCallback
import be.vives.vivesplus.enum.Status
import be.vives.vivesplus.model.StudyProposal

class DocentStudyProposalViewModel (application: Application, context: Context) : AndroidViewModel(application), StudyProposalsDAOCallback {

    private val _daoProps = MutableLiveData<StudyProposalsDAO>()

    //proposals ophalen
    private var _proposals = MutableLiveData<MutableList<StudyProposal>>()
    val proposals : LiveData<MutableList<StudyProposal>>
        get() = _proposals

    private var _deleteProposal = MutableLiveData<StudyProposal?>()
    val deleteProposal : LiveData<StudyProposal?>
        get() = _deleteProposal

    private var _addProposal = MutableLiveData<StudyProposal?>()
    val addProposal : LiveData<StudyProposal?>
        get() = _addProposal

    init {
        _daoProps.value = StudyProposalsDAO(context, this)
        _proposals.value = ArrayList()
    }

    fun onDeleteStudyProposalClicked(studyProposal: StudyProposal){
        _daoProps.value!!.put(studyProposal.id, Status.DECLINED.toString())
        _deleteProposal.value = studyProposal
    }
    fun onDeleteStudyProposalFinished(){
        _deleteProposal.value = null
    }

    fun onAddStudyProposalClicked(studyProposal: StudyProposal){
        _daoProps.value!!.put(studyProposal.id, Status.ACCEPTED.toString())
        _addProposal.value = studyProposal
    }

    fun onAddStudyProposalFinished(){
        _addProposal.value = null
    }


    override fun proposalDataLoaded(studyProposals: MutableList<StudyProposal>) {
    }

    override fun dataLoaded(studyProposal: StudyProposal) {
    }

    override fun putSucces() {
    }

}