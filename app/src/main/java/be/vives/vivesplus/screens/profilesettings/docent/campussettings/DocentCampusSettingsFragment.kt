package be.vives.vivesplus.screens.profilesettings.docent.campussettings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.databinding.FragmentDocentCampusSettingsBinding
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.model.FieldOfStudy
import be.vives.vivesplus.model.Member
import com.google.android.material.chip.Chip

class DocentCampusSettingsFragment : Fragment(), MembersDAOCallback, CampusDAOCallback {

    private lateinit var viewModel: DocentCampusSettingsViewModel
    private lateinit var binding: FragmentDocentCampusSettingsBinding
    private lateinit var inflater: LayoutInflater
    private lateinit var member: Member
    private lateinit var campusList: ArrayList<Campus>
    private lateinit var docentFOS: ArrayList<FieldOfStudy>
    private lateinit var docentCampus: ArrayList<Campus>
    private var campusIds: ArrayList<Int> = arrayListOf()
    private var fieldOfStudiesIds: ArrayList<Int> = arrayListOf()
    private var meansOfTransportationIds: ArrayList<Int> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_docent_campus_settings, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = DocentCampusSettingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(DocentCampusSettingsViewModel::class.java)
        binding.myModel = viewModel
        this.inflater = inflater

        init()

        return binding.root
    }

    private fun init(){
        setObserver()
        loadPageInfo()
    }

    private fun setObserver() {
        viewModel.btnSaveClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it){
                if (campusIds.size < 1){
                    Toast.makeText(context, R.string.geenCampusDocent, Toast.LENGTH_LONG ).show()
                } else {
                    binding.progressBar3.visibility = View.VISIBLE
                    binding.btnsaveCampusDocent.isEnabled = false
                    viewModel.btnSaveClickedFinished()
                    MembersDAO(requireContext(), this).put(this.member, campusIds, fieldOfStudiesIds, meansOfTransportationIds)
                }
            }
        })
    }


    private fun loadPageInfo() {
        MembersDAO(requireContext(), this).getInfo()
    }

    override fun dataLoaded(members: MutableList<Member>) {}

    override fun dataLoaded(member: Member) {
        this.member = member

        docentFOS = member.fieldOfStudies
        docentCampus = member.campusses

        member.campusses.forEach {
            campusIds.add(it.id)
        }
        member.fieldOfStudies.forEach {
            fieldOfStudiesIds.add(it.id)
        }

        member.transportOptions.forEach {
            meansOfTransportationIds.add(it.id)
        }

        CampusDAO(requireContext(),this).get()
    }

    override fun putSucces() {
        requireActivity().onBackPressed()
    }

    override fun campusDataLoaded(campusList: ArrayList<Campus>) {
        this.campusList = campusList
        campusList.forEach { it ->
            if(docentCampus.contains(it)) {
                addSelectedChip(it)
            } else {
                addUnselectedChip(it)
            }
        }
        binding.progressBar3.visibility = View.GONE
    }

    private fun addUnselectedChip(it: Campus) {
        val chip = inflater.inflate(R.layout.unselected_chips_layout, binding.chipgroupCampus, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            chipclicked(it)
        }
        binding.chipgroupCampus.addView(chip)

    }

    private fun addSelectedChip(it: Campus) {
        val chip = inflater.inflate(R.layout.selected_chips_layout, binding.chipgroupCampus, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            chipclicked(it)
        }
        binding.chipgroupCampus.addView(chip)

    }

    private fun chipclicked(selectedCampus: Campus) {
        // al erin -> hier eruit
        if (docentCampus.contains(selectedCampus)) {
            docentCampus.remove(selectedCampus)
            if(campusIds.contains(selectedCampus.id)) {
                campusIds.remove(selectedCampus.id)
            }
        } else {
            docentCampus.add(selectedCampus)
            campusIds.add(selectedCampus.id)
        }

        binding.chipgroupCampus.removeAllViews()

        campusList.forEach { it ->
            if (docentCampus.contains(it)) {
                addSelectedChip(it)
            }
            else {
                addUnselectedChip(it)
            }
        }
    }
}