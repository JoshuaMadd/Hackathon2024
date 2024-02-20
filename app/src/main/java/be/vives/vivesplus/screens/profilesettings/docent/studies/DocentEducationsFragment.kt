package be.vives.vivesplus.screens.profilesettings.docent.studies

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentDocentEducationsBinding
import be.vives.vivesplus.model.Study

class DocentEducationsFragment : Fragment() {

    private lateinit var binding : FragmentDocentEducationsBinding
    private lateinit var viewModel : DocentEducationsViewModel
    private var isFabMenuOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_docent_educations, container, false)
        val application = requireNotNull(this.activity).application
        val factory = DocentEducationsViewModelFactory(application, requireContext())
        viewModel = ViewModelProvider(this, factory).get(DocentEducationsViewModel::class.java)

        binding.viewModel = viewModel

        val studyListAdapter = StudyListAdapter(DeleteListener {
            deleteStudyFromList(it)
        })

        binding.studiesList.layoutManager = LinearLayoutManager(activity)
        binding.studiesList.adapter = studyListAdapter

        setObservers(studyListAdapter)

        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStudies()
        viewModel.getStudyProposals()
    }

    override fun onPause() {
        super.onPause()
        if(isFabMenuOpen){
            closeFabMenu()
        }
    }

    private fun setObservers(studyListAdapter : StudyListAdapter){
        viewModel.studies.observe(viewLifecycleOwner, Observer() {if(it != null){
                studyListAdapter.submitList(it)
                (binding.studiesList.adapter as StudyListAdapter).notifyDataSetChanged()
            }
            checkListStudies()
        })

        viewModel.deleteStudy.observe(viewLifecycleOwner, Observer {
            it?.let{
                (binding.studiesList.adapter as StudyListAdapter).notifyDataSetChanged()
                viewModel.onDeleteStudyClickedFinished()
            }
            checkListStudies()
        })

        viewModel.proposals.observe(viewLifecycleOwner, Observer {
            if(it != null){
                binding.fabProposals.extend()
            }
            if(it.isEmpty()){
                viewModel.setManagePropsClickableFalse()
            }else{
                viewModel.setManagePropsClickableTrue()
            }
        })

        viewModel.fabClicked.observe(viewLifecycleOwner, Observer {
            if(it){
                if(!isFabMenuOpen){
                    showFabMenu()
                } else {
                    closeFabMenu()
                }
                viewModel.onFabClickedFinished()
            }
        })

        viewModel.fabSearchClicked.observe(viewLifecycleOwner, Observer {
            if(it){
                closeFabMenu()
                this.findNavController().navigate(DocentEducationsFragmentDirections.actionDocentEductionsFragmentToDocentSearchStudiesFragment())
                viewModel.onFabSearchClickedFinished()
            }
        })

        viewModel.fabManageProposalsClicked.observe(viewLifecycleOwner, Observer {
            if(it && viewModel.isManagePropsClickable.value!!){
                closeFabMenu()
                this.findNavController().navigate(DocentEducationsFragmentDirections.actionDocentEductionsFragmentToDocentStudyProposalFragment(viewModel.proposals.value!!.toTypedArray()))
                viewModel.onFabManageProposalsClickedFinished()
            }else if (it){
                Toast.makeText(requireContext(), getString(R.string.no_proposals_not_clickable), Toast.LENGTH_SHORT).show()
                viewModel.onFabManageProposalsClickedFinished()
            }
        })
    }

    private fun deleteStudyFromList(study: Study){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.zeker))
        builder.setMessage(getString(R.string.sure_delete_study))

        builder.setPositiveButton(getString(R.string.yes_delete_study)) { _, _ ->
            viewModel.onDeleteStudyClicked(study)
            viewModel.studies.value!!.remove(study)
        }

        builder.setNegativeButton(getString(R.string.annuleer)){ _, _ -> }

        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

    private fun checkListStudies(){
        if(viewModel.studies.value.isNullOrEmpty()){
            binding.noStudies.visibility = View.VISIBLE
        } else {
            binding.noStudies.visibility = View.INVISIBLE
        }
    }

    private fun showFabMenu(){
        isFabMenuOpen = true
        binding.fabProposals.shrink()
        binding.fabProposals.rotation+=45F

        binding.fabAddProposals.visibility = View.VISIBLE
        binding.fabAddProposals.animate().translationY(-resources.getDimension(R.dimen.standard_70))

        binding.fabLabelAddProposal.animate().translationY(-resources.getDimension(R.dimen.standard_90))
        binding.fabLabelAddProposal.visibility = View.VISIBLE

        binding.fabSearchStudies.visibility = View.VISIBLE
        binding.fabSearchStudies.animate().translationY(-resources.getDimension(R.dimen.standerd_140))

        binding.fabLabelSearchStudy.animate().translationY(-resources.getDimension(R.dimen.standard_160))
        binding.fabLabelSearchStudy.visibility = View.VISIBLE
    }

    private fun closeFabMenu(){
        isFabMenuOpen = false
        binding.fabProposals.extend()
        binding.fabProposals.rotation-=45F

        binding.fabAddProposals.animate().translationY(0F)
        binding.fabAddProposals.visibility = View.GONE

        binding.fabLabelAddProposal.visibility = View.GONE
        binding.fabLabelAddProposal.animate().translationY(0F)

        binding.fabSearchStudies.animate().translationY(0F)
        binding.fabSearchStudies.visibility = View.GONE

        binding.fabLabelSearchStudy.visibility = View.GONE
        binding.fabLabelSearchStudy.animate().translationY(0F)

    }
}