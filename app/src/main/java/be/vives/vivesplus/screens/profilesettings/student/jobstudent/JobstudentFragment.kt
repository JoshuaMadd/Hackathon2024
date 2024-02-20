package be.vives.vivesplus.screens.profilesettings.student.jobstudent

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.StudentDAO
import be.vives.vivesplus.dao.StudentDAOCallback
import be.vives.vivesplus.databinding.FragmentStudentJobstudentBinding
import be.vives.vivesplus.model.Student

class JobstudentFragment : Fragment(), StudentDAOCallback {

    private lateinit var viewModel: JobstudentViewModel
    private lateinit var binding: FragmentStudentJobstudentBinding
    private lateinit var inflaterfrag: LayoutInflater
    private lateinit var student: Student
    private var transportIds: ArrayList<Int> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_jobstudent, container, false)
        val application = requireNotNull(this.activity).application
        val fact = JobstudentViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(JobstudentViewModel::class.java)
        binding.myModel = viewModel
        inflaterfrag = inflater

        init()

        return binding.root
    }

    private fun init(){
        setObserver()
        loadPageInfo()
    }

    private fun loadPageInfo(){
        StudentDAO(requireContext(), this).getMe()
    }

    private fun setObserver(){
        viewModel.jobstudentBoolean.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.btnSaveFinished()
                binding.switchJobstudent.isChecked = true
            } })

        viewModel.save.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar11.visibility = View.VISIBLE
                binding.btnsaveGeneralInfo.isEnabled = false
                save()
            }
        })
    }


    override fun dataLoaded(student: Student) {

        val wantsJobstudent = student.optInJob

        if (wantsJobstudent){
            viewModel.setJobstudent()
        }

        this.student = student
        binding.progressBar11.visibility = View.GONE
    }

    private fun save() {
        val saveStudent = Student(
            this.student.kulNumber,
            this.student.email,
            this.student.firstName,
            this.student.lastName,
            this.student.study,
            this.student.campus,
            this.student.fieldOfStudy,
            this.student.transportOptions,
            binding.switchJobstudent.isChecked
        )

        saveStudent.transportOptions.forEach { it ->
            transportIds.add(it.id)
        }

        StudentDAO(requireContext(),this).put(saveStudent.campus!!.id, saveStudent.fieldOfStudy!!.id, transportIds, saveStudent.optInJob)

    }

    override fun putSucces() {
        requireActivity().onBackPressed()
    }

    override fun dataLoadedFailed() {

    }
}