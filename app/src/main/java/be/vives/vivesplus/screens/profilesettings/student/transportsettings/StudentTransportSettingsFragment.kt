package be.vives.vivesplus.screens.profilesettings.student.transportsettings

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
import be.vives.vivesplus.databinding.FragmentStudentTransportSettingsBinding
import be.vives.vivesplus.model.*
import com.google.android.material.chip.Chip

class StudentTransportSettingsFragment : Fragment(), TransportSettingsDAOCallback, StudentDAOCallback {

    private lateinit var viewModel: StudentTransportSettingsViewModel
    private lateinit var binding: FragmentStudentTransportSettingsBinding
    private lateinit var inflaterfrag: LayoutInflater
    private lateinit var transportList: ArrayList<TransportOption>
    private lateinit var studentTransport: ArrayList<TransportOption>
    private lateinit var student: Student
    private var transportIds: ArrayList<Int> = arrayListOf()
    private var allPossilbleTransportids: ArrayList<Int> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_transport_settings, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = StudentTransportSettingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(StudentTransportSettingsViewModel::class.java)
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
                binding.progressBar9.visibility = View.VISIBLE
                binding.btnsavefosstudent.isEnabled = false
                viewModel.btnSaveClickedFinished()
                StudentDAO(requireContext(), this).put(this.student.campus!!.id, this.student.fieldOfStudy!!.id, transportIds, this.student.optInJob)
            }
        })
    }

    private fun loadPageInfo() {
        StudentDAO(requireContext(), this).getMe()
    }

    override fun dataLoaded(student: Student) {
        this.student = student
        studentTransport = student.transportOptions
        student.transportOptions.forEach { it ->
            transportIds.add(it.id)
        }
        TransportSettingsDAO(requireContext(), this).getAll()
    }

    override fun putSucces() {
        requireActivity().onBackPressed()
    }

    override fun dataLoadedFailed() {}

    override fun transportSettingsDataLoaded(transportList: ArrayList<TransportOption>) {
        this.transportList = transportList

        transportList.forEach { it ->
            allPossilbleTransportids.add(it.id)
        }

        transportList.forEach { it ->
            if (transportIds.contains(it.id)) {
                addSelectedChip(it)
            } else {
                addUnselectedChip(it)
            }
        }
        binding.progressBar9.visibility = View.GONE
    }

    private fun addUnselectedChip(it: TransportOption) {
        val chip = inflaterfrag.inflate(R.layout.unselected_chips_layout, binding.chipgroupTransportStudent, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            chipclicked(it)
        }
        binding.chipgroupTransportStudent.addView(chip)
    }

    private fun addSelectedChip(it: TransportOption) {
        val chip = inflaterfrag.inflate(R.layout.selected_chips_layout, binding.chipgroupTransportStudent, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            chipclicked(it)
        }
        binding.chipgroupTransportStudent.addView(chip)

    }

    private fun chipclicked(selectedTransportOption: TransportOption) {
        if (transportIds.contains(selectedTransportOption.id)) {
            transportIds.remove(selectedTransportOption.id)
            studentTransport.remove(selectedTransportOption)
        } else {
            studentTransport.add(selectedTransportOption)
            transportIds.add(selectedTransportOption.id)
        }

        binding.chipgroupTransportStudent.removeAllViews()

        transportList.forEach { it ->
            if (transportIds.contains(it.id)) {
                addSelectedChip(it)
            } else {
                addUnselectedChip(it)
            }
        }
    }
}