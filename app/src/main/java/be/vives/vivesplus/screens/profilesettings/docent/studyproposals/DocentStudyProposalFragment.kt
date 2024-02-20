package be.vives.vivesplus.screens.profilesettings.docent.studyproposals

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import be.vives.vivesplus.R
import be.vives.vivesplus.databinding.FragmentDocentStudyProposalBinding
import be.vives.vivesplus.model.StudyProposal

class DocentStudyProposalFragment : Fragment() {

    private lateinit var binding : FragmentDocentStudyProposalBinding
    private lateinit var viewModel: DocentStudyProposalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_docent_study_proposal, container, false)

        val application = requireNotNull(this.activity).application
        val factory = DocentStudyProposalViewModelFactory(application, requireContext())
        val arguments = DocentStudyProposalFragmentArgs.fromBundle(requireArguments())

        viewModel = ViewModelProvider(this, factory).get(DocentStudyProposalViewModel::class.java)

        binding.viewModel = viewModel

        val proposalListAdapter = ProposalStudyListAdapter(DeleteProposalListener {
            deleteProposalFromList(it)
        }, AddProposalListener {
            addProposalToStudies(it)
        })

        binding.proposalList.layoutManager = LinearLayoutManager(context)
        binding.proposalList.adapter = proposalListAdapter

        viewModel.proposals.value!!.addAll(arguments.proposalList.toMutableList())

        setObservers(proposalListAdapter)

        return binding.root
    }

    private fun setObservers(proposalListAdapter: ProposalStudyListAdapter){
        viewModel.proposals.observe(viewLifecycleOwner, Observer {
            if(it != null){
                proposalListAdapter.submitList(it)
                (binding.proposalList.adapter as ProposalStudyListAdapter).notifyDataSetChanged()
            }
        })

        viewModel.deleteProposal.observe(viewLifecycleOwner, Observer {
            it?.let{
                (binding.proposalList.adapter as ProposalStudyListAdapter).notifyDataSetChanged()
                viewModel.onDeleteStudyProposalFinished()
            }
        })

        viewModel.addProposal.observe(viewLifecycleOwner, Observer {
            it?.let{
                (binding.proposalList.adapter as ProposalStudyListAdapter).notifyDataSetChanged()
                viewModel.onAddStudyProposalFinished()
            }
        })
    }

    private fun deleteProposalFromList(studyProposal: StudyProposal){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.zeker))
        builder.setMessage(getString(R.string.sure_decline_study_proposal))

        builder.setPositiveButton(getString(R.string.yes_decline_proposal)){ _,_ ->
            viewModel.onDeleteStudyProposalClicked(studyProposal)
            viewModel.proposals.value!!.remove(studyProposal)
        }

        builder.setNegativeButton(getString(R.string.annuleer)){ _, _ -> }

        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

    private fun addProposalToStudies(studyProposal: StudyProposal){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.zeker))
        builder.setMessage(getString(R.string.sure_accept_proposal))

        builder.setPositiveButton(getString(R.string.yes_accept_proposal)){ _, _ ->
            viewModel.onAddStudyProposalClicked(studyProposal)
            viewModel.proposals.value!!.remove(studyProposal)
        }

        builder.setNegativeButton(getString(R.string.annuleer)){ _, _ -> }

        val dialog : AlertDialog = builder.create()
        dialog.show()
    }

}