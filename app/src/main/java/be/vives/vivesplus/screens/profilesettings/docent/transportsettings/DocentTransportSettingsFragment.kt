package be.vives.vivesplus.screens.profilesettings.docent.transportsettings

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
import be.vives.vivesplus.databinding.FragmentDocentTransportSettingsBinding
import be.vives.vivesplus.model.Member
import be.vives.vivesplus.model.TransportOption
import com.google.android.material.chip.Chip

class DocentTransportSettingsFragment : Fragment(), MembersDAOCallback, TransportSettingsDAOCallback {

    private lateinit var viewModel: DocentTransportSettingsViewModel
    private lateinit var binding: FragmentDocentTransportSettingsBinding
    private lateinit var inflaterfrag: LayoutInflater
    private lateinit var docentTransport: ArrayList<TransportOption>
    private var transportIds: ArrayList<Int> = arrayListOf()
    private lateinit var transportList: ArrayList<TransportOption>
    private lateinit var member : Member
    private var campusIds: ArrayList<Int> = arrayListOf()
    private var fieldOfStudiesIds: ArrayList<Int> = arrayListOf()
    private var allPossilbleTransportids: ArrayList<Int> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_docent_transport_settings, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = DocentTransportSettingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(DocentTransportSettingsViewModel::class.java)
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
                binding.progressBar4.visibility = View.VISIBLE
                binding.btnsavetransportdocent.isEnabled = false
                viewModel.btnSaveClickedFinished()
                MembersDAO(requireContext(), this).put(this.member, campusIds, fieldOfStudiesIds, transportIds)
            }
        })
    }

    private fun loadPageInfo() {
        MembersDAO(requireContext(), this).getInfo()
    }

    override fun dataLoaded(members: MutableList<Member>) {}

    override fun dataLoaded(member: Member) {
        this.member = member
        member.campusses.forEach { it ->
            campusIds.add(it.id)
        }
        member.fieldOfStudies.forEach { it ->
            fieldOfStudiesIds.add(it.id)
        }

        docentTransport = member.transportOptions
        member.transportOptions.forEach { it ->
            transportIds.add(it.id)
        }
        TransportSettingsDAO(requireContext(), this).getAll()
    }

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
        binding.progressBar4.visibility = View.GONE
    }

    private fun addUnselectedChip(it: TransportOption) {
        val chip = inflaterfrag.inflate(R.layout.unselected_chips_layout, binding.chipgroupTransportDocent, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            chipclicked(it)
        }
        binding.chipgroupTransportDocent.addView(chip)
    }

    private fun addSelectedChip(it: TransportOption) {
        val chip = inflaterfrag.inflate(R.layout.selected_chips_layout, binding.chipgroupTransportDocent, false) as Chip
        chip.text = it.name
        chip.setOnClickListener { _ ->
            chipclicked(it)
        }
        binding.chipgroupTransportDocent.addView(chip)
    }

    private fun chipclicked(selectedTransportOption: TransportOption) {
        if (transportIds.contains(selectedTransportOption.id)) {
            docentTransport.remove(selectedTransportOption)
            transportIds.remove(selectedTransportOption.id)
        } else {
            docentTransport.add(selectedTransportOption)
            transportIds.add(selectedTransportOption.id)
        }

        binding.chipgroupTransportDocent.removeAllViews()

        transportList.forEach { it ->
            if (transportIds.contains(it.id)) {
                addSelectedChip(it)
            }
            else {
                addUnselectedChip(it)
            }
        }
    }

    override fun putSucces() {
        requireActivity().onBackPressed()
    }
}