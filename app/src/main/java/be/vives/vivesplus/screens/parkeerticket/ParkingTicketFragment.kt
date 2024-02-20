
package be.vives.vivesplus.screens.parkeerticket

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentParkingticketBinding


class ParkingTicketFragment : Fragment(), OnBackPressedDispatcherOwner{
    private lateinit var viewModel: ParkingTicketViewModel
    private lateinit var binding: FragmentParkingticketBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parkingticket, container, false)
        viewModel = ViewModelProvider(this).get(ParkingTicketViewModel::class.java)

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
                binding.idTVDate.visibility = View.GONE
                binding.idTVuses.visibility = View.GONE
                binding.idTVvalid.visibility = View.GONE
                binding.idTVerror.visibility = View.GONE

                binding.idIVQrcode.visibility = View.VISIBLE
                binding.idTVnetworkError.visibility = View.VISIBLE
                binding.idBtnAgain.visibility = View.VISIBLE
            }
        })

        viewModel.otherError.observe(viewLifecycleOwner, Observer {
            if(it){
                binding.idTVDate.visibility = View.GONE
                binding.idTVuses.visibility = View.GONE
                binding.idTVvalid.visibility = View.GONE
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

                binding.idIVQrcode.setImageBitmap(viewModel.createQRCode(viewModel.APITicket.code))

                var day = viewModel.APITicket.validDate.substring(8, viewModel.APITicket.validDate.length)
                var month = viewModel.APITicket.validDate.substring(5, 7)
                var year = viewModel.APITicket.validDate.substring(0,4)

                binding.idTVDate.text = day + "-" + month + "-" + year

                binding.idIVQrcode.visibility = View.VISIBLE
                binding.idTVDate.visibility = View.VISIBLE
                binding.idTVuses.visibility = View.VISIBLE
                binding.idTVvalid.visibility = View.VISIBLE
            }
        })
    }

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