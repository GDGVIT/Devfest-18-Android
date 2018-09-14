package com.dscvit.android.devfest18.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.utils.getColourId
import com.dscvit.android.devfest18.utils.getResId
import com.dscvit.android.devfest18.utils.inflate
import kotlinx.android.synthetic.main.item_agenda_dark.view.*
import kotlinx.android.synthetic.main.item_agenda_light.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundColorResource

class AgendaAdapter(
        private val events: Array<String>,
        private val timings: Array<String>,
        private val icons: Array<String>,
        private val colours: Array<String>
) : RecyclerView.Adapter<AgendaViewHolder>(){

    private val darkThemeColours = listOf("indigo", "indigo_dark")

    override fun getItemViewType(position: Int)= if (colours[position] in darkThemeColours) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if(viewType == 0)
        AgendaViewHolder(parent.inflate(R.layout.item_agenda_dark))
     else
        AgendaViewHolder(parent.inflate(R.layout.item_agenda_light))

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
        if (itemViewType == 0) {
            layout_agenda_dark.setBackgroundColor(colour.getColourId(context))
            image_agenda_dark_icon.setImageResource(icon.getResId(context))
            text_agenda_dark_event.text = event
            text_agenda_dark_timing.text = timing
        } else {
            layout_agenda_light.setBackgroundColor(colour.getColourId(context))
            image_agenda_light_icon.setImageResource(icon.getResId(context))
            text_agenda_light_event.text = event
            text_agenda_light_timing.text = timing
        }
    }
}