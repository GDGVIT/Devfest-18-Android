package com.dscvit.android.devfest18.ui.info.FAQ;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import com.dscvit.android.devfest18.R;
import com.dscvit.android.devfest18.ui.adapter.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FaqFragment extends Fragment {


    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    public static Fragment newInstance() {
        return new FaqFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_faq, container, false);


        listView=(ExpandableListView)v.findViewById(R.id.lvexp);
        initData();
        listAdapter=new ExpandableListAdapter(getContext(),listDataHeader,listHash);
        listView.setAdapter(listAdapter);

        return v;

    }
    private void initData(){
        listDataHeader=new ArrayList<>();
        listHash=new HashMap<>();

        listDataHeader.add(getContext().getString(R.string.hq1));
        listDataHeader.add(getContext().getString(R.string.hq2));
        listDataHeader.add(getContext().getString(R.string.hq3));
        listDataHeader.add(getContext().getString(R.string.hq4));
        listDataHeader.add(getContext().getString(R.string.hq5));
        listDataHeader.add(getContext().getString(R.string.hq6));
        listDataHeader.add(getContext().getString(R.string.hq7));
        listDataHeader.add(getContext().getString(R.string.hq8));
        listDataHeader.add(getContext().getString(R.string.hq9));
        listDataHeader.add(getContext().getString(R.string.hq10));
        listDataHeader.add(getContext().getString(R.string.hq11));
        listDataHeader.add(getContext().getString(R.string.hq12));
        listDataHeader.add(getContext().getString(R.string.hq13));
        listDataHeader.add(getContext().getString(R.string.hq14));
        listDataHeader.add(getContext().getString(R.string.hq15));


        List<String> HA1=new ArrayList<>();
        HA1.add(getContext().getString(R.string.ha1));

        List<String> HA2=new ArrayList<>();
        HA2.add(getContext().getString(R.string.ha2));

        List<String> HA3=new ArrayList<>();
        HA3.add(getContext().getString(R.string.ha3));
        List<String> HA4=new ArrayList<>();
        HA4.add(getContext().getString(R.string.ha4));
        List<String> HA5=new ArrayList<>();
        HA5.add(getContext().getString(R.string.ha5));
        List<String> HA6=new ArrayList<>();
        HA6.add(getContext().getString(R.string.ha6));
        List<String> HA7=new ArrayList<>();
        HA7.add(getContext().getString(R.string.ha7));
        List<String> HA8=new ArrayList<>();
        HA8.add(getContext().getString(R.string.ha8));
        List<String> HA9=new ArrayList<>();
        HA9.add(getContext().getString(R.string.ha9));
        List<String> HA10=new ArrayList<>();
        HA10.add(getContext().getString(R.string.ha10));

        List<String> HA11=new ArrayList<>();
        HA11.add(getContext().getString(R.string.ha11));

        List<String> HA12=new ArrayList<>();
        HA12.add(getContext().getString(R.string.ha12));

        List<String> HA13=new ArrayList<>();
        HA13.add(getContext().getString(R.string.ha13));

        List<String> HA14=new ArrayList<>();
        HA14.add(getContext().getString(R.string.ha14));

        List<String> HA15=new ArrayList<>();
        HA15.add(getContext().getString(R.string.ha15));

        listDataHeader.add(getContext().getString(R.string.dq1));
        listDataHeader.add(getContext().getString(R.string.dq2));
        listDataHeader.add(getContext().getString(R.string.dq3));
        listDataHeader.add(getContext().getString(R.string.dq4));
        listDataHeader.add(getContext().getString(R.string.dq5));
        listDataHeader.add(getContext().getString(R.string.dq6));
        listDataHeader.add(getContext().getString(R.string.dq7));
        listDataHeader.add(getContext().getString(R.string.dq8));
        listDataHeader.add(getContext().getString(R.string.dq9));
        listDataHeader.add(getContext().getString(R.string.dq10));
        listDataHeader.add(getContext().getString(R.string.dq11));


        List<String> DA1=new ArrayList<>();
        DA1.add(getContext().getString(R.string.da1));

        List<String> DA2=new ArrayList<>();
        DA2.add(getContext().getString(R.string.da2));
        List<String> DA3=new ArrayList<>();
        DA3.add(getContext().getString(R.string.da3));
        List<String> DA4=new ArrayList<>();
        DA4.add(getContext().getString(R.string.da4));
        List<String> DA5=new ArrayList<>();
        DA5.add(getContext().getString(R.string.da5));
        List<String> DA6=new ArrayList<>();
        DA6.add(getContext().getString(R.string.da6));
        List<String> DA7=new ArrayList<>();
        DA7.add(getContext().getString(R.string.da7));
        List<String> DA8=new ArrayList<>();
        DA8.add(getContext().getString(R.string.da8));
        List<String> DA9=new ArrayList<>();
        DA9.add(getContext().getString(R.string.da9));
        List<String> DA10=new ArrayList<>();
        DA10.add(getContext().getString(R.string.da10));
        List<String> DA11=new ArrayList<>();
        DA11.add(getContext().getString(R.string.da11));




        listHash.put(listDataHeader.get(0),HA1);
        listHash.put(listDataHeader.get(1),HA2);
        listHash.put(listDataHeader.get(2),HA3);
        listHash.put(listDataHeader.get(3),HA4);
        listHash.put(listDataHeader.get(4),HA5);
        listHash.put(listDataHeader.get(5),HA6);
        listHash.put(listDataHeader.get(6),HA7);
        listHash.put(listDataHeader.get(7),HA8);
        listHash.put(listDataHeader.get(8),HA9);
        listHash.put(listDataHeader.get(9),HA10);
        listHash.put(listDataHeader.get(10),HA11);
        listHash.put(listDataHeader.get(11),HA12);
        listHash.put(listDataHeader.get(12),HA13);
        listHash.put(listDataHeader.get(13),HA14);
        listHash.put(listDataHeader.get(14),HA15);
        listHash.put(listDataHeader.get(15),DA1);
        listHash.put(listDataHeader.get(16),DA2);
        listHash.put(listDataHeader.get(17),DA3);
        listHash.put(listDataHeader.get(18),DA4);
        listHash.put(listDataHeader.get(19),DA5);
        listHash.put(listDataHeader.get(20),DA6);
        listHash.put(listDataHeader.get(21),DA7);
        listHash.put(listDataHeader.get(22),DA8);
        listHash.put(listDataHeader.get(23),DA9);
        listHash.put(listDataHeader.get(24),DA10);
        listHash.put(listDataHeader.get(25),DA11);





    }
    }

