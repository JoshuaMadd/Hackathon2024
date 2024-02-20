package be.vives.vivesplus.screens.traffic.train

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Connection

class TrainAdapter(val data: ArrayList<Connection>, val context: Context): RecyclerView.Adapter<TrainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.connection_row, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = "${data[position].departure.station} - ${data[position].arrival.station}"

        if(data[position].overstaps.size == 0) {
            holder.overstappen.visibility = View.GONE
        }
        else {
            if(data[position].overstaps.size == 1) {
                holder.overstappen.text = "${data[position].overstaps.size}" + context.getString(R.string.overstap)
            }
            else {
                holder.overstappen.text = "${data[position].overstaps.size}" + context.getString(R.string.overstappen)
            }
        }

        holder.times.text = "${data[position].departure.time} - ${data[position].arrival.time}"

        if(data[position].departure.delay == "0") {
            holder.delay.visibility = View.GONE
        }
        holder.delay.text ="+${data[position].departure.delay} "+ context.getString(R.string.min_vertraging)

        holder.perron.text =  context.getString(R.string.perron) + " ${data[position].departure.perron}"

        holder.container.setOnClickListener {
            buildInfoBox(data[position])
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val times: TextView = itemView.findViewById(R.id.times)
        val delay: TextView = itemView.findViewById(R.id.delay)
        val perron: TextView = itemView.findViewById(R.id.perron)
        val overstappen: TextView = itemView.findViewById(R.id.overstappen)
        val container: LinearLayout = itemView.findViewById(R.id.container)
    }

    private fun buildInfoBox(obj: Connection) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Info")

        var txt = ""

        if(obj.overstaps.size != 0) {
            txt += "<b><u>" + context.getString(R.string.OVERSTAPPEN) + "</u></b><br/>"
            for (i in 0 until obj.overstaps.size) {
                txt += "${obj.overstaps[i].station}<br/>"
                txt +=   context.getString(R.string.perron) + "${obj.overstaps[i].arrivalPerron} - " + context.getString(R.string.perron)+ "${obj.overstaps[i].departurePerron}<br/>"
                txt += obj.overstaps[i].time + context.getString(R.string.tijd_overstap) + "<br/><br/>"
            }
        }

        txt += "<b><u>STOPS</u></b><br/>"

        txt += "<b>${obj.departure.station}</b><br/>"
        txt += "<b>${obj.departure.time}</b>"
        txt += "<br/><br/>"

        for (i in 0 until obj.stops.size) {
            txt += obj.stops[i].station
            txt += "<br/>"
            txt += obj.stops[i].time
            if(obj.stops[i].delay != "0")
                txt += " - +${obj.stops[i].delay} min"
            txt += "<br/><br/>"
        }

        txt += "<b>${obj.arrival.station}</b><br/>"
        txt += "<b>${obj.arrival.time}</b>"

        builder.setMessage(Html.fromHtml(txt,0))

        builder.setPositiveButton("Close"){_,_ -> }
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }
}