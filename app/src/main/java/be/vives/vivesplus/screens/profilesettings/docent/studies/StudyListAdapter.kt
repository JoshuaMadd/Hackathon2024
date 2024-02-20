package be.vives.vivesplus.screens.profilesettings.docent.studies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.vives.vivesplus.databinding.StudyRowBinding
import be.vives.vivesplus.model.Study
import be.vives.vivesplus.util.StudyDiffCallback

class StudyListAdapter (val deleteListener : DeleteListener): ListAdapter<Study, StudyListAdapter.ViewHolder>(StudyDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, deleteListener)

    }

    class ViewHolder private constructor(val binding: StudyRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(study: Study, deleteListener: DeleteListener){
            binding.study = study
            binding.deleteListener = deleteListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StudyRowBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class DeleteListener(val deleteListener : (study : Study) -> Unit){
    fun onDelete(study : Study) = deleteListener(study)
}