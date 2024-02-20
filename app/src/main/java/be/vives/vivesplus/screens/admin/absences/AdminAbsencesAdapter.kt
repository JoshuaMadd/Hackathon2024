package be.vives.vivesplus.screens.admin.absences

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import be.vives.vivesplus.R
import be.vives.vivesplus.dao.AdminAbsencesDAO
import be.vives.vivesplus.dao.AdminAbsencesDAOCallback
import be.vives.vivesplus.model.AdminAbsence
import be.vives.vivesplus.util.DateTimeHelper
import java.time.LocalDateTime

class AdminAbsencesAdapter(val data: ArrayList<AdminAbsence>, val context: Context, val fragment: AdminAbsencesFragment): RecyclerView.Adapter<AdminAbsencesAdapter.ViewHolder>(),
    AdminAbsencesDAOCallback {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.admin_absences_row, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.van.text = context.getString(R.string.van) + " ${DateTimeHelper().formatDateTimeToFullString(data[position].start)}"
        holder.tot.text = context.getString(R.string.tot) + " ${DateTimeHelper().formatDateTimeToFullString(data[position].end)}"

        holder.delete.setOnClickListener { delete(position) }
        holder.edit.setOnClickListener{ edit(data[position].id, data[position].end, data[position].start) }
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val van: TextView = itemView.findViewById(R.id.van)
        val tot: TextView = itemView.findViewById(R.id.tot)
        val delete: ImageView = itemView.findViewById(R.id.delete)
        val edit: ImageView = itemView.findViewById(R.id.edit)
    }

    private fun delete(position: Int) {
        if(data[position].start.isBefore(LocalDateTime.now())) {
            Toast.makeText(context, context.getString(R.string.absences_change), Toast.LENGTH_LONG).show()
        } else {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.zeker))
            builder.setMessage(context.getString(R.string.delete_absence))

            builder.setPositiveButton(context.getString(R.string.verwijder)) { _,_ ->
                AdminAbsencesDAO(context, this).delete(data[position].id)
                data.removeAt(position)
                notifyDataSetChanged()
            }
            builder.setNegativeButton(context.getString(R.string.annuleer)) { _,_ -> }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun edit(id: Int, end: LocalDateTime, start: LocalDateTime) {
        if(end.isBefore(LocalDateTime.now())) {
            Toast.makeText(context, context.getString(R.string.change_absence_past), Toast.LENGTH_LONG).show()

        } else if ( start.isBefore(LocalDateTime.now())){
            Toast.makeText(context, "current absences can not be edited", Toast.LENGTH_LONG).show()
        } else {
            fragment.edit(id)
            
        }
    }

    override fun dataLoaded(absences: ArrayList<AdminAbsence>) {}

    override fun postOrPutSuccesful() {

    }
}