package be.vives.vivesplus.screens.profilesettings.docent.aanwezigheden

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import be.vives.vivesplus.R
import com.google.zxing.integration.android.IntentIntegrator

class HostScannerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start the scanner when the fragment is created
        startScanner()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Navigate back to ScannerFragment
                findNavController().navigate(R.id.action_hostScannerFragment_to_scannerFragment)
            }
        })
    }

    private fun startScanner() {
        // Initialize the ZXing Intent Integrator
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
    }
}