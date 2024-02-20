package be.vives.vivesplus.screens.profilesettings.docent.searchstudies

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.model.Study

class DocentSearchStudiesViewModel(application: Application, var context: Context) : AndroidViewModel(application), StudiesDAOCallback, MemberStudiesDAOCallback {

    private val _daoStudy = MutableLiveData<StudiesDAO>()
    private val _daoMemberStudies = MutableLiveData<MemberStudiesDAO>()

    private var _studies = MutableLiveData<MutableList<Study>>()
    val studies : LiveData<MutableList<Study>>
        get() = _studies

    private var _addStudy = MutableLiveData<Study?>()
    val addStudy : LiveData<Study?>
        get() = _addStudy

    var txtSearchName = MutableLiveData<String>()
    var errorSearchName = MutableLiveData<String?>()

    private val _errorAlreadyHaveStudy = MutableLiveData<Boolean>()
    val errorAlreadyHaveStudy : LiveData<Boolean>
        get() = _errorAlreadyHaveStudy

    init {
        _daoStudy.value = StudiesDAO(context, this)
        _daoMemberStudies.value = MemberStudiesDAO(context, this)

        _errorAlreadyHaveStudy.value = false
    }


    fun onPressEnter(){
        if(txtSearchName.value.isNullOrBlank()){
            errorSearchName.value = context.getString(R.string.fill_in_field)
        } else if (txtSearchName.value!!.length < 5){
            errorSearchName.value = context.getString(R.string.at_least_5_characters)
        } else {
            errorSearchName.value = null
        }

        if(errorSearchName.value.isNullOrBlank()){
            getStudies(txtSearchName.value!!)
        }
    }

    fun clearStudies(){
        _studies.value = ArrayList()
    }

    fun onPressEnterFinished(){
        _studies.value = ArrayList()
    }

    fun getStudies(name : String){
        _daoStudy.value!!.getStudiesByName(name)
    }

    fun onAddStudyclicked(study: Study){
        _daoMemberStudies.value!!.postStudy(study)
        _addStudy.value = study
    }

    fun onAddStudyClickedFinished(){
        _addStudy.value = null
    }


    override fun studyDataLoaded(studies: MutableList<Study>) {
        _studies.value = ArrayList()
        _studies.value = studies
    }

    override fun studyDataLoaded(study: Study) {
    }

    override fun dataLoaded(studies: MutableList<Study>) {

    }

    override fun dataLoaded(study: Study) {
    }

    override fun postSucces() {
    }

    override fun setError(error: Int) {
        if(error == 400){
            _errorAlreadyHaveStudy.value = true
        }
    }
}