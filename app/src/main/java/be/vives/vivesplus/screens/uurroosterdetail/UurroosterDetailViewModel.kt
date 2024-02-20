package be.vives.vivesplus.screens.uurroosterdetail

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.enum.EventType
import be.vives.vivesplus.model.*
import be.vives.vivesplus.screens.identity.PresenceQRDAO
import be.vives.vivesplus.screens.identity.PresenceQrDAOCallback
import be.vives.vivesplus.util.DateTimeHelper
import java.time.LocalDateTime

class UurroosterDetailViewModel(application: Application, val context: Context) : AndroidViewModel(application), EventDetailDAOCallback, AdminAbsencesDAOCallback {

    private val _dao = MutableLiveData<EventDetailDAO>()
    private val _daoAbsence = MutableLiveData<AdminAbsencesDAO>()
    private val _daoPresence = MutableLiveData<PresenceQRDAO>()

    private val _presenceDetail = MutableLiveData<CodeQR>()

    private var _eventDetail = MutableLiveData<EventDetail>()

    private var _bindingEvent = MutableLiveData<BindingEvent>()
    val bindingEvent : LiveData<BindingEvent>
        get() = _bindingEvent

    private var _errorLogOut = MutableLiveData<Boolean>()
    val errorLogOut : LiveData<Boolean>
        get() = _errorLogOut

    private var _errorForbidden = MutableLiveData<Boolean>()
    val errorForbidden : LiveData<Boolean>
        get() = _errorForbidden

    private var _btnAbsent = MutableLiveData<Boolean>()
    val btnAbsent : LiveData<Boolean>
        get() = _btnAbsent

    private var _btnSaveAbsence = MutableLiveData<Boolean>()
    val btnSaveAbsence : LiveData<Boolean>
        get() = _btnSaveAbsence

    private var _btnCancelAbsence = MutableLiveData<Boolean>()
    val btnCancelAbsence : LiveData<Boolean>
        get() = _btnCancelAbsence

    var txtRemarkMakeAbsence = MutableLiveData<String>()

    private var _btnManagaAbsence = MutableLiveData<Boolean>()
    val btnManageAbsence : LiveData<Boolean>
        get() = _btnManagaAbsence

    private var _makeToastAbsenceMade = MutableLiveData<Boolean>()
    val makeToastAbsence : LiveData<Boolean>
        get() = _makeToastAbsenceMade

    private var _navigateToQr = MutableLiveData<Boolean>()
    val navigateToQr : LiveData<Boolean>
        get() = _navigateToQr

    private var _navigateToScanner = MutableLiveData<Boolean>()
    val navigateToScanner : LiveData<Boolean>
        get() = _navigateToScanner

    init {
        _dao.value = EventDetailDAO(context, this)
        _daoAbsence.value = AdminAbsencesDAO(context, this)

        _errorLogOut.value = false
        _errorForbidden.value = false
        _btnAbsent.value = false
        _btnSaveAbsence.value = false
        _btnCancelAbsence.value = false
        _btnManagaAbsence.value = false
        _makeToastAbsenceMade.value = false
        txtRemarkMakeAbsence.value = ""
        _navigateToQr.value = false
        _navigateToScanner.value = false
    }

    fun getEventDetail(type: String, id: String){
        _dao.value!!.getAnEventDetail(type, id)
    }

    fun makeAbsence(){
        _daoAbsence.value!!.post(
            _eventDetail.value!!.startDateTime,
            _eventDetail.value!!.endDateTime,
            txtRemarkMakeAbsence.value!!
        )

    }

    fun btnSaveAbsenceClicked(){
        _btnSaveAbsence.value = true
        makeAbsence()
    }

    fun btnSaveAbsenceClickedFinished(){
       _btnSaveAbsence.value = false
    }

    fun btnCancelAbsenceClicked(){
        _btnCancelAbsence.value = true
    }
    fun btnCancelAbsenceClickedFinished(){
        _btnCancelAbsence.value = false
    }
    fun btnAbsentClicked(){
        _btnAbsent.value = true
    }
    fun btnAbsentClickedFinished(){
        _btnAbsent.value = false
    }

