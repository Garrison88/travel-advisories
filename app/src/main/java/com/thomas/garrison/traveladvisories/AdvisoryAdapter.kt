package com.thomas.garrison.traveladvisories

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.advisory_card_layout.view.*

class AdvisoryAdapter (private val advisoryItemList: List<Advisory>?, private val clickListener: (Advisory) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.advisory_card_layout, parent, false)

        return AdvisoryViewHolder(view)
    }

    override fun getItemCount() = advisoryItemList!!.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AdvisoryViewHolder).bind(advisoryItemList!![position], clickListener)
    }

    class AdvisoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(advisory: Advisory, clickListener: (Advisory) -> Unit) {

            itemView.advisory_rv_country.text = advisory.country
            itemView.setOnClickListener { clickListener(advisory) }
        }
    }
}