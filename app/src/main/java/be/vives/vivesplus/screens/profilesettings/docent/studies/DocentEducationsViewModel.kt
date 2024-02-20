package be.vives.vivesplus.screens.profilesettings.docent.studies

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.model.Study
import be.vives.vivesplus.model.StudyProposal

class DocentEducationsViewModel (application: Application, context: Context) : AndroidViewModel(application), MemberStudiesDAOCallback, StudyProposalsDAOCallback{

    private val _daoStudy = MutableLiveData<MemberStudiesDAO>()
    private val _daoProposals = MutableLiveData<StudyProposalsDAO>()

    private var _studies = MutableLiveData<MutableList<Study>>()
    val studies : LiveData<MutableList<Study>>
        get() = _studies

    private var _deleteStudy = MutableLiveData<Study?>()
    val deleteStudy : LiveData<Study?>
        get() = _deleteStudy

    private var _propCount = MutableLiveData<Int>()
    val propCount : LiveData<Int>
        get() = _propCount

    private var _proposals = MutableLiveData<MutableList<StudyProposal>>()
    val proposals : LiveData<MutableList<StudyProposal>>
        get() = _proposals


    private var _fabClicked = MutableLiveData<Boolean>()
    val fabClicked : LiveData<Boolean>
        get() = _fabClicked

    private var _fabSearchClicked = MutableLiveData<Boolean>()
    val fabSearchClicked : LiveData<Boolean>
        get() = _fabSearchClicked

    private var _fabManageProposals = MutableLiveData<Boolean>()
    val fabManageProposalsClicked : LiveData<Boolean>
        get() = _fabManageProposals

    private var _isManagePropsClickable = MutableLiveData<Boolean>()
    val isManagePropsClickable : LiveData<Boolean>
        get() = _isManagePropsClickable

    init {
        _daoStudy.value = MemberStudiesDAO(context, this)
        _daoProposals.value = StudyProposalsDAO(context, this)

        getStudies()
        getStudyProposals()

        _fabClicked.value = false
        _fabSearchClicked.value = false
        _fabSearchClicked.value = false
    }

    fun getStudies(){
        _daoStudy.value!!.getStudies()
    }

    fun getStudyProposals(){
        _daoProposals.value!!.getProposals()
    }


    fun onFabClicked(){
        _fabClicked.value = true

    }
    fun onFabClickedFinished(){
        _fabClicked.value = false
    }

    fun onFabSearchClicked(){
        _fabSearchClicked.value = true
    }
    fun onFabSearchClickedFinished(){
        _fabSearchClicked.value = false
    }

    fun onFabManageProposalsClicked(){
        _fabManageProposals.value = true
    }
    fun onFabManageProposalsClickedFinished(){
        _fabManageProposals.value = false
    }

    fun setManagePropsClickableTrue(){
        _isManagePropsClickable.value = true
    }
    fun setManagePropsClickableFalse(){
        _isManagePropsClickable.value = false
    }

    fun onDeleteStudyClicked(study: Study){
        _daoStudy.value!!.deleteStudy(study.id)

        _deleteStudy.value = study
    }

    fun onDeleteStudyClickedFinished(){
        _deleteStudy.value = null
    }


    override fun dataLoaded(studies: MutableList<Study>) {
        _studies.value = studies
    }

    override fun dataLoaded(study: Study) {
    }

    override fun postSucces() {

    }

    override fun proposalDataLoaded(studyProposals: MutableList<StudyProposal>) {
        _proposals.value = studyProposals
        _propCount.value = studyProposals.count()
        _isManagePropsClickable.value = _propCount.value != 0
    }

    override fun dataLoaded(studyProposal: StudyProposal) {
    }

    override fun putSucces() {
    }


}