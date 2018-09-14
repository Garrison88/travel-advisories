package com.thomas.garrison.traveladvisories

import android.arch.lifecycle.LiveData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thomas.garrison.traveladvisories.database.Trip
import kotlinx.android.synthetic.main.card_layout.view.*

class TripAdapter (private val tripItemList: List<Trip>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_layout, parent, false)
        return TripViewHolder(view)
    }

    override fun getItemCount() = tripItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TripViewHolder).bind(tripItemList[position])
    }

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(trip: Trip) {
            itemView.rv_tv_country.text = trip.country
            itemView.rv_tv_start_date.text = trip.startDate
            itemView.rv_tv_end_date.text = trip.endDate
        }
    }
}

//class TripAdapter (
//        private val tripList: List<Trip>,
//        private val listener: (Trip) -> Unit
//): RecyclerView.Adapter<TripAdapter.TripHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TripHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false))
//
//    override fun onBindViewHolder(holder: TripHolder, position: Int) = holder.bind(tripList[position], listener)
//
//    override fun getItemCount() = tripList.size
//
//    class TripHolder(tripView: View): RecyclerView.ViewHolder(tripView) {
//
//        fun bind(trip: Trip, listener: (Trip) -> Unit) = with(itemView) {
//            rv_tv_country.text = trip.country
//            rv_tv_start_date.text = trip.startDate
//            rv_tv_end_date.text = trip.endDate
//            setOnClickListener { listener(trip) }
//        }
//    }
//}