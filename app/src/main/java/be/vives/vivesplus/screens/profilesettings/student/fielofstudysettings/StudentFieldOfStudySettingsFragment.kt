package be.vives.vivesplus.screens.profilesettings.student.fielofstudysettings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.FieldOfStudyDAO
import be.vives.vivesplus.dao.FieldOfStudyDAOCallback
import be.vives.vivesplus.dao.StudentDAO
import be.vives.vivesplus.dao.StudentDAOCallback
import be.vives.vivesplus.databinding.FragmentStudentFieldOfStudySettingsBinding
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.model.FieldOfStudy
import be.vives.vivesplus.model.Student
import com.google.android.material.chip.Chip

class StudentFieldOfStudySettingsFragment : Fragment(), FieldOfStudyDAOCallback, StudentDAOCallback {

    private lateinit var viewModel: StudentFieldOfStudySettingsViewModel
    private lateinit var binding: FragmentStudentFieldOfStudySettingsBinding
    private lateinit var inflaterfrag: LayoutInflater
    private lateinit var student: Student
    private lateinit var fieldOfStudyList: ArrayList<FieldOfStudy>
    private var studentFOS: FieldOfStudy? = null
    private var studentCampus: Campus? = null
    private var meansOfTransportationIds: ArrayList<Int> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_field_of_study_settings, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = StudentFieldOfStudySettingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(StudentFieldOfStudySettingsViewModel::class.java)
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
                binding.progressBar7.visibility = View.VISIBLE
                binding.btnsavefosstudent.isEnabled = false
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
        FieldOfStudyDAO(requireContext(),this).getAll()
    }

    override fun putSucces() {

    }

    override fun dataLoadedFailed() {

    }

    override fun dataLoaded(fieldOfStudyLst: ArrayList<FieldOfStudy>) {
        fieldOfStudyList = fieldOfStudyLst
        fieldOfStudyLst.forEach { it ->
            if(it == studentFOS) {
                addSelectedChip(it)
            } else {
                addUnselectedChip(it)
            }
        }
        binding.progressBar7.visibility = View.GONE
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

    private fun chipclicked(fos: FieldOfStudy) {
        binding.chipgroupFOS.removeAllViews()
        studentFOS = fos
        fieldOfStudyList.forEach { it ->
            if (fos == it) {
                addSelectedChip(it)
            }
            else {
                addUnselectedChip(it)
            }
        }
    }

}