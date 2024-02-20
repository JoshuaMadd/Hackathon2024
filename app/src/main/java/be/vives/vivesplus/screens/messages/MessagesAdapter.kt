package be.vives.vivesplus.screens.messages

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Message
import java.time.LocalDateTime
import java.util.*

class MessagesAdapter(val data: ArrayList<Message>,val context: Context, private val listener: onItemClickListener): RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.message_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val categoryIcon = data[position].categorieIcon.replace("-", "_").plus("_solid")

        val id = context.resources.getIdentifier(categoryIcon, "string", context.packageName)

        holder.category.text = context.getString(id)
        holder.subject.text = data[position].subject

        holder.discription.text = data[position].description.take(100).plus("").replace("\n", " ")

        val difference = (LocalDateTime.now().dayOfYear - data[position].viewDate.dayOfYear)

        if(difference == 0)
        {
            if(data[position].viewDate.minute == 0)
            {
                holder.time.text = data[position].viewDate.dayOfMonth.toString().plus(" ").plus(data[position].viewDate.month.name.take(3)
                    .lowercase(Locale.ROOT)).plus(".").plus(" ").plus(data[position].viewDate.hour.toString()).plus(":").plus(data[position].viewDate.minute).plus("0")
                    .lowercase(Locale.ROOT)
            } else {

                holder.time.text = data[position].viewDate.dayOfMonth.toString().plus(" ").plus(data[position].viewDate.month.name.take(3)
                    .lowercase(Locale.ROOT)).plus(".").plus(" ").plus(data[position].viewDate.hour.toString()).plus(":").plus(data[position].viewDate.minute)
            }
        } else{

            holder.time.text = data[position].viewDate.dayOfMonth.toString().plus(" ").plus(data[position].viewDate.month.name.take(3)
                .lowercase(Locale.ROOT)).plus(".")


        }



        if (data[position].priority.lowercase(Locale.ROOT) == "high")
        {
            holder.uitroepteken.visibility = View.VISIBLE
            holder.uitroepteken.setTextColor(Color.parseColor("#FF1E1E"))
        }

    }

    override fun getItemCount() = data.size

    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {
        val uitroepteken: TextView = itemview.findViewById(R.id.uitroepteken)
        val category: TextView = itemview.findViewById(R.id.categoryIcon)
        val subject: TextView = itemview.findViewById(R.id.onderwerp)
        val discription: TextView = itemview.findViewById(R.id.beschrijving)
        val time: TextView = itemview.findViewById(R.id.creatieTijd)

        init {
            itemview.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val message = data[position]
            if(position!= RecyclerView.NO_POSITION){
                listener.onItemClick(message)
            }
        }
    }

    interface onItemClickListener{
        fun onItemClick(message: Message)
    }



}