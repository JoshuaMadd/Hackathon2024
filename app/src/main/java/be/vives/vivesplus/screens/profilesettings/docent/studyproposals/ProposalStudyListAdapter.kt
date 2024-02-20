package be.vives.vivesplus.screens.profilesettings.docent.studyproposals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.vives.vivesplus.databinding.StudyProposalRowBinding
import be.vives.vivesplus.model.StudyProposal

class ProposalStudyListAdapter (private val deletePropListener: DeleteProposalListener, private val addPropListener: AddProposalListener) : ListAdapter<StudyProposal, ProposalStudyListAdapter.ViewHolder>(
    ProposalDiffCallback()
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, deletePropListener, addPropListener)
    }

    class ViewHolder private constructor(val binding : StudyProposalRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(studyProposal: StudyProposal, deletePropListener: DeleteProposalListener, addPropListener: AddProposalListener){
            binding.studyProp = studyProposal
            binding.deleteListener = deletePropListener
            binding.addListener = addPropListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StudyProposalRowBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class ProposalDiffCallback : DiffUtil.ItemCallback<StudyProposal>(){
    override fun areItemsTheSame(oldItem: StudyProposal, newItem: StudyProposal): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: StudyProposal, newItem: StudyProposal): Boolean {
        return oldItem.id == newItem.id
    }
}

class DeleteProposalListener(val deleteListener: (studyProposal : StudyProposal) -> Unit){
    fun onDelete(studyProposal: StudyProposal) = deleteListener(studyProposal)
}

class AddProposalListener(val addListener: (studyProposal : StudyProposal) -> Unit){
    fun onAdd(studyProposal: StudyProposal) = addListener(studyProposal)
}