package be.vives.vivesplus.screens.admin.absences


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import be.vives.vivesplus.R
import be.vives.vivesplus.dao.AdminAbsencesDAO
import be.vives.vivesplus.dao.AdminAbsencesDAOCallback
import be.vives.vivesplus.databinding.FragmentAdminAbsencesBinding
import be.vives.vivesplus.model.AdminAbsence

class AdminAbsencesFragment : Fragment(), AdminAbsencesDAOCallback {

    lateinit var binding: FragmentAdminAbsencesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_absences, container, false)

        binding.refresh.setOnRefreshListener { AdminAbsencesDAO(requireContext(), this).get() }
        binding.btnNew.setOnClickListener { view -> view.findNavController().navigate(R.id.action_adminAbsencesFragment_to_adminNewAbsencesFragment) }

        binding.absencesList.layoutManager = LinearLayoutManager(context)
        binding.absencesList.adapter = AdminAbsencesAdapter(ArrayList(), requireContext(), this)

        AdminAbsencesDAO(requireContext(), this).get()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.refresh.isRefreshing = true
        AdminAbsencesDAO(requireContext(), this).get()
    }

    override fun dataLoaded(absences: ArrayList<AdminAbsence>) {
        binding.refresh.isRefreshing = false

        binding.indicator.visibility = View.GONE
        if(absences.size == 0)
            binding.nofound.visibility = View.VISIBLE

        binding.absencesList.adapter = AdminAbsencesAdapter(absences, requireContext(), this)
    }

    override fun postOrPutSuccesful() {

    }

    fun edit(id: Int) {
        val bundle = bundleOf("id" to id)
        binding.root.findNavController().navigate(R.id.action_adminAbsencesFragment_to_adminNewAbsencesFragment, bundle)
    }

}
