package be.vives.vivesplus.screens.profilesettings.student.campussettings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.databinding.FragmentStudentCampusSettingsBinding
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.model.FieldOfStudy
import be.vives.vivesplus.model.Student
import com.google.android.material.chip.Chip

class StudentCampusSettingsFragment : Fragment(), CampusDAOCallback, StudentDAOCallback {

    private lateinit var viewModel: StudentCampusSettingsViewModel
    private lateinit var binding: FragmentStudentCampusSettingsBinding
    private lateinit var inflaterfrag: LayoutInflater
    private lateinit var student: Student
    private lateinit var campusList: ArrayList<Campus>
    private var studentFOS: FieldOfStudy? = null
    private var studentCampus: Campus? = null
    private var meansOfTransportationIds: ArrayList<Int> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_campus_settings, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = StudentCampusSettingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(StudentCampusSettingsViewModel::class.java)
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
                binding.progressBar5.visibility = View.VISIBLE
                binding.btnsaveCampusstudent.isEnabled = false
                viewModel.btnSaveClickedFinished()
                StudentDAO(requireContext(), this).put(studentCampus?.id, studentFOS?.id, meansOfTransportationIds, this.student.optInJob)
                requireActivity().onBackPressed()
            }
        })
    }

    private fun loadPageInfo() {
        StudentDAO(requireContext(), this).getMe()
    }

    override fun dataLoaded(student: Student) {
        this.student = student
        student.transportOptions.forEach { it ->
            meansOfTransportationIds.add(it.id)
        }
        studentFOS = student.fieldOfStudy
        studentCampus = student.campus
        CampusDAO(requireContext(),this).get()
    }

    override fun putSucces() {

    }

    override fun dataLoadedFailed() {

    }

    override fun campusDataLoaded(campusList: ArrayList<Campus>) {
        this.campusList = campusList
        campusList.forEach { it ->
            if(it == studentCampus) {
                addSelectedChip(it)
            } else {
                addUnselectedChip(it)
            }
        }
        binding.progressBar5.visibility = View.GONE
    }

    private fun addUnselectedChip(it: Campus) {
        val chip = inflaterfrag.inflate(R.layout.unselected_chips_layout, binding.chipgroupCampus, false) as Chip
        chip.text = it.name
        chip.textSize = 20F
        chip.setOnClickListener { _ ->
            chipclicked(it)
        }
        binding.chipgroupCampus.addView(chip)

    }

    private fun addSelectedChip(it: Campus) {
        val chip = inflaterfrag.inflate(R.layout.selected_chips_layout, binding.chipgroupCampus, false) as Chip
        chip.text = it.name
        chip.textSize = 20F
        chip.setOnClickListener { _ ->
            chipclicked(it)
        }
        binding.chipgroupCampus.addView(chip)

    }

    private fun chipclicked(camp: Campus) {
        binding.chipgroupCampus.removeAllViews()
        studentCampus = camp
        campusList.forEach { it ->
            if (camp == it) {
                addSelectedChip(it)
            }
            else {
                addUnselectedChip(it)
            }
        }
    }
}