package be.vives.vivesplus.screens.parkeerticket

import android.app.Application
import android.graphics.Bitmap
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import be.vives.vivesplus.dao.ParkingTicketDAO
import be.vives.vivesplus.dao.ParkingTicketDAOCallback
import be.vives.vivesplus.model.ParkeerTicket
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ParkingTicketViewModel (application: Application) : AndroidViewModel(application), ParkingTicketDAOCallback {
    var networkError = MutableLiveData<Boolean>()
    var succes = MutableLiveData<Boolean>()
    var otherError = MutableLiveData<Boolean>()
    var APITicket = ParkeerTicket("","")

    init {
        networkError.value = false
        succes.value = false
        otherError.value = false
        call()
    }

    fun call(){
        ParkingTicketDAO(getApplication(), this).getTicketCode()
    }

    override fun dataLoaded(ticket: ParkeerTicket) {
        if(ticket.code.isEmpty() && ticket.validDate.isEmpty()){
            networkError()
        } else if(cacheIsOutdated(ticket.validDate)){
            networkError()
        } else {
            succesGenerate(ticket)
        }
    }

    private fun networkError(){
        succes.value = false
        otherError.value = false
        networkError.value = true
    }

    private fun succesGenerate(ticket: ParkeerTicket){
        networkError.value = false
        otherError.value = false
        APITicket = ticket
        succes.value = true
    }

    override fun setError(error: Int){
        if(error == 400) {
            succes.value = false
            networkError.value = false
            otherError.value = true
        }
    }

    private fun cacheIsOutdated(cacheDate: String): Boolean {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        var today = LocalDate.now().format(formatter)

        return cacheDate != today
    }

    fun createQRCode(data: String): Bitmap{
        val width = 512
        val height = 512

        var dimen = if (width < height) width else height

        var qrEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, dimen)

        return qrEncoder.encodeAsBitmap()
    }
}