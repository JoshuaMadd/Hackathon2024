import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import be.vives.vivesplus.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ScannerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scanner, container, false)

        /*
        val args = arguments

        // argument ophalen meegegeven uit lessenrooster
        if (args != null) {
            // Retrieve the parameter value using the specified key
            val description = args.getString("description", "")
            // Use the parameter value as needed
            // For example, set it to a TextView
            view.findViewById<TextView>(R.id.editTextFillable).text = description
        }*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        val startScanButton = view.findViewById<FloatingActionButton>(R.id.scanButton)

        // Set a click listener for the button
        startScanButton.setOnClickListener {
            // Start the barcode scanner when the button is clicked
            startScanner()
        }
    }

    private fun startScanner() {
        // Initialize the ZXing Intent Integrator
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
        integrator.setPrompt("")
    }


    // Override onActivityResult to handle the result of the barcode scanning
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the result to the ZXing Intent Integrator
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // Check if the scan was successful and handle the result
            if (result.contents != null) {
                handleScannedQRCode(result.contents)
                startScanner()
            } else {
                // Handle case where the user canceled the scan
                // For example, display a message to the user
                Toast.makeText(requireContext(), "Done scanning", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle other results if needed
        }
    }

    private fun handleScannedQRCode(qrCodeData: String) {
        val jsonObject = JSONObject(qrCodeData)
        val firstName = jsonObject.getString("firstName")
        val lastName = jsonObject.getString("lastName")
        val rNumber = jsonObject.getString("rNumber")

        val url =
            URL("${context?.getString(R.string.vivesplus_api_link)}/events/course/5627315720220207110000130000/registrations")

        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "POST"
            //headers
            setRequestProperty("Content-Type", "application/json")

            //body
            val postData = JSONObject().apply {
                put("firstName", firstName)
                put("lastName", lastName)
                put("rNumber", rNumber)
            }

            val postDataBytes = postData.toString().toByteArray(Charsets.UTF_8)
            outputStream.write(postDataBytes)

            val toast =
                Toast.makeText(requireContext(), "Geregistreerd: $firstName $lastName", Toast.LENGTH_LONG)
            toast.show()

        }
    }
}
