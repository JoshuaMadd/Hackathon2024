package be.vives.vivesplus.screens.profilesettings.docent.aanwezigheden

import ScannerFragment
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.integration.android.IntentIntegrator


class ScannerViewModel(application: Application) : AndroidViewModel(application) {
    private val _scannedQRCode = MutableLiveData<String>()
    val scannedQRCode: LiveData<String>
        get() = _scannedQRCode

    fun onQRCodeScanned(qrCodeData: String) {
        // Hier kun je eventueel aanvullende logica toevoegen
        // bijvoorbeeld het controleren van de QR-code of het doorsturen naar de backend
        _scannedQRCode.value = qrCodeData
    }
}