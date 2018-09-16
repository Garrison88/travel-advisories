package com.thomas.garrison.traveladvisories

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thomas.garrison.traveladvisories.database.Trip
import kotlinx.android.synthetic.main.card_layout.view.*

class TripAdapter (private val tripItemList: List<Trip>?) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_layout, parent, false)
        return TripViewHolder(view)
    }

    override fun getItemCount() = tripItemList!!.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TripViewHolder).bind(tripItemList!![position])
    }

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(trip: Trip) {
            itemView.rv_tv_country.text = trip.country
            itemView.rv_tv_start_date.text = trip.startDate
            itemView.rv_tv_end_date.text = trip.endDate
        }
    }
}