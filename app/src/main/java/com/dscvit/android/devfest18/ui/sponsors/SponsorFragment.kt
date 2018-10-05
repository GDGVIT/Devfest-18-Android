package com.dscvit.android.devfest18.ui.sponsors

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.yarolegovich.discretescrollview.Orientation
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.model.Sponsor
import com.dscvit.android.devfest18.ui.adapter.SponsorAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter
import com.yarolegovich.discretescrollview.transform.ScaleTransformer

import java.util.Arrays

import androidx.annotation.Nullable
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import saschpe.android.customtabs.CustomTabsHelper
import saschpe.android.customtabs.WebViewFallback


class SponsorFragment : androidx.fragment.app.Fragment(), DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>, View.OnClickListener {

    private lateinit var rootView: View

    private var currentSponsorName: TextView? = null
    private var currentSponsorType: TextView? = null
    private var previousSponsorButton: ImageView? = null
    private var nextSponsorButton: ImageView? = null
    private var websiteButton: FloatingActionButton? = null
    private var itemPicker: DiscreteScrollView? = null
    private var infiniteAdapter: InfiniteScrollAdapter<*>? = null

    private var sponsorData: List<Sponsor>? = null

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        rootView = inflater!!.inflate(R.layout.fragment_sponsor, container, false)

        sponsorData = Arrays.asList(
                Sponsor(".tech DOMAINS", "Sponsor", "http://get.tech/", R.drawable.logo_dottech),
                Sponsor("balsamiq", "Sponsor", "https://balsamiq.com/", R.drawable.logo_balsmiq),
                Sponsor("Pixectra", "Sponsor", "http://www.pixectra.com/", R.drawable.logo_p),
                Sponsor("Decoder", "Sponsor", "url", R.drawable.logo_decoders),
                Sponsor("The Souled Store", "Sponsor", "https://www.thesouledstore.com/", R.drawable.logo_thesouledstore),
                Sponsor("CNCF", "Sponsor", "https://www.cncf.io/", R.drawable.logo_cncf),
                Sponsor("Khronos", "Sponsor", "https://www.khronos.org/", R.drawable.logo_khronos),
                Sponsor("Agora", "Sponsor", "https://www.agora.io/en/", R.drawable.logo_agora),
                Sponsor("Snlang Labs", "Sponsor", "https://slanglabs.in/", R.drawable.logo_slanglabs),
                Sponsor("Sketch", "Sponsor", "https://www.sketchapp.com/", R.drawable.logo_sketch),
                Sponsor("LBRY", "Sponsor", "https://lbry.io/", R.drawable.logo_lbry),
                Sponsor("dev.to", "Sponsor", "https://lbry.io/", R.drawable.logo_devto),
                Sponsor("Travis", "Sponsor", "https://travis-ci.org/", R.drawable.logo_travis),
                Sponsor("Bugsee", "Sponsor", "https://www.bugsee.com/", R.drawable.logo_bugsee),
                Sponsor("Creative Tim", "Sponsor", "https://www.creative-tim.com/", R.drawable.logo_creativetim),
                Sponsor("Yocto Project", "Sponsor", "https://www.yoctoproject.org/", R.drawable.logo_yocto),
                Sponsor("Estimote", "Sponsor", "https://estimote.com/", R.drawable.logo_estimo),
                Sponsor("hackerearth", "Sponsor", "https://hackerearth.com", R.drawable.logo_hackearth),
                Sponsor("JetBrains", "Sponsor", "https://www.jetbrains.com", R.drawable.logo_jetbrains),
                Sponsor("OpenSUSE", "Sponsor", "https://www.opensuse.org/", R.drawable.logo_opensuse)

                //                new Sponsor("TECHGIG", "Platform Sponsor", "https://www.techgig.com/", R.drawable.platform_sponsor),
                //                new Sponsor("Google Developers", "Sponsor", "https://developers.google.com/", R.drawable.sponsor_two),
                //                new Sponsor("GitLab", "Sponsor", "https://gitlab.com/", R.drawable.sponsor_three),
                //                new Sponsor("Skcript", "Sponsor", "https://skcript.com", R.drawable.sponsor_four),
                //                new Sponsor("iconscout", "Sponsor", "https://iconscout.com/", R.drawable.sponsor_seven),
                //                new Sponsor("todoist", "Sponsor", "https://en.todoist.com/", R.drawable.sponsor_eight),
                //                new Sponsor("doselect", "Sponsor", "https://doselect.com/", R.drawable.sponsor_nine),
                //                new Sponsor("docker", "Sponsor", "https://www.docker.com/", R.drawable.sponsor_ten),
                //                new Sponsor("npm", "Sponsor", "https://www.npmjs.com/", R.drawable.sponsor_eleven),
        )

        currentSponsorName = rootView!!.findViewById<View>(R.id.sponsor_name) as TextView
        currentSponsorType = rootView!!.findViewById<View>(R.id.sponsor_type) as TextView
        previousSponsorButton = rootView!!.findViewById<View>(R.id.previous_sponsor_button) as ImageView
        nextSponsorButton = rootView!!.findViewById<View>(R.id.next_sponsor_button) as ImageView
        websiteButton = rootView!!.findViewById<View>(R.id.sponsor_website_button) as FloatingActionButton

        itemPicker = rootView!!.findViewById<View>(R.id.item_picker) as DiscreteScrollView
        itemPicker!!.setOrientation(Orientation.HORIZONTAL)
        itemPicker!!.addOnItemChangedListener(this)
        infiniteAdapter = InfiniteScrollAdapter.wrap(SponsorAdapter(sponsorData!!))
        itemPicker!!.adapter = infiniteAdapter
        itemPicker!!.setItemTransitionTimeMillis(150)
        itemPicker!!.setItemTransformer(ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build())

        onItemChanged(sponsorData!![0])

        rootView!!.findViewById<View>(R.id.sponsor_name).setOnClickListener(this)
        rootView!!.findViewById<View>(R.id.sponsor_type).setOnClickListener(this)
        rootView!!.findViewById<View>(R.id.previous_sponsor_button).setOnClickListener(this)
        rootView!!.findViewById<View>(R.id.next_sponsor_button).setOnClickListener(this)
        rootView!!.findViewById<View>(R.id.sponsor_website_button).setOnClickListener(this)

        itemPicker!!.setSlideOnFling(true)
        itemPicker!!.setSlideOnFlingThreshold(2100)

        return rootView as View
    }

    override fun onClick(v: View) {
        var realPosition = 0
        lateinit var current: Sponsor
        when (v.id) {
            R.id.sponsor_website_button -> {
                realPosition = infiniteAdapter!!.getRealPosition(itemPicker!!.currentItem)
                current = sponsorData!![realPosition]

                val customTabsIntent = CustomTabsIntent.Builder()
                        .addDefaultShareMenuItem()
                        .setToolbarColor(ContextCompat.getColor(rootView.context, R.color.colorPrimary))
                        .setShowTitle(true)
                        //                        .setCloseButtonIcon(backArrow)
                        .build()

                CustomTabsHelper.addKeepAliveExtra(rootView!!.context, customTabsIntent.intent)

                CustomTabsHelper.openCustomTab(rootView!!.context, customTabsIntent,
                        Uri.parse(current.sponsorWebsite),
                        WebViewFallback())
            }
            R.id.previous_sponsor_button -> {
                realPosition = infiniteAdapter!!.getRealPosition(itemPicker!!.currentItem)
                current = sponsorData!![realPosition]
                smoothScrollToPreviousPosition(itemPicker!!, realPosition)
            }
            R.id.next_sponsor_button -> {
                realPosition = infiniteAdapter!!.getRealPosition(itemPicker!!.currentItem)
                current = sponsorData!![realPosition]
                smoothScrollToNextPosition(itemPicker!!, realPosition)
            }
            else -> {
            }
        }
    }

    private fun onItemChanged(sponsor: Sponsor) {
        currentSponsorName!!.text = sponsor.sponsorName
        currentSponsorType!!.text = sponsor.sponsorType
    }

    override fun onCurrentItemChanged(viewHolder: RecyclerView.ViewHolder?, adapterPosition: Int) {
        val positionInDataSet = infiniteAdapter!!.getRealPosition(adapterPosition)
        onItemChanged(sponsorData!!.get(positionInDataSet))
    }

    companion object {

        fun newInstance(): androidx.fragment.app.Fragment {
            return SponsorFragment()
        }

        fun smoothScrollToNextPosition(scrollView: DiscreteScrollView, pos: Int) {
            val adapter = scrollView.adapter
            var destination = pos + 1
            if (adapter is InfiniteScrollAdapter<*>) {
                destination = adapter.getClosestPosition(destination)
            }
            scrollView.smoothScrollToPosition(destination)
        }

        fun smoothScrollToPreviousPosition(scrollView: DiscreteScrollView, pos: Int) {
            val adapter = scrollView.adapter
            var destination = pos - 1
            if (adapter is InfiniteScrollAdapter<*>) {
                destination = adapter.getClosestPosition(destination)
            }
            scrollView.smoothScrollToPosition(destination)
        }
    }

}
