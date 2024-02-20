package be.vives.vivesplus.screens.profilesettings.student.tutorial

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
import be.vives.vivesplus.databinding.FragmentStudentTutorialBinding
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.model.FieldOfStudy
import be.vives.vivesplus.model.Student
import com.google.android.material.chip.Chip

class StudentTutorialFragment : Fragment(), FieldOfStudyDAOCallback, CampusDAOCallback, StudentDAOCallback {

    private lateinit var viewModel: StudentTutorialViewModel
    private lateinit var binding: FragmentStudentTutorialBinding
    private lateinit var inflaterfrag: LayoutInflater
    private lateinit var campusList: ArrayList<Campus>
    private lateinit var fosList: ArrayList<FieldOfStudy>
    private var selectedFOSstudent: FieldOfStudy? = null
    private var selectedCampusStudent: Campus? = null
    private lateinit var student: Student
    private var meansOfTransportationIds: ArrayList<Int> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_tutorial, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = StudentTutorialViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(StudentTutorialViewModel::class.java)
        binding.myModel = viewModel
        inflaterfrag = inflater

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
                if(selectedCampusStudent != null && selectedFOSstudent != null ) {
                    binding.progressBar10.visibility = View.VISIBLE
                    binding.btnContinueTutorialStudent.isEnabled = false
                    StudentDAO(requireContext(), this).put(selectedCampusStudent!!.id, selectedFOSstudent!!.id, meansOfTransportationIds, this.student.optInJob)
                }
                else {
                    Toast.makeText(context, R.string.geenCampusOfFOSStudent, Toast.LENGTH_LONG ).show()

                }
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
        selectedFOSstudent = student.fieldOfStudy
        selectedCampusStudent = student.campus
        CampusDAO(requireContext(),this).get()
        FieldOfStudyDAO(requireContext(), this).getAll()
    }

    override fun putSucces() {
        navigateToMainActivity()
    }

    override fun dataLoadedFailed() {}

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun dataLoaded(fieldOfStudyLst: ArrayList<FieldOfStudy>) {
        this.fosList = fieldOfStudyLst
        fieldOfStudyLst.forEach { it ->
            if(it == selectedFOSstudent) {
                addSelectedFOSChip(it)
            } else {
                addUnselectedFOSChip(it)
            }
        }
        binding.progressBar10.visibility = View.GONE
    }

    override fun campusDataLoaded(campusList: ArrayList<Campus>) {
        this.campusList = campusList
        campusList.forEach { it ->
            if(it == selectedCampusStudent) {
                addSelectedCampusChip(it)
            } else {
                addUnselectedCampusChip(it)
            }
        }
    }

    private fun addSelectedCampusChip(it: Campus) {
        val chip = inflaterfrag.inflate(R.layout.selected_chips_layout, binding.chipgroupCampusTutorial, false) as Chip
        chip.text = it.name
        binding.chipgroupCampusTutorial.addView(chip)
    }

    private fun addUnselectedCampusChip(it: Campus) {
        val chip = inflaterfrag.inflate(R.layout.unselected_chips_layout, binding.chipgroupCampusTutorial, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            campusChipclicked(it)
        }
        binding.chipgroupCampusTutorial.addView(chip)
    }

    private fun campusChipclicked(camp: Campus) {
        binding.chipgroupCampusTutorial.removeAllViews()
        selectedCampusStudent = camp
        campusList.forEach { it ->
            if (camp == it) {
                addSelectedCampusChip(it)
            }
            else {
                addUnselectedCampusChip(it)
            }
        }
    }

    private fun addSelectedFOSChip(it: FieldOfStudy) {
        val chip = inflaterfrag.inflate(R.layout.selected_chips_layout, binding.chipgroupFOSTutorial, false) as Chip
        chip.text = it.name
        binding.chipgroupFOSTutorial.addView(chip)
    }

    private fun addUnselectedFOSChip(it: FieldOfStudy) {
        val chip = inflaterfrag.inflate(R.layout.unselected_chips_layout, binding.chipgroupFOSTutorial, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            fosChipclicked(it)
        }
        binding.chipgroupFOSTutorial.addView(chip)
    }

    private fun fosChipclicked(fos: FieldOfStudy) {
        binding.chipgroupFOSTutorial.removeAllViews()
        selectedFOSstudent = fos
        fosList.forEach { it ->
            if (fos == it) {
                addSelectedFOSChip(it)
            }
            else {
                addUnselectedFOSChip(it)
            }
        }
    }
}