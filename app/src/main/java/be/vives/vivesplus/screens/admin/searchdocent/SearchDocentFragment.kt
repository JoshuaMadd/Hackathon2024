package be.vives.vivesplus.screens.admin.searchdocent

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.FieldOfStudyDAO
import be.vives.vivesplus.dao.FieldOfStudyDAOCallback
import be.vives.vivesplus.dao.MembersDAO
import be.vives.vivesplus.dao.MembersDAOCallback
import be.vives.vivesplus.databinding.FragmentSearchDocentBinding
import be.vives.vivesplus.model.FieldOfStudy
import be.vives.vivesplus.model.Member
import be.vives.vivesplus.util.PreferencesManager

class SearchDocentFragment : Fragment(), FieldOfStudyDAOCallback, MembersDAOCallback {

    private lateinit var membersAdapter: ArrayAdapter<Member>
    private lateinit var fieldOfStudyAdapter: ArrayAdapter<FieldOfStudy>
    lateinit var binding: FragmentSearchDocentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        FieldOfStudyDAO(requireContext(), this).getAll()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_docent, container, false)
        return binding.root
    }

    private fun createListener(fieldOfStudyLst: ArrayList<FieldOfStudy>) {

        binding.studiegebiedspinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val selectedObject = binding.studiegebiedspinner.selectedItem as FieldOfStudy
                    fieldOfStudyLst.removeIf { fieldOfStudy -> fieldOfStudy.id == -1 }
                    PreferencesManager().writeIntToPreferences(requireContext(), requireContext().getString(R.string.pref_field_of_study_docentlist), fieldOfStudyLst.indexOf(selectedObject))
                    binding.progressBar17.visibility = View.VISIBLE
                    MembersDAO(requireContext(), this@SearchDocentFragment).getMembersBasedOnFieldOfStudy(selectedObject.id)
                }
            }
    }

    override fun dataLoaded(fieldOfStudyLst: ArrayList<FieldOfStudy>) {
        val prefFieldOfStudyIndex = PreferencesManager().getIntFromPreferences(requireContext(), requireContext().getString(R.string.pref_field_of_study_docentlist))!!
        val originalList  = ArrayList<FieldOfStudy>(fieldOfStudyLst)


        if (prefFieldOfStudyIndex == -1 || fieldOfStudyLst.size-1 < prefFieldOfStudyIndex) {
            originalList.add(0, FieldOfStudy(-1, getString(R.string.choose_f_o_s)))
        } else {
            val prefFieldOfStudy = fieldOfStudyLst[prefFieldOfStudyIndex]
            fieldOfStudyLst.remove(prefFieldOfStudy)
            fieldOfStudyLst.add(0, prefFieldOfStudy)
        }
        createSpinnerAdapter(fieldOfStudyLst)
        createListener(originalList)
    }

    override fun dataLoaded(members: MutableList<Member>) {
        createDocentAdapter(members)
        binding.progressBar17.visibility = View.GONE
        createTextListener(members)
    }

    override fun dataLoaded(member: Member) {}

    override fun putSucces() {}

    private fun createSpinnerAdapter(fieldOfStudyLst: ArrayList<FieldOfStudy>) {
        fieldOfStudyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, fieldOfStudyLst)
        binding.studiegebiedspinner.adapter = fieldOfStudyAdapter
    }

    private fun createTextListener(members: MutableList<Member>) {
        binding.textDocent.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                changeDocentList(members)
                return@OnKeyListener true
            }
            false
        })
    }

    private fun changeDocentList(members: MutableList<Member>) {
        val searchfor = binding.textDocent.text.toString()
        val filteredMembers = members.filter { member ->
            member.firstName.contains(searchfor, true)
                    || member.lastName.contains(searchfor, true)
                    || "${member.firstName} ${member.lastName}".contains(searchfor, true)
                    || "${member.lastName} ${member.firstName}".contains(searchfor, true)
        }
        createDocentAdapter(filteredMembers as MutableList<Member>)
    }

    private fun createDocentAdapter(members: MutableList<Member>) {
        membersAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, members)
        binding.docentenList.layoutManager = LinearLayoutManager(context)
        binding.docentenList.adapter = DocentAdapter(members as ArrayList<Member>, requireContext())
    }
}