    fun btnManageAbsenceClicked(){
        _btnManagaAbsence.value = true
    }
    fun btnManageAbsenceClickedFinished(){
        _btnManagaAbsence.value = false
    }

    fun btnPresencesClicked(){
        _navigateToQr.value = true
    }
    fun btnPresencesFinished(){
        _navigateToQr.value = false
    }

    fun makeToastAbsenceFinished(){
        _makeToastAbsenceMade.value = false
    }

    override fun eventDetailDataLoaded(eventDetail: EventDetail) {
        _eventDetail.value = eventDetail
        _bindingEvent.value = BindingEvent(_eventDetail.value!!)
    }

    override fun setError(error: Int) {
        if(error == 401){
            _errorLogOut.value = true
        }
        if(error == 403){
            _errorForbidden.value = true
        }
    }

    override fun dataLoaded(absences: ArrayList<AdminAbsence>) {
    }

    override fun postOrPutSuccesful() {
        _makeToastAbsenceMade.value = true
    }




}

class BindingEvent(var eventDetail: EventDetail){

    var id : String = eventDetail.id
    var startDateTimeString : String = eventDetail.startDateTime
    var endDateTime : LocalDateTime = DateTimeHelper().formatStringToDateTime(eventDetail.endDateTime)
    var startDate : String = DateTimeHelper().formatStringToLongNameDateString(eventDetail.startDateTime)
    var startTime : String = DateTimeHelper().formatStringToHourMinutesString(eventDetail.startDateTime)
    var endDate : String = DateTimeHelper().formatStringToLongNameDateString(eventDetail.endDateTime)
    var endTime : String = DateTimeHelper().formatStringToHourMinutesString(eventDetail.endDateTime)
    var description : String = eventDetail.description
    var allDay : Boolean = eventDetail.allDay
    var coordinates : Coordinates?
    var locations : String?
    var teachers : String?
    var type : EventType = eventDetail.type
    var ectsCode : String
    var groupInfo : String


    init{
        this.locations = eventDetail.locations.joinToString("\n") { it ->
            if (it.description != "null") {
                if (it.buildingName != "null") {
                    "${it.description}\n${it.buildingName}" +
                            if (eventDetail.locations.lastIndex == eventDetail.locations.indexOf(it)) {
                                ""
                            } else {
                                "\n"
                            }
                } else {
                    "${it.description}" +
                            if (eventDetail.locations.lastIndex == eventDetail.locations.indexOf(it)) {
                                ""
                            } else {
                                "\n"
                            }
                }
            } else {
                if (it.buildingName != "null") {
                    "${it.buildingName}" +
                            if (eventDetail.locations.lastIndex == eventDetail.locations.indexOf(it)) {
                                ""
                            } else {
                                "\n"
                            }
                }else{
                    ""
                }
            }
        }

        if(startDate == endDate){
            endDate = ""
        }

        if(allDay){
            startTime = ""
            endTime = ""
        }

        if(eventDetail.locations.isEmpty()){
            coordinates = null
        } else {
            coordinates = Coordinates(
                eventDetail.locations.first().coordinates!!.latitude,
                eventDetail.locations.first().coordinates!!.longitude
            )
        }

        when(eventDetail.type){
            EventType.COURSE -> {
                val courseDetail : CourseEventDetail = eventDetail as CourseEventDetail
                this.teachers = courseDetail.teachers?.joinToString("\n") { it.name }
                this.ectsCode = courseDetail.ectsCode
                this.groupInfo = courseDetail.groupInfo

            }
            EventType.ACADEMIC -> {
                this.teachers = ""
                this.ectsCode = ""
                this.groupInfo = ""
            }
            EventType.CANCELLED -> {
                val cancelledDetail : CourseEventDetail = eventDetail as CourseEventDetail
                this.teachers = ""
                this.ectsCode = cancelledDetail.ectsCode
                this.groupInfo = cancelledDetail.groupInfo
            }
        }
    }
}