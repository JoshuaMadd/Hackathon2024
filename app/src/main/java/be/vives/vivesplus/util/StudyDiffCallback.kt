package be.vives.vivesplus.util

import androidx.recyclerview.widget.DiffUtil
import be.vives.vivesplus.model.Study

class StudyDiffCallback : DiffUtil.ItemCallback<Study>(){
    override fun areContentsTheSame(oldItem: Study, newItem: Study): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areItemsTheSame(oldItem: Study, newItem: Study): Boolean {
        return oldItem == newItem
    }

}