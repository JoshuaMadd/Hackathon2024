package be.vives.vivesplus.screens.profilesettings.student.generalinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.FieldOfStudyDAOCallback
import be.vives.vivesplus.dao.StudentDAO
import be.vives.vivesplus.dao.StudentDAOCallback
import be.vives.vivesplus.databinding.FragmentStudentGeneralInfoBinding
import be.vives.vivesplus.model.FieldOfStudy
import be.vives.vivesplus.model.Student

class StudentGeneralInfoFragment : Fragment(), StudentDAOCallback, FieldOfStudyDAOCallback {

    private lateinit var viewModel: StudentGeneralInfoViewModel
    private lateinit var binding: FragmentStudentGeneralInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_general_info, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = StudentGeneralInfoViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(StudentGeneralInfoViewModel::class.java)
        binding.myModel = viewModel

        init()

        return binding.root
    }

    private fun init() {
        setObservers()
        loadPageInfo()
    }

    private fun loadPageInfo() {
        getStudentInfo()
    }

    private fun getStudentInfo() {
        StudentDAO(requireContext(), this).getMe()
    }


    private fun setObservers() {
        viewModel.name.observe(viewLifecycleOwner, Observer { name ->
            binding.inputStudentName.text = name.toString()
        })

        viewModel.email.observe(viewLifecycleOwner, Observer { email ->
            binding.inputEmailStudent.text = email.toString()
        })

        viewModel.education.observe(viewLifecycleOwner, Observer { fos ->
            binding.educationStudentGeneral.text = fos.toString()
        })
    }

    override fun dataLoaded(student: Student) {
        val name = student.firstName + " " + student.lastName
        val email = student.email
        val education = student.study?.name
        viewModel.setName(name)
        viewModel.setEmail(email)
        if (education == null) {
            binding.educationStudentGeneral.text = resources.getString(R.string.no_education_found);
        } else {
            viewModel.setEducation(education)
        }
        binding.progressBar8.visibility = View.GONE
    }

    override fun putSucces() {

    }

    override fun dataLoadedFailed() {

    }

    override fun dataLoaded(fieldOfStudyLst: ArrayList<FieldOfStudy>) {

    }
}