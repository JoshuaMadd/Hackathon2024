package be.vives.vivesplus.screens.profilesettings.docent.searchstudies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import be.vives.vivesplus.databinding.StudySearchRowBinding
import be.vives.vivesplus.model.Study
import be.vives.vivesplus.util.StudyDiffCallback

class SearchStudyAdapter(val addListener : AddListener) : ListAdapter<Study, SearchStudyAdapter.ViewHolder>(StudyDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, addListener)
    }

    class ViewHolder private constructor(val binding : StudySearchRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(study: Study, addListener: AddListener){
            binding.study = study
            binding.addListener = addListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StudySearchRowBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class AddListener(val addListener : (study : Study) -> Unit){
    fun onAdd(study : Study) = addListener(study)
}