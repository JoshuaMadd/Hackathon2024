package be.vives.vivesplus.screens.traffic.train

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.databinding.FragmentTrainBinding
import be.vives.vivesplus.model.*
import be.vives.vivesplus.util.PreferencesManager
import java.lang.Exception

class TrainFragment : Fragment(), TrainDAOCallback, TrainSearchDAOCallback {

    private lateinit var nonCampusStationsAdapter: ArrayAdapter<Station>
    private lateinit var campusStationsAdapter: ArrayAdapter<Station>
    lateinit var binding: FragmentTrainBinding
    private lateinit var viewModel: TrainViewModel
    private var direction: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_train, container, false)

        val application  = requireNotNull(this.activity).application
        val fact = TrainViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(TrainViewModel::class.java)
        binding.myModel = viewModel

        binding.connectionsList.layoutManager = LinearLayoutManager(context)
        binding.connectionsList.adapter = TrainAdapter(ArrayList(), requireContext())

        TrainSearchDAO(requireContext(), this).getStation()

        binding.fab.setOnClickListener {
            direction = !direction
            binding.indicator.visibility = View.VISIBLE
            getConnections()
        }

        getConnections()

        return binding.root
    }

    override fun dataLoaded(connectios: ArrayList<Connection>) {
        binding.indicator.visibility = View.GONE
        binding.fab.visibility = View.VISIBLE
        binding.connectionsList.adapter = TrainAdapter(connectios, requireContext())
    }

    private fun getConnections() {

        val homeStation = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_home_station))!!
        val schoolStation = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_campus_station))!!

        if (homeStation != "") {
            if (homeStation == schoolStation) {
                binding.fab.visibility = View.GONE
                binding.indicator.visibility = View.GONE
            } else {
                if (direction) {
                    TrainDAO(requireContext(), this).getConnection(homeStation, schoolStation, 6)
                }
                else {
                    TrainDAO(requireContext(), this).getConnection(schoolStation, homeStation, 6)
                }
            }
        } else {
            binding.fab.visibility = View.GONE
            binding.indicator.visibility = View.GONE
        }
    }

    override fun dataLoaded(stations: MutableList<Station>) {
        val campusStations = stations.filter { station -> station.campusId is Int }.toMutableList()
        createAdapters(stations as ArrayList<Station>, campusStations)
        createListeners()
    }

    private fun createListeners() {

        binding.firstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedObject = binding.firstSpinner.selectedItem as Station
                PreferencesManager().writeStringToPreferences(requireContext(), requireContext().getString(R.string.pref_campus_station), selectedObject.name)
                checkIfStationChanged(selectedObject)
            }

            private fun checkIfStationChanged(selectedObject: Station) {
                val prevSelectedObjectName = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.prev_pref_selected_campus_name))!!

                if (prevSelectedObjectName != selectedObject.name) {
                    PreferencesManager().writeStringToPreferences(requireContext(), requireContext().getString(R.string.pref_selected_campus_name), selectedObject.name)
                    PreferencesManager().writeStringToPreferences(requireContext(), requireContext().getString(R.string.prev_pref_selected_campus_name), selectedObject.name)
                    refresh()
                }
            }
        }
        binding.autotextview.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                val homestation = binding.autotextview.text.toString()
                PreferencesManager().writeStringToPreferences(requireContext(), requireContext().getString(R.string.pref_home_station), homestation)
                refresh()
            }
    }

    private fun refresh() {
        getConnections()
    }

    private fun createAdapters(stations: ArrayList<Station>, campusStations: MutableList<Station>) {
        nonCampusStationsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, stations)
        addFirstElementToSpinner(stations, campusStations)
        campusStationsAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, campusStations)

        val homestation = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_home_station)) ?: ""

        binding.firstSpinner.adapter = campusStationsAdapter
        binding.autotextview.setAdapter(nonCampusStationsAdapter)
        binding.autotextview.setText(homestation)
    }

    private fun addFirstElementToSpinner(stations: ArrayList<Station>, campusStations: MutableList<Station>) {
        val profileSchoolStation = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_selected_campus_name))!!
        val schoolStation = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_campus_station))!!
        val changedCampus = PreferencesManager().getBoolFromPreferences(requireContext(), requireContext().getString(R.string.pref_changed_campus))!!

        if (profileSchoolStation != "" && !changedCampus) {
            modifyList(stations, schoolStation, campusStations)
        } else {
            modifyList(stations, profileSchoolStation, campusStations)
            PreferencesManager().writeBoolToPreferences(requireContext(), requireContext().getString(R.string.pref_changed_campus), false)
        }
    }

    private fun modifyList(stations: ArrayList<Station>, schoolStation: String, campusStations: MutableList<Station>) {
        try {
            val foundSchoolStation = stations.find { station -> station.name == schoolStation }
            campusStations.remove(foundSchoolStation)
            campusStations.add(0, foundSchoolStation!!)
        } catch (ex: Exception) { }
    }
}
