package be.vives.vivesplus.screens.identity

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentPresenceQrBinding
import be.vives.vivesplus.model.CodeQR
import be.vives.vivesplus.screens.parkeerticket.ParkingTicketFragmentDirections
import be.vives.vivesplus.util.WebServiceCallback
import com.android.volley.VolleyError
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class PresenceQrFragment: Fragment(), OnBackPressedDispatcherOwner,
    WebServiceCallback, PresenceQrDAOCallback {
    private lateinit var viewModel: PresenceQrViewModel
    private lateinit var binding: FragmentPresenceQrBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_presence_qr, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = PresenceQrViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(PresenceQrViewModel::class.java)

        binding.idBtnAgain.setOnClickListener {
            viewModel.call()
        }

        init()

        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            findNavController().navigate(ParkingTicketFragmentDirections.actionGlobalModernDashboardFragment())
            isEnabled = true
        }

        return binding.root
    }

    private fun init(){
        askPermission()

        setObservers()

        loadPageInfo()
    }

    private fun loadPageInfo() {
        getQRinfo()
    }

    private fun getQRinfo() {
        PresenceQRDAO(requireContext(),this).getQRCode()
    }

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return requireActivity().onBackPressedDispatcher
    }

    private fun askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context)) {
                Settings.System.putInt(context?.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                Settings.System.putInt(context?.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 255);
            } else {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(getString(R.string.toestemming_helderheid))
                builder.setMessage(getString(R.string.toestemming_helderheid_text))

                builder.setPositiveButton(getString(R.string.Toestemming_helderheid_ok)) { _, _ ->
                    val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                    context?.startActivity(intent)
                }

                builder.setNegativeButton(getString(R.string.annuleer)){ _, _ -> }

                val dialog : AlertDialog = builder.create()
                dialog.show()
            }
        }
    }

    private fun setObservers(){
        viewModel.networkError.observe(viewLifecycleOwner, Observer {
            if(it) {
                binding.idTVerror.visibility = View.GONE

                binding.idIVQrcode.visibility = View.VISIBLE
                binding.idTVnetworkError.visibility = View.VISIBLE
                binding.idBtnAgain.visibility = View.VISIBLE
            }
        })

        viewModel.otherError.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.idTVuses.visibility = View.GONE
                binding.idTVerror.visibility = View.GONE
                binding.idBtnAgain.visibility = View.GONE

                binding.idIVQrcode.visibility = View.VISIBLE
                binding.idTVerror.visibility = View.VISIBLE
            }
        })

        viewModel.succes.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.idTVerror.visibility = View.GONE
                binding.idTVnetworkError.visibility = View.GONE
                binding.idBtnAgain.visibility = View.GONE


                val gson = Gson()
                var convertedObject = gson.toJson(viewModel.QRcode)
                binding.idIVQrcode.setImageBitmap(viewModel.createQRCode(convertedObject))


                binding.idIVQrcode.visibility = View.VISIBLE
                binding.idTVnumber.visibility = View.VISIBLE
                binding.idTVname.visibility = View.VISIBLE
            }
        })

        viewModel.name.observe(viewLifecycleOwner, Observer { name ->
            binding.idTVname.text = name
        })

        viewModel.kulNumber.observe(viewLifecycleOwner, Observer { kulNumber ->
            binding.idTVnumber.text = kulNumber
        })
    }





    override fun dataLoaded(jsonArray: JSONArray) {}


    override fun dataLoaded(jsonObject: JSONObject) {}

    override fun dataLoaded(ticket: CodeQR) {
        val kulNumber = ticket.kulNumber
        val firstName = ticket.firstName
        val lastName = ticket.lastName

        viewModel.setKulNumber(kulNumber)
        viewModel.setName(firstName, lastName)
    }

    override fun setError(error: Int) {}


    /*override fun dataLoaded(docent: Member) {
        val name = docent.firstName + " " + docent.lastName
        viewModel.setName(name)
        val kulNumber: String? = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_kul_number))
        viewModel.setKulNumber(kulNumber)
    }*/



    override fun onPause() {
        super.onPause()
        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE)

        if (Settings.System.canWrite(context)) {
            Settings.System.putInt(context?.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        if (Settings.System.canWrite(context)) {
            Settings.System.putInt(context?.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(context?.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 255);
        }
    }


}
