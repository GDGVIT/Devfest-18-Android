package com.dscvit.android.devfest18.ui.info.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dscvit.android.devfest18.R;

import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {


    public static Fragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }


}
