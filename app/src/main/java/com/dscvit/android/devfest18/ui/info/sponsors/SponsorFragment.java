package com.dscvit.android.devfest18.ui.info.sponsors;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yarolegovich.discretescrollview.Orientation;
import com.dscvit.android.devfest18.R;
import com.dscvit.android.devfest18.model.Sponsor;
import com.dscvit.android.devfest18.ui.adapter.SponsorAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

public class SponsorFragment extends androidx.fragment.app.Fragment implements DiscreteScrollView.OnItemChangedListener,
        View.OnClickListener {

    private View rootView;


    private TextView currentSponsorName;
    private TextView currentSponsorType;
    private ImageView previousSponsorButton;
    private ImageView nextSponsorButton;
    private FloatingActionButton websiteButton;
    private DiscreteScrollView itemPicker;
    private InfiniteScrollAdapter infiniteAdapter;

    private List<Sponsor> sponsorData;


    public SponsorFragment() {
    }

    public static androidx.fragment.app.Fragment newInstance() {
        return new SponsorFragment();
    }

    public static void smoothScrollToNextPosition(final DiscreteScrollView scrollView, int pos) {
        final RecyclerView.Adapter adapter = scrollView.getAdapter();
        int destination = pos + 1;
        if (adapter instanceof InfiniteScrollAdapter) {
            destination = ((InfiniteScrollAdapter) adapter).getClosestPosition(destination);
        }
        scrollView.smoothScrollToPosition(destination);
    }

    public static void smoothScrollToPreviousPosition(final DiscreteScrollView scrollView, int pos) {
        final RecyclerView.Adapter adapter = scrollView.getAdapter();
        int destination = pos - 1;
        if (adapter instanceof InfiniteScrollAdapter) {
            destination = ((InfiniteScrollAdapter) adapter).getClosestPosition(destination);
        }
        scrollView.smoothScrollToPosition(destination);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sponsor, container, false);

        sponsorData = Arrays.asList(
                new Sponsor(".tech DOMAINS", "Sponsor", "http://get.tech/", R.drawable.tech_logo),
                new Sponsor("balsamiq", "Sponsor", "https://balsamiq.com/", R.drawable.balsamiq_logo),
                new Sponsor("Pixectra", "Sponsor", "http://www.pixectra.com/", R.drawable.pixectra_logo),
//                new Sponsor("Decoder", "Sponsor", "url", ...)
                new Sponsor("The Souled Store", "Sponsor", "https://www.thesouledstore.com/", R.drawable.souled_store_logo),
                new Sponsor("CNCF", "Sponsor", "https://www.cncf.io/", R.drawable.cncf_logo),
                new Sponsor("Khronos", "Sponsor", "https://www.khronos.org/", R.drawable.khronos_logo),
                new Sponsor("Agora", "Sponsor", "https://www.agora.io/en/", R.drawable.agora_logo),
                new Sponsor("Snlang Labs", "Sponsor", "https://slanglabs.in/", R.drawable.slanglabs_logo),
                new Sponsor("Sketch", "Sponsor", "https://www.sketchapp.com/", R.drawable.sketch_logo),
                new Sponsor("LBRY", "Sponsor", "https://lbry.io/", R.drawable.lbry_logo),
                new Sponsor("dev.to", "Sponsor", "https://lbry.io/", R.drawable.dev_logo),
                new Sponsor("Travis", "Sponsor", "https://travis-ci.org/", R.drawable.travis_logo),
                new Sponsor("Bugsee", "Sponsor", "https://www.bugsee.com/", R.drawable.bugsee_logo),
                new Sponsor("Creative Tim", "Sponsor", "https://www.creative-tim.com/", R.drawable.creative_tim),
                new Sponsor("Zulip", "Sponsor", "https://zulipchat.com/", R.drawable.zulip_logo),
                new Sponsor("Yocto Project", "Sponsor", "https://www.yoctoproject.org/", R.drawable.yocto_project_logo),
                new Sponsor("Estimote", "Sponsor", "https://estimote.com/", R.drawable.estimote_logo),
                new Sponsor("hackerearth", "Sponsor", "https://hackerearth.com", R.drawable.he_logo),
//                new Sponsor("JetBrains", "Sponsor", "https://www.jetbrains.com", R.drawable.jetbrains_logo),
                new Sponsor("OpenSUSE", "Sponsor", "https://www.opensuse.org/", R.drawable.suse)

//                new Sponsor("TECHGIG", "Platform Sponsor", "https://www.techgig.com/", R.drawable.platform_sponsor),
//                new Sponsor("Google Developers", "Sponsor", "https://developers.google.com/", R.drawable.sponsor_two),
//                new Sponsor("GitLab", "Sponsor", "https://gitlab.com/", R.drawable.sponsor_three),
//                new Sponsor("Skcript", "Sponsor", "https://skcript.com", R.drawable.sponsor_four),
//                new Sponsor("iconscout", "Sponsor", "https://iconscout.com/", R.drawable.sponsor_seven),
//                new Sponsor("todoist", "Sponsor", "https://en.todoist.com/", R.drawable.sponsor_eight),
//                new Sponsor("doselect", "Sponsor", "https://doselect.com/", R.drawable.sponsor_nine),
//                new Sponsor("docker", "Sponsor", "https://www.docker.com/", R.drawable.sponsor_ten),
//                new Sponsor("npm", "Sponsor", "https://www.npmjs.com/", R.drawable.sponsor_eleven),
        );

        currentSponsorName = (TextView) rootView.findViewById(R.id.sponsor_name);
        currentSponsorType = (TextView) rootView.findViewById(R.id.sponsor_type);
        previousSponsorButton = (ImageView) rootView.findViewById(R.id.previous_sponsor_button);
        nextSponsorButton = (ImageView) rootView.findViewById(R.id.next_sponsor_button);
        websiteButton = (FloatingActionButton) rootView.findViewById(R.id.sponsor_website_button);

        itemPicker = (DiscreteScrollView) rootView.findViewById(R.id.item_picker);
        itemPicker.setOrientation(Orientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new SponsorAdapter(sponsorData));
        itemPicker.setAdapter(infiniteAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        onItemChanged(sponsorData.get(0));

        rootView.findViewById(R.id.sponsor_name).setOnClickListener(this);
        rootView.findViewById(R.id.sponsor_type).setOnClickListener(this);
        rootView.findViewById(R.id.previous_sponsor_button).setOnClickListener(this);
        rootView.findViewById(R.id.next_sponsor_button).setOnClickListener(this);
        rootView.findViewById(R.id.sponsor_website_button).setOnClickListener(this);

        itemPicker.setSlideOnFling(true);
        itemPicker.setSlideOnFlingThreshold(2100);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sponsor_website_button:
                int realPosition = infiniteAdapter.getRealPosition(itemPicker.getCurrentItem());
                Sponsor current = sponsorData.get(realPosition);

                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                        .addDefaultShareMenuItem()
                        .setToolbarColor(ContextCompat.getColor(rootView.getContext(), R.color.colorPrimary))
                        .setShowTitle(true)
//                        .setCloseButtonIcon(backArrow)
                        .build();

                CustomTabsHelper.addKeepAliveExtra(rootView.getContext(), customTabsIntent.intent);

                CustomTabsHelper.openCustomTab(rootView.getContext(), customTabsIntent,
                        Uri.parse(current.getSponsorWebsite()),
                        new WebViewFallback());
                break;
            case R.id.previous_sponsor_button:
                realPosition = infiniteAdapter.getRealPosition(itemPicker.getCurrentItem());
                current = sponsorData.get(realPosition);
                smoothScrollToPreviousPosition(itemPicker, realPosition);
                break;
            case R.id.next_sponsor_button:
                realPosition = infiniteAdapter.getRealPosition(itemPicker.getCurrentItem());
                current = sponsorData.get(realPosition);
                smoothScrollToNextPosition(itemPicker, realPosition);
                break;
            default:
                break;
        }
    }

    private void onItemChanged(Sponsor sponsor) {
        currentSponsorName.setText(sponsor.getSponsorName());
        currentSponsorType.setText(sponsor.getSponsorType());
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
        onItemChanged(sponsorData.get(positionInDataSet));
    }

}
