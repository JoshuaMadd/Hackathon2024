package be.vives.vivesplus.screens.uurroosterdetail


import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ScrollView
import android.widget.Toast
import androidmads.library.qrgenearator.BuildConfig
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentUurroosterDetailBinding
import be.vives.vivesplus.enum.EventType
import be.vives.vivesplus.enum.ProfileRole
import be.vives.vivesplus.model.CodeQR
import be.vives.vivesplus.model.Coordinates
import be.vives.vivesplus.screens.identity.PresenceQRDAO
import be.vives.vivesplus.screens.identity.PresenceQrDAOCallback
import be.vives.vivesplus.screens.identity.PresenceQrViewModel
import be.vives.vivesplus.screens.identity.PresenceQrViewModelFactory
import be.vives.vivesplus.util.CheckerRole
import be.vives.vivesplus.util.LogoutHandler
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import org.json.JSONObject
import org.osmdroid.config.Configuration.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import java.io.File
import java.time.LocalDateTime


class UurroosterDetailFragment : Fragment(), OnBackPressedDispatcherOwner, PresenceQrDAOCallback {

    private lateinit var binding: FragmentUurroosterDetailBinding
    private lateinit var viewModel: UurroosterDetailViewModel
    private lateinit var viewModelIdentity: PresenceQrViewModel
    private val TAG = "SCAN AANWEZIGHEDEN KNOP TEST"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_uurrooster_detail, container, false)

        val application = requireNotNull(this.activity).application
        val factory = UurroosterDetailViewModelFactory(application, requireContext())
        viewModel = ViewModelProvider(this, factory).get(UurroosterDetailViewModel::class.java)
        val applicationIdentity  = requireNotNull(this.activity).application
        val fact = PresenceQrViewModelFactory(applicationIdentity)
        viewModelIdentity = ViewModelProvider(this, fact).get(PresenceQrViewModel::class.java)

        val args = UurroosterDetailFragmentArgs.fromBundle(requireArguments())

        viewModel.getEventDetail(args.eventType, args.id)

        binding.viewModel = viewModel


        setObservers(args.eventType, args.id)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(
                UurroosterDetailFragmentDirections.actionUurroosterDetailFragmentToUurroosterFragment(
                    viewModel.bindingEvent.value!!.startDateTimeString
                )
            )
            isEnabled = true
        }



        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        val startScanButton = view.findViewById<Button>(R.id.btnScanPrecenses)

        startScanButton.setOnClickListener {
            startScanner()
        }
    }


    private fun setObservers(eventType: String, eventId: String) {
        viewModel.bindingEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it.type) {
                    EventType.ACADEMIC -> {
                        binding.eventDetailImg.setImageResource(R.drawable.timelineblauw)
                        if (checkRoleMemberAndTime()) {
                            binding.btnScanPrecenses.visibility = View.VISIBLE
                        }
                    }
                    EventType.COURSE -> {
                        binding.eventDetailImg.setImageResource(R.drawable.time_stamp_circles)

                        if (checkRoleMemberAndTime()) {
                            binding.btnDetailAbsence.visibility = View.VISIBLE
                            binding.btnScanPrecenses.visibility = View.VISIBLE
                        }
                    }
                    EventType.CANCELLED -> {
                        binding.eventDetailCancelledBlock.visibility = View.VISIBLE
                        binding.eventDetailImg.setImageResource(R.drawable.time_stamp_circles)

                        if (checkRoleMemberAndTime()) {
                            binding.btnDetailManageAbscence.visibility = View.VISIBLE
                            binding.btnDetailAbsence.visibility = View.GONE
                            binding.btnScanPrecenses.visibility = View.GONE
                        }
                    }
                }

                if (it.coordinates != null) {
                    if (it.coordinates!!.latitude != 0.0 && it.coordinates!!.longitude != 0.0) {
                        binding.mapView.visibility = View.VISIBLE
                        setUpMapView(it.coordinates!!)
                    }
                }
            }
        })

        viewModel.errorLogOut.observe(viewLifecycleOwner, Observer {
            if (it) {
                LogoutHandler.showLogoutAlert(requireContext(), requireActivity())
            }
        })

        viewModel.errorForbidden.observe(viewLifecycleOwner, Observer {
            if (it) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(getString(R.string.wrong_back_to_timetable))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        findNavController().navigate(
                            UurroosterDetailFragmentDirections.actionUurroosterDetailFragmentToUurroosterFragment(
                                null
                            )
                        )
                    }
                val alert = builder.create()
                alert.show()
            }
        })

        viewModel.btnAbsent.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.makeAbsenceSaveBlock.visibility = View.VISIBLE
                binding.btnDetailAbsence.visibility = View.GONE
                binding.btnScanPrecenses.visibility = View.GONE
                viewModel.btnAbsentClickedFinished()
                binding.detailCourseScrollView.post(Runnable {
                    binding.detailCourseScrollView.fullScroll(ScrollView.FOCUS_DOWN)
                })
            }
        })

        viewModel.btnSaveAbsence.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.getEventDetail(eventType, eventId)
                binding.makeAbsenceSaveBlock.visibility = View.GONE
                viewModel.btnSaveAbsenceClickedFinished()
            }
        })

        viewModel.btnCancelAbsence.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.makeAbsenceSaveBlock.visibility = View.GONE
                binding.btnDetailAbsence.visibility = View.VISIBLE
                binding.btnScanPrecenses.visibility = View.VISIBLE
                viewModel.btnCancelAbsenceClickedFinished()
            }
        })

        viewModel.btnManageAbsence.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(UurroosterDetailFragmentDirections.actionUurroosterDetailFragmentToAdminAbsencesFragment())
                viewModel.btnManageAbsenceClickedFinished()
            }
        })

        viewModel.makeToastAbsence.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.absence_made),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.makeToastAbsenceFinished()
            }
        })

        /*viewModel.navigateToQr.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.navigateToQr.observe(viewLifecycleOwner, Observer {
                    if (it) {
                        val description = viewModel.bindingEvent.value?.description ?: ""
                        val bundle = bundleOf("description" to description)
                        findNavController().navigate(R.id.action_uurroosterDetailFragment_to_scannerFragment, bundle)
                    }
                })


            }
        })*/

        binding.btnScanPrecenses.setOnClickListener {
            startScanner()
        }

    }
    /*-----------SCANNER IMPLEMENTATIE-------------*/

    private fun startScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
        integrator.setTorchEnabled(false)
        integrator.setPrompt("")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                handleScannedQRCode(result.contents)
                startScanner()
            } else {
                //wanneer scanner gesloten wordt
                Toast.makeText(requireContext(), "Done scanning", Toast.LENGTH_SHORT).show()
            }
        } else {
            // ???
        }
    }



    private fun handleScannedQRCode(qrCodeData: String) {
        try {
            val jsonobject = JSONObject(qrCodeData)
            val kulNumber = jsonobject.getString("kulNumber")
            val firstName = jsonobject.getString("firstName")
            val lastName = jsonobject.getString("lastName")

            val url = "${requireContext().getString(R.string.vivesplus_api_link)}/events/course/${viewModel.bindingEvent.value?.id}/registrations"

            PresenceQRDAO(requireContext(), this).postPresence(
                kulNumber,
                firstName,
                lastName,
                url
            )

        } catch (e: JSONException) {
            Toast.makeText(requireContext(),"Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        TODO("Not yet implemented")
    }






        /*-----------------------------------------------------------------------*/


        private fun setUpMapView(coordinates: Coordinates) {
            makeMapOfMapViewVisible()

            binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
            binding.mapView.setMultiTouchControls(true)

            val mapController = binding.mapView.controller
            val startPoint = GeoPoint(coordinates.latitude, coordinates.longitude)
            mapController.setZoom(19.0)             //MAX ZOOM VAN MAPNIK MAP !!
            mapController.setCenter(startPoint)

            val marker = Marker(binding.mapView)
            marker.position = startPoint
            val markerIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_mapview_pin)
            marker.icon = markerIcon
            marker.infoWindow = null

            binding.mapView.overlays.add(marker)
        }

        private fun makeMapOfMapViewVisible() {
            getInstance().apply {
                userAgentValue = BuildConfig.APPLICATION_ID
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    osmdroidBasePath = File(requireContext().filesDir, "osmdroid").apply {
                        mkdirs()
                    }
                    osmdroidTileCache = File(osmdroidBasePath, "tiles").apply {
                        mkdirs()
                    }
                }
            }
        }

        private fun checkRoleMemberAndTime(): Boolean {
            return CheckerRole(requireContext()).checkRole() == ProfileRole.MEMBER &&
                    viewModel.bindingEvent.value!!.endDateTime.isAfter(LocalDateTime.now())
        }

        override fun dataLoaded(ticket: CodeQR) {
            Toast.makeText(requireContext(),"${ticket.firstName} ${ticket.lastName} ${ticket.kulNumber}", Toast.LENGTH_LONG).show()
        }

    override fun setErrorPost(error: Int) {
        Toast.makeText(requireContext(),"Something went wrong", Toast.LENGTH_LONG).show()
    }
}
