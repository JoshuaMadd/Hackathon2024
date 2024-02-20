package be.vives.vivesplus.screens.profilesettings.docent.fielofstudysettings

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
import be.vives.vivesplus.databinding.FragmentDocentFieldOfStudySettingsBinding
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.model.FieldOfStudy
import be.vives.vivesplus.model.Member
import com.google.android.material.chip.Chip

class DocentFieldOfStudySettingsFragment : Fragment(), MembersDAOCallback, FieldOfStudyDAOCallback {

    private lateinit var viewModel: DocentFieldOfStudySettingsViewModel
    private lateinit var binding: FragmentDocentFieldOfStudySettingsBinding
    private lateinit var inflaterfrag: LayoutInflater
    private lateinit var member: Member
    private lateinit var fosList: ArrayList<FieldOfStudy>
    private lateinit var docentFOS: ArrayList<FieldOfStudy>
    private lateinit var docentCampus: ArrayList<Campus>
    private var campusIds: ArrayList<Int> = arrayListOf()
    private var fieldOfStudiesIds: ArrayList<Int> = arrayListOf()
    private var meansOfTransportationIds: ArrayList<Int> = arrayListOf()
    private var selectedChipCount: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_docent_field_of_study_settings, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = DocentFieldOfStudySettingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(DocentFieldOfStudySettingsViewModel::class.java)
        binding.myModel = viewModel
        inflaterfrag = inflater
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
                if (selectedChipCount < 1){
                    Toast.makeText(context, R.string.noFosSelected, Toast.LENGTH_LONG ).show()
                } else {
                    binding.progressBar2.visibility = View.VISIBLE
                    binding.btnsaveFOSDocent.isEnabled = false
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

        member.campusses.forEach { it ->
            campusIds.add(it.id)
        }
        member.fieldOfStudies.forEach { it ->
            fieldOfStudiesIds.add(it.id)
        }
        member.transportOptions.forEach { it ->
            meansOfTransportationIds.add(it.id)
        }

        FieldOfStudyDAO(requireContext(),this).getAll()
    }

    override fun putSucces() {
        requireActivity().onBackPressed()
    }

    override fun dataLoaded(fieldOfStudyLst: ArrayList<FieldOfStudy>) {
        this.fosList = fieldOfStudyLst
        selectedChipCount = docentFOS.size
        fieldOfStudyLst.forEach { it ->
            if(docentFOS.contains(it)) {
                addSelectedChip(it)
            } else {
                addUnselectedChip(it)
            }
        }
        binding.progressBar2.visibility = View.GONE
    }

    private fun addUnselectedChip(it: FieldOfStudy) {
        val chip = inflaterfrag.inflate(R.layout.unselected_chips_layout, binding.chipgroupFOS, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            chipclicked(it)

        }
        binding.chipgroupFOS.addView(chip)

    }

    private fun addSelectedChip(it: FieldOfStudy) {
        val chip = inflaterfrag.inflate(R.layout.selected_chips_layout, binding.chipgroupFOS, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            chipclicked(it)

        }

        binding.chipgroupFOS.addView(chip)

    }

    private fun chipclicked(selectedFOS: FieldOfStudy) {
        if (docentFOS.contains(selectedFOS)) {
            docentFOS.remove(selectedFOS)
            if(fieldOfStudiesIds.contains(selectedFOS.id)) {
                fieldOfStudiesIds.remove(selectedFOS.id)
                selectedChipCount--
            }
        } else {
            docentFOS.add(selectedFOS)
            fieldOfStudiesIds.add(selectedFOS.id)
            selectedChipCount++
        }

        binding.chipgroupFOS.removeAllViews()

        fosList.forEach { it ->
            if (docentFOS.contains(it)) {
                addSelectedChip(it)
            }
            else {
                addUnselectedChip(it)
            }
        }
    }
}