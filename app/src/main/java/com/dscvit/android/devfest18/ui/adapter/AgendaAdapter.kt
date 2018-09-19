package com.dscvit.android.devfest18.ui.adapter

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.utils.getColourId
import com.dscvit.android.devfest18.utils.getResId
import com.dscvit.android.devfest18.utils.inflate
import kotlinx.android.synthetic.main.item_agenda_dark.view.*
import kotlinx.android.synthetic.main.item_agenda_light.view.*

class AgendaAdapter(
        private val events: Array<String>,
        private val timings: Array<String>,
        private val icons: Array<String>,
        private val colours: Array<String>
) : RecyclerView.Adapter<AgendaViewHolder>(){

    private val darkThemeColours = listOf("indigo", "indigo_dark")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AgendaViewHolder(parent.inflate(R.layout.item_agenda_light))

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) = holder.bind(
            events[position],
            timings[position],
            icons[position],
            colours[position]
    )
}

class AgendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(event: String, timing: String, icon: String, colour: String) = with(itemView) {
        val lightColour = colour+"_200"
        val darkColour = colour+"_800"
//        layout_agenda_light.setBackgroundColor(lightColour.getColourId(context))
        image_agenda_light_icon.setImageResource(icon.getResId(context))
        ImageViewCompat.setImageTintList(image_agenda_light_icon, ColorStateList.valueOf(lightColour.getColourId(context)))
        text_agenda_light_event.text = event
        text_agenda_light_timing.text = timing
    }
}