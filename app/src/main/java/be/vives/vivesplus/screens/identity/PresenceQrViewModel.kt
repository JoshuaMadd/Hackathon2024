package be.vives.vivesplus.screens.identity

import android.app.Application
import android.graphics.Bitmap
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.vives.vivesplus.model.CodeQR
import com.android.volley.VolleyError
import kotlinx.coroutines.internal.AddLastDesc
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PresenceQrViewModel (application: Application) : AndroidViewModel(application),
    PresenceQrDAOCallback {
    var networkError = MutableLiveData<Boolean>()
    var succes = MutableLiveData<Boolean>()
    var otherError = MutableLiveData<Boolean>()
    var QRcode = CodeQR("","", "")


    private var _name = MutableLiveData<String>()
    val name : LiveData<String>
        get() {return _name}

    private var _kulNumber = MutableLiveData<String?>()
    val kulNumber : LiveData<String?>
        get() {return _kulNumber}

    init {
        networkError.value = false
        succes.value = false
        otherError.value = false
        _kulNumber.value = ""
        _name.value = ""
        call()
    }


    fun setName(firstName: String, lastName: String) {
        _name.value = "$firstName $lastName"
    }

    fun setKulNumber(kulNumber: String?) {
        _kulNumber.value = kulNumber
    }

    fun call(){
        PresenceQRDAO(getApplication(), this).getQRCode()
    }


    override fun dataLoaded(ticket: CodeQR) {
            succesGenerate(ticket)
    }

    private fun networkError(){
        succes.value = false
        otherError.value = false
        networkError.value = true
    }

    private fun succesGenerate(ticket: CodeQR){
        networkError.value = false
        otherError.value = false
        QRcode = ticket
        succes.value = true
    }

    override fun setErrorPost(error: Int){
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

    fun createQRCode(data: String): Bitmap {
        val width = 512
        val height = 512

        var dimen = if (width < height) width else height

        var qrEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, dimen)

        return qrEncoder.encodeAsBitmap()
    }

    /*fun createQRCode(jsonString: String): Bitmap {
        val jsonData = JSONObject(jsonString)
        val jsonString = jsonData.toString()
        val qrCodeBitmap = generateQRCodeBitmap(jsonString)
        return qrCodeBitmap
    }*/

    private fun generateQRCodeBitmap(data: String): Bitmap {
        val width = 512
        val height = 512
        val qrEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, width)
        return qrEncoder.encodeAsBitmap()
    }


}
