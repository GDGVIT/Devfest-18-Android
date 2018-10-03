package com.dscvit.android.devfest18.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.model.Sponsor

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by abhis on 9/14/2017.
 */

class SponsorAdapter(private val sponsorData: List<Sponsor>) : RecyclerView.Adapter<SponsorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_sponsor_gallery, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.cropToPadding = true
        Glide.with(holder.itemView.context)
                .load(sponsorData[position].sponsorImage)
                .into(holder.image)

    }

    override fun getItemCount(): Int {
        return sponsorData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById<View>(R.id.image) as ImageView
    }
}
