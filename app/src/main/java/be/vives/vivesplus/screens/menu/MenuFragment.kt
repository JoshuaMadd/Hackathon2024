package be.vives.vivesplus.screens.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import be.vives.vivesplus.BuildConfig
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.MembersDAO
import be.vives.vivesplus.dao.MembersDAOCallback
import be.vives.vivesplus.dao.StudentDAO
import be.vives.vivesplus.dao.StudentDAOCallback
import be.vives.vivesplus.databinding.FragmentMenuBinding
import be.vives.vivesplus.enum.ProfileRole
import be.vives.vivesplus.model.Member
import be.vives.vivesplus.model.Student
import be.vives.vivesplus.util.CheckerRole
import be.vives.vivesplus.util.LogoutHandler
import be.vives.vivesplus.util.PreferencesManager
import com.squareup.picasso.Picasso

class MenuFragment : Fragment(), StudentDAOCallback, MembersDAOCallback {

    private lateinit var viewModel: MenuViewModel
    private lateinit var binding: FragmentMenuBinding
    private var profileRole : ProfileRole? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = MenuViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(MenuViewModel::class.java)
        binding.myModel = viewModel

        init()

        return binding.root
    }

    private fun init() {
        setProfile()
        setObservers()
        loadPageInfo()
        initVersionNumber()
    }

    private fun setProfile() {
        profileRole = CheckerRole(requireContext()).checkRole()
    }

    private fun setObservers() {
        viewModel.name.observe(viewLifecycleOwner, Observer { name ->
            binding.txtName.text = name.toString()
        })

        viewModel.kulNumber.observe(viewLifecycleOwner, Observer { kulnumber ->
            binding.txtKulNumber.text = kulnumber.toString()
        })

        viewModel.versionNumber.observe(viewLifecycleOwner, Observer { versionnumber ->
            binding.txtVersionNumber.text = versionnumber.toString()
        })

        viewModel.btnLogoutClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it){
                viewModel.btnLogoutClickedFinished()
                LogoutHandler.showLogoutAlertMenu(requireContext(), requireActivity())
            }
        })

        viewModel.btnSettingsClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it){
                viewModel.btnSettingsClickedFinished()
                if (profileRole != null) {
                    if(profileRole == ProfileRole.STUDENT) {
                        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToStudentProfileSettingsFragment())
                    }
                    if(profileRole == ProfileRole.MEMBER) {
                        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToDocentProfileSettingsFragment())
                    }
                }
            }
        })

        viewModel.navigateToRestaurants.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                navigateToRestaurants()
            }
        })

        viewModel.navigateToTransport.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                navigateToTransport()
            }
        })

        viewModel.navigateToTransport.observe(viewLifecycleOwner, Observer {
            if (it) {
                navigateToTransport()
            }
        })

        viewModel.navigateToMyAbsences.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.btnMyAbsencesFinished()
                navigateToMyAbsences()
            }
        })

        viewModel.navigateToSearchDocent.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.btnsearchdocentFinished()
                navigateToSearchDocent()
            }
        })

        viewModel.navigateToSupport.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.btnSupportFinished()
                navigateToSupport()
            }
        })

        viewModel.navigateToWebshop.observe(viewLifecycleOwner, Observer {
            if(it) {
                viewModel.btnWebshopFinished()
                navigateToWebshop()
            }
        })

        viewModel.navigateToScanner.observe(viewLifecycleOwner, Observer {
            if(it) {
                viewModel.btnScannerFinished()
                navigateToScanner()
            }
        })

        viewModel.navigateToPresence.observe(viewLifecycleOwner, Observer {
            if(it) {
                viewModel.btnPresenceFinished()
                navigateToPresence()
            }
        })
    }

    private fun navigateToMyAbsences() {
        requireView().findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToAdminAbsencesFragment())
    }

    private fun navigateToPresence() {
        requireView().findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToPresenceQRFragment())
    }

    private fun navigateToScanner() {
        viewModel.btnScannerFinished()
        requireView().findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToScannerFragment())
    }

    private fun navigateToSearchDocent() {
        requireView().findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToSearchDocentFragment())
    }

    private fun navigateToSupport() {
        this.findNavController().navigate(
            MenuFragmentDirections.actionMenuFragmentToWebViewFragment(getString(R.string.vivesplus_base_link) + "help?t=" + PreferencesManager().getStringFromPreferences(requireContext(), "pref_jwt_without_beaerer")!! + "&os=Android"))
    }

    private fun navigateToTransport() {
        viewModel.btnTransportFinished()
        requireView().findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToOpenbaarvervoerTrafficFragment(0.toString()))
    }

    private fun navigateToRestaurants() {
        viewModel.btnRestaurantsFinished()
        requireView().findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToRestaurantFragment())
    }

    private fun navigateToWebshop(){
        viewModel.btnWebshopFinished()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vivesplus_webshop_link)))
        startActivity(intent)
    }


    private fun initVersionNumber() {
        viewModel.setVersionNumber(BuildConfig.VERSION_NAME)
    }

    private fun loadPageInfo() {
        if (profileRole != null) {
            if(profileRole == ProfileRole.STUDENT) {
                StudentDAO(requireContext(), this).getMe()
            }
            if(profileRole == ProfileRole.MEMBER) {
                MembersDAO(requireContext(), this).getInfo()
            }
        }
    }

    override fun dataLoaded(members: MutableList<Member>) {}

    override fun dataLoaded(member: Member) {
        val name = member.firstName + " " + member.lastName
        viewModel.setName(name)
        val kulNumber: String? = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_kul_number))
        viewModel.setKulNumber(kulNumber)
        binding.myAbsencesLayout.visibility = View.VISIBLE
        binding.searchdocentLayout.visibility = View.VISIBLE
        binding.scannerQRcode.visibility = View.GONE


        var imageUrl = member.photoUrl
        if (imageUrl != null) {
            if (   PreferencesManager().getIntFromPreferences(requireContext(),"aantalRotaties") != -1) {
                Picasso.with(context).invalidate(member.photoUrl)
                PreferencesManager().writeIntToPreferences(requireContext(),"aantalRotaties",0)
                imageUrl =  imageUrl!!.plus(("?=" + System.currentTimeMillis()))
            }
            Picasso.with(context)
                .load(imageUrl)
                .into(binding.imageProfile)
        }
    }

    override fun dataLoaded(student: Student) {
        val name = student.firstName + " " + student.lastName
        viewModel.setName(name)
        val kulNumber: String? = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_kul_number))
        viewModel.setKulNumber(kulNumber)
    }

    override fun putSucces() {

    }

    override fun dataLoadedFailed() {

    }
}
