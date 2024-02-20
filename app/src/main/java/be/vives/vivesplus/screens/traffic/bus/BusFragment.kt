package be.vives.vivesplus.screens.traffic.bus

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.databinding.FragmentBusBinding
import be.vives.vivesplus.enum.ProfileRole
import be.vives.vivesplus.model.Bus
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.model.Member
import be.vives.vivesplus.model.Student
import be.vives.vivesplus.util.CheckerRole
import be.vives.vivesplus.util.PreferencesManager
import java.util.*
import kotlin.collections.ArrayList

class BusFragment : Fragment(), BusDAOCallback, StudentDAOCallback, MembersDAOCallback, CampusDAOCallback{

    lateinit var binding: FragmentBusBinding
    private lateinit var viewModel: BusViewModel
    private lateinit var adapter: ArrayAdapter<Bus>
    private var campusToBeSearched: Campus? = null
    private lateinit var campusAdapter: ArrayAdapter<Campus>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bus, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = BusViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(BusViewModel::class.java)
        binding.myModel = viewModel

        init()

        return binding.root
    }

    private fun init() {
        val profileRole = CheckerRole(requireContext()).checkRole()
        if (profileRole != null) {
            if(profileRole == ProfileRole.STUDENT) {
                StudentDAO(requireContext(), this).getMe()
            }
            if(profileRole == ProfileRole.MEMBER) {
                MembersDAO(requireContext(), this).getInfo()
            }
        }
    }

    override fun dataLoaded(student: Student) {
        val lastselectedcampusbus = PreferencesManager().getIntFromPreferences(requireContext(), requireContext().getString(R.string.lastselectedcampusbus))!!
        if(lastselectedcampusbus == -1){
            campusToBeSearched = student.campus
        }
        CampusDAO(requireContext(), this).get()
    }

    override fun putSucces() {

    }

    override fun dataLoadedFailed() {

    }

    override fun campusDataLoaded(campusList: ArrayList<Campus>) {
        createSpinnerAdapterCampussen(campusList)
        createListenerCampussen()
    }

    private fun createSpinnerAdapterCampussen(campusList: java.util.ArrayList<Campus>) {
        if (campusToBeSearched != null) {
            for ((position, campus) in campusList.withIndex()) {
                if (campus.id == campusToBeSearched!!.id) {
                    Collections.swap(campusList, 0, position)
                }
            }
            BusDAO(requireContext(), this).getBusses(campusToBeSearched!!.id)
        }
        else {
            val lastselectedcampusbus = PreferencesManager().getIntFromPreferences(requireContext(), requireContext().getString(R.string.lastselectedcampusbus))!!
            for ((position, campus) in campusList.withIndex()) {
                if (campus.id == lastselectedcampusbus) {
                    Collections.swap(campusList, 0, position)
                }
            }
            BusDAO(requireContext(), this).getBusses(lastselectedcampusbus)
        }
        campusAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, campusList)
        binding.campusspinner.adapter = campusAdapter

    }

    private fun createListenerCampussen() {
        binding.campusspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val selectedObject = binding.campusspinner.selectedItem as Campus
                    PreferencesManager().writeIntToPreferences(requireContext(), requireContext().getString(R.string.lastselectedcampusbus), selectedObject.id)
                    BusDAO(requireContext(),this@BusFragment).getBusses(selectedObject.id)
                }
            }
    }

    override fun dataLoaded(members: MutableList<Member>) {}

    override fun dataLoaded(member: Member) {
        val lastselectedcampusbus = PreferencesManager().getIntFromPreferences(requireContext(), requireContext().getString(R.string.lastselectedcampusbus))!!
        if(lastselectedcampusbus == -1){
            campusToBeSearched = member.campusses.first()
        }
        CampusDAO(requireContext(), this).get()
    }

    override fun dataLoaded(bussen: ArrayList<Bus>) {
        addFirstElements(bussen)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, bussen)
        binding.spinnerhaltes.adapter = adapter

        binding.spinnerhaltes.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedObject = binding.spinnerhaltes.selectedItem as Bus

                PreferencesManager().writeStringToPreferences(requireContext(), requireContext().getString(R.string.selected_bus_id), selectedObject.halteId)

                val url = selectedObject.infoUrl
                val dark = "?inverse=white"
                val darkmodeUrl = "$url$dark"
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> { binding.webView.loadUrl(url) } // Night mode is not active, we're using the light theme
                    Configuration.UI_MODE_NIGHT_YES -> {binding.webView.loadUrl(darkmodeUrl)} // Night mode is active, we're using dark theme
                }
                val webSettings = binding.webView.settings
                webSettings.javaScriptEnabled = true

                binding.webView.webViewClient = WebViewClient()
            }
        }
    }

    private fun addFirstElements(bussen: ArrayList<Bus>) {
        val prefBus = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.selected_bus_id))!!
        val gevondenPrefBus = bussen.find { bus -> bus.halteId == prefBus }
        bussen.remove(gevondenPrefBus)
        if (gevondenPrefBus != null) {
            bussen.add(0, gevondenPrefBus)
        }
    }
}
