package be.vives.vivesplus.screens.profilesettings.docent.generalinfo

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
import be.vives.vivesplus.dao.FieldOfStudyDAOCallback
import be.vives.vivesplus.dao.MembersDAO
import be.vives.vivesplus.dao.MembersDAOCallback
import be.vives.vivesplus.databinding.FragmentDocentGeneralInfoBinding
import be.vives.vivesplus.model.FieldOfStudy
import be.vives.vivesplus.model.Member

class DocentGeneralInfoFragment : Fragment(), MembersDAOCallback, FieldOfStudyDAOCallback {

    private lateinit var viewModel: DocentGeneralInfoViewModel
    private lateinit var binding: FragmentDocentGeneralInfoBinding
    private lateinit var member: Member
    private var campusIds: ArrayList<Int> = arrayListOf()
    private var fieldOfStudiesIds: ArrayList<Int> = arrayListOf()
    private var transportIds: ArrayList<Int> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_docent_general_info, container, false)
        val application  = requireNotNull(this.activity).application
        val fact = DocentGeneralInfoViewModelFactory(application)
        viewModel = ViewModelProvider(this, fact).get(DocentGeneralInfoViewModel::class.java)
        binding.myModel = viewModel

        init()

        return binding.root
    }

    private fun init() {
        setObservers()
        loadPageInfo()
    }

    private fun loadPageInfo() {
        getDocentInfo()
    }

    private fun getDocentInfo() {
        MembersDAO(requireContext(),this).getInfo()
    }

    private fun setObservers() {
        viewModel.name.observe(viewLifecycleOwner, Observer { name ->
            binding.NameTv.text = name.toString()
        })

        viewModel.firstname.observe(viewLifecycleOwner, Observer { firstName ->
            binding.editTextTextPersonName.setText(firstName.toString())
        })

        viewModel.email.observe(viewLifecycleOwner, Observer { email ->
            binding.editTextTextEmailAddress.text = email.toString()
        })

        viewModel.phone.observe(viewLifecycleOwner, Observer { phone ->
            binding.editTextPhone.setText(phone.toString())
        })

        viewModel.jobTitle.observe(viewLifecycleOwner, Observer { title ->
            binding.editJobTitle.setText(title.toString())
        })

        viewModel.bachelor.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.switchBachelor.isChecked = true
            } })

        viewModel.graduate.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.switchGraduaat.isChecked = true
            } })

        viewModel.save.observe(viewLifecycleOwner, Observer { if (it) {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.btnSaveFinished()
            binding.btnsaveGeneralInfo.isEnabled = false
            save()
        } })
    }


    override fun dataLoaded(fieldOfStudyLst: ArrayList<FieldOfStudy>) {
    }

    override fun dataLoaded(members: MutableList<Member>) {
    }

    override fun dataLoaded(member: Member) {
        val name = member.lastName
        val firstName = member.firstName
        val email = member.email
        val phone = member.phoneNumber
        val jobTitle = member.functieOmschrijving
        val isBachelor = member.bachelor
        val isGraduate = member.graduaat

        viewModel.setName(name)
        viewModel.setFirstName(firstName)
        viewModel.setEmail(email)
        viewModel.setPhone(phone)
        viewModel.setJobTitle(jobTitle)

        if (isBachelor) {
            viewModel.setBachelor()
        }
        if (isGraduate) {
            viewModel.setGraduate()
        }

        this.member = member

        binding.progressBar.visibility = View.GONE

    }

    private fun save() {
        val newMember = Member(
            binding.editTextTextPersonName.text.toString(),
            this.member.lastName,
            this.member.photoUrl,
            this.member.kulNumber,
            binding.editTextPhone.text.toString(),
            binding.editJobTitle.text.toString(),
            this.member.email,
            this.member.campusses,
            this.member.fieldOfStudies,
            this.member.studies,
            this.member.transportOptions,
            binding.switchBachelor.isChecked,
            binding.switchGraduaat.isChecked)

        newMember.campusses.forEach { it ->
            campusIds.add(it.id)
        }

        newMember.fieldOfStudies.forEach { it ->
            fieldOfStudiesIds.add(it.id)
        }

        newMember.transportOptions.forEach { it ->
            transportIds.add(it.id)
        }

        if (newMember.firstName != ""){
            if (newMember.phoneNumber != ""){
                if (newMember.functieOmschrijving != "" ){
                    MembersDAO(requireContext(), this).put(newMember, campusIds, fieldOfStudiesIds, transportIds)
                } else {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, R.string.noFunction, Toast.LENGTH_LONG ).show()
                    binding.btnsaveGeneralInfo.isEnabled = true
                }
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, R.string.noPhoneNumber, Toast.LENGTH_LONG ).show()
                binding.btnsaveGeneralInfo.isEnabled = true
            }
        } else {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(context, R.string.noName, Toast.LENGTH_LONG ).show()
            binding.btnsaveGeneralInfo.isEnabled = true
        }

    }

    override fun putSucces() {
        requireActivity().onBackPressed()
    }
}