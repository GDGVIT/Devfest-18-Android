package com.dscvit.android.devfest18.ui.agenda

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.di.Injectable
import com.dscvit.android.devfest18.ui.adapter.AgendaAdapter
import kotlinx.android.synthetic.main.fragment_agenda.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class AgendaFragment : Fragment(), Injectable {

    companion object {
        fun newInstance() = AgendaFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_agenda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(rv_6oct) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = AgendaAdapter(
                    resources.getStringArray(R.array.agenda_6_oct_events),
                    resources.getStringArray(R.array.agenda_6_oct_timings),
                    resources.getStringArray(R.array.agenda_6_oct_icons),
                    resources.getStringArray(R.array.agenda_6_oct_colors)
            )
        }

        with(rv_7oct) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = AgendaAdapter(
                    resources.getStringArray(R.array.agenda_7_oct_events),
                    resources.getStringArray(R.array.agenda_7_oct_timings),
                    resources.getStringArray(R.array.agenda_7_oct_icons),
                    resources.getStringArray(R.array.agenda_7_oct_colors)
            )
        }
    }
}