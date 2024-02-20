package be.vives.vivesplus.screens.profilesettings.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.*
import be.vives.vivesplus.databinding.FragmentStudentProfileSettingsBinding
import be.vives.vivesplus.model.Campus
import be.vives.vivesplus.model.Student
import be.vives.vivesplus.util.PreferencesManager

class StudentProfileSettingsFragment : Fragment(), StudentDAOCallback {

    private lateinit var viewModel: StudentProfileSettingsViewModel
    private lateinit var binding: FragmentStudentProfileSettingsBinding
    private var selectedCampusStudent: Campus? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_profile_settings, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = StudentProfileSettingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(StudentProfileSettingsViewModel::class.java)
        binding.myModel = viewModel

        init()

        return binding.root
    }

    private fun init() {
        setObservers()
        loadPageInfo()
    }

    private fun setObservers() {
        viewModel.name.observe(viewLifecycleOwner, Observer { name ->
            binding.txtName.text = name.toString()
        })

        viewModel.kulNumber.observe(viewLifecycleOwner, Observer { kulnumber ->
            binding.txtKulNumber.text = kulnumber.toString()
        })

        viewModel.btnGeneralInfoClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it) {
                this.findNavController().navigate(StudentProfileSettingsFragmentDirections.actionStudentProfileSettingsFragmentToStudentGeneralInfoFragment())
                viewModel.btnGeneralInfoClickedFinished()
            }
        })

        viewModel.btnFOSClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it) {
                this.findNavController().navigate(StudentProfileSettingsFragmentDirections.actionStudentProfileSettingsFragmentToStudentFieldOfStudySettingsFragment())
                viewModel.btnFOSClickedFinished()
            }
        })

        viewModel.btnCampusClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it) {
                this.findNavController().navigate(StudentProfileSettingsFragmentDirections.actionStudentProfileSettingsFragmentToStudentCampusSettingsFragment())
                viewModel.btnCampusClickedFinished()
            }
        })

        viewModel.btnTransportClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it) {
                this.findNavController().navigate(StudentProfileSettingsFragmentDirections.actionStudentProfileSettingsFragmentToStudentTransportSettingsFragment())
                viewModel.btnTransportClickedFinished()
            }
        })

        viewModel.btnJobstudentClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it) {
                this.findNavController().navigate(StudentProfileSettingsFragmentDirections.actionStudentProfileSettingsFragmentToJobstudentFragment())
                viewModel.btnJobstudentClickedFinish()
            }
        })

        viewModel.btnDeleteAccountClicked.observe(viewLifecycleOwner, Observer { it ->
            if(it) {
                this.findNavController().navigate(StudentProfileSettingsFragmentDirections.actionStudentProfileSettingsFragmentToDeleteAccountFragment())
                viewModel.btnDeleteAccountClickedFinished()
            }
        })
    }



    private fun loadPageInfo() {
        getStudentInfo()
    }

    override fun dataLoaded(student: Student) {
        val name = student.firstName + " " + student.lastName
        viewModel.setName(name)
        val kulNumber: String? = PreferencesManager().getStringFromPreferences(requireContext(), requireContext().getString(R.string.pref_kul_number))
        viewModel.setKulNumber(kulNumber)
        selectedCampusStudent = student.campus
        viewModel.setOpleidingsnaam(student.study?.name)
    }

    override fun putSucces() {}

    override fun dataLoadedFailed() {}

    private fun getStudentInfo() {
        StudentDAO(requireContext(), this).getMe()
    }
}