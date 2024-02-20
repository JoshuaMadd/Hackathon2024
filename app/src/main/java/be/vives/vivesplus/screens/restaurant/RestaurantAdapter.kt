package be.vives.vivesplus.screens.restaurant

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.vives.vivesplus.R
import be.vives.vivesplus.model.Restaurant

class RestaurantAdapter (val context: Context, val fragment: RestaurantFragment, val data: ArrayList<Restaurant>): RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.resto_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].name
        if (data[position].description == null) {
            holder.description.visibility = View.GONE
        }
        else {
            holder.description.text = data[position].description
            holder.description.setOnClickListener { showDesc(data[position]) }
        }

        if (data[position].link == null) {
            holder.link.visibility = View.GONE
        }
        else {
            holder.link.setOnClickListener { fragment.navigateToWebView(data[position].link!!)}
            holder.moreButtonText.text = data[position].moreButtonText
        }

        if (data[position].openingsInfo == null) {
            holder.openingsInfo.visibility =  View.GONE
        }
        else {
            holder.openingsInfo.text = data[position].openingsInfo
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.descriptionRestaurant)
        val link: RelativeLayout = itemView.findViewById(R.id.link)
        val moreButtonText: TextView = itemView.findViewById(R.id.moreButtonText)
        val openingsInfo: TextView = itemView.findViewById(R.id.openingsInfo)
    }

    private fun showDesc(obj: Restaurant) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Restaurant info")
        builder.setIcon(R.drawable.ic_info_8c8c8c_24dp)
        builder.setMessage(obj.description)
        builder.setPositiveButton("Close") {_,_ -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
