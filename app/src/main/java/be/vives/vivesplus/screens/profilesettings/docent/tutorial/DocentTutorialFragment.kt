package be.vives.vivesplus.screens.profilesettings.docent.tutorial

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import be.vives.vivesplus.MainActivity
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.databinding.FragmentDocentTutorialBinding
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.model.FieldOfStudy
import be.vives.vivesplus.model.Member
import com.google.android.material.chip.Chip

class DocentTutorialFragment : Fragment(), FieldOfStudyDAOCallback, CampusDAOCallback, MembersDAOCallback {

    private lateinit var viewModel: DocentTutorialViewModel
    private lateinit var binding: FragmentDocentTutorialBinding
    private lateinit var inflater: LayoutInflater
    private lateinit var member: Member
    private lateinit var allCampusses: ArrayList<Campus>
    private lateinit var allFos: ArrayList<FieldOfStudy>
    private lateinit var fosFromDocent: ArrayList<FieldOfStudy>
    private lateinit var campussesFromDocent: ArrayList<Campus>
    private var campusIdsFromDocent: ArrayList<Int> = arrayListOf()
    private var fieldOfStudiesIdsFromDocent: ArrayList<Int> = arrayListOf()
    private var meansOfTransportationIds: ArrayList<Int> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_docent_tutorial, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = DocentTutorialViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(DocentTutorialViewModel::class.java)
        binding.myModel = viewModel
        this.inflater = inflater

        init()

        return binding.root
    }

    private fun init() {
        setObservers()
        loadPageInfo()
    }

    private fun setObservers() {
        viewModel.btnSaveClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it){
                viewModel.btnSaveClickedFinished()
                if(campusIdsFromDocent.size > 0 && fieldOfStudiesIdsFromDocent.size > 0) {
                    binding.progressBar6.visibility = View.VISIBLE
                    binding.btnContinueTutorialDocent.isEnabled = false
                    MembersDAO(requireContext(), this).put(this.member, campusIdsFromDocent, fieldOfStudiesIdsFromDocent, meansOfTransportationIds)
                } else {
                    Toast.makeText(context, R.string.geenCampusOfFOSDocent, Toast.LENGTH_LONG ).show()
                }
            }
        })
    }

    private fun loadPageInfo() {
        MembersDAO(requireContext(), this).getInfo()
    }

    override fun dataLoaded(member: Member) {
        this.member = member
        fosFromDocent = member.fieldOfStudies
        campussesFromDocent = member.campusses
        member.campusses.forEach { it ->
            campusIdsFromDocent.add(it.id)
        }
        member.fieldOfStudies.forEach { it ->
            fieldOfStudiesIdsFromDocent.add(it.id)
        }
        member.transportOptions.forEach { it ->
            meansOfTransportationIds.add(it.id)
        }
        FieldOfStudyDAO(requireContext(), this).getAll()
        CampusDAO(requireContext(), this).get()
    }

    override fun putSucces() {
        navigateToMainActivity()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun dataLoaded(fieldOfStudyLst: ArrayList<FieldOfStudy>) {
        this.allFos = fieldOfStudyLst

        fieldOfStudyLst.forEach { it ->
            if(fosFromDocent.contains(it)) {
                addSelectedFOSChip(it)
            } else {
                addUnselectedFOSChip(it)
            }
        }
    }

    override fun campusDataLoaded(campusList: ArrayList<Campus>) {
        this.allCampusses = campusList

        campusList.forEach { it ->
            if(campussesFromDocent.contains(it)) {
                addSelectedCampusChip(it)
            } else {
                addUnselectedCampusChip(it)
            }
        }
        binding.progressBar6.visibility = View.GONE
    }

    private fun addSelectedCampusChip(it: Campus) {
        val chip = inflater.inflate(R.layout.selected_chips_layout, binding.chipgroupCampusTutorial, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            campusChipclicked(it)
        }
        binding.chipgroupCampusTutorial.addView(chip)
    }

    private fun addUnselectedCampusChip(it: Campus) {
        val chip = inflater.inflate(R.layout.unselected_chips_layout, binding.chipgroupCampusTutorial, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            campusChipclicked(it)
        }
        binding.chipgroupCampusTutorial.addView(chip)
    }

    private fun addSelectedFOSChip(it: FieldOfStudy) {
        val chip = inflater.inflate(R.layout.selected_chips_layout, binding.chipgroupFOSTutorial, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            fosChipclicked(it)
        }
        binding.chipgroupFOSTutorial.addView(chip)
    }

    private fun addUnselectedFOSChip(it: FieldOfStudy) {
        val chip = inflater.inflate(R.layout.unselected_chips_layout, binding.chipgroupFOSTutorial, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            fosChipclicked(it)
        }
        binding.chipgroupFOSTutorial.addView(chip)
    }

    private fun campusChipclicked(selectedCampus: Campus) {
        // al erin -> hier eruit
        if (campussesFromDocent.contains(selectedCampus)) {
            campussesFromDocent.remove(selectedCampus)
            if(campusIdsFromDocent.contains(selectedCampus.id)) {
                campusIdsFromDocent.remove(selectedCampus.id)
            }
        } else {
            campussesFromDocent.add(selectedCampus)
            campusIdsFromDocent.add(selectedCampus.id)
        }

        binding.chipgroupCampusTutorial.removeAllViews()

        allCampusses.forEach { it ->
            if (campussesFromDocent.contains(it)) {
                addSelectedCampusChip(it)
            }
            else {
                addUnselectedCampusChip(it)
            }
        }
    }

    private fun fosChipclicked(selectedFos: FieldOfStudy) {
        // al erin -> hier eruit
        if (fosFromDocent.contains(selectedFos)) {
            fosFromDocent.remove(selectedFos)
            if(fieldOfStudiesIdsFromDocent.contains(selectedFos.id)) {
                fieldOfStudiesIdsFromDocent.remove(selectedFos.id)
            }
        } else {
            fosFromDocent.add(selectedFos)
            fieldOfStudiesIdsFromDocent.add(selectedFos.id)
        }

        binding.chipgroupFOSTutorial.removeAllViews()

        allFos.forEach { it ->
            if (fosFromDocent.contains(it)) {
                addSelectedFOSChip(it)
            }
            else {
                addUnselectedFOSChip(it)
            }
        }
    }

    override fun dataLoaded(members: MutableList<Member>) {}
}