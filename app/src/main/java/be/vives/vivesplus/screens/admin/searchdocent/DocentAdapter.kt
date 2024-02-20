package be.vives.vivesplus.screens.admin.searchdocent

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Member
import be.vives.vivesplus.util.PreferencesManager
import com.squareup.picasso.Picasso

class DocentAdapter (val data: ArrayList<Member>, val context: Context): RecyclerView.Adapter<DocentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.docentsearch_row, parent, false)
    return ViewHolder(view)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val docentName: TextView = itemView.findViewById(R.id.docentName)
        val row: LinearLayout = itemView.findViewById(R.id.row)
        val imageView: ImageView = itemView.findViewById(R.id.docentImage)
    }

    override fun getItemCount() = data.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.docentName.text = "${data[position].lastName} ${data[position].firstName}"
        if(data[position].photoUrl != null) {
            Picasso.with(context)
                .load(data[position].photoUrl)
                .error(R.drawable.ic_person_8c8c8c_64dp)
                .resize(150,150)
                .into(holder.imageView)
        } else {
            Picasso.with(context)
                .load(R.drawable.ic_person_8c8c8c_64dp)
                .into(holder.imageView)
        }


        holder.row.setOnClickListener {
            val memberToBeSearchedKulNumber = data[position].kulNumber
            PreferencesManager().writeStringToPreferences(context, context.getString(R.string.docent_to_be_searched_kul_number), memberToBeSearchedKulNumber)
            it.findNavController().navigate(SearchDocentFragmentDirections.actionSearchDocentFragmentToSearchDocentDetailFragment())
        }
    }
}