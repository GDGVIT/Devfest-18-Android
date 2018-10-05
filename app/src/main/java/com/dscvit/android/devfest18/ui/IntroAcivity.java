package com.dscvit.android.devfest18.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dscvit.android.devfest18.R;
import com.dscvit.android.devfest18.ui.adapter.MPagerAdapter;

public class IntroAcivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mPager;
    private int[] layouts={R.layout.first_slide,R.layout.second_slide,R.layout.third_slide,R.layout.fourth_slide};
    private MPagerAdapter mPagerAdapter;

    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    Button btnBack,btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(new PreferenceManager(this).checkPreferences())
        {
            loadHome();
        }
        if(Build.VERSION.SDK_INT>=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_intro_acivity);

        btnBack=findViewById(R.id.bnback);
        btnNext=findViewById(R.id.bnNext);
        btnBack.setBackground(null);
        btnNext.setBackground(null);
        btnNext.setText("Next");
        btnBack.setVisibility(View.INVISIBLE);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        mPager=findViewById(R.id.viewPager_intro);
        mPagerAdapter=new MPagerAdapter(layouts,this);
        mPager.setAdapter(mPagerAdapter);

        Dots_Layout=(LinearLayout) findViewById(R.id.dotslayout);
        createDots(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                  createDots(position);
                  if(position==layouts.length-1)
                  {
                      btnBack.setBackground(null);
                      btnNext.setBackground(null);
                      btnNext.setText("Start");
                      btnBack.setVisibility(View.VISIBLE);
                  }
                  else if(position==0)
                  {
                      btnBack.setBackground(null);
                      btnNext.setBackground(null);
                      btnNext.setText("Next");
                      btnBack.setVisibility(View.INVISIBLE);
                  }
                  else
                  {
                      btnBack.setBackground(null);
                      btnNext.setBackground(null);
                      btnNext.setText("Next");
                      btnBack.setVisibility(View.VISIBLE);
                  }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void createDots(int current_position)
    {
        if(Dots_Layout!=null){
            Dots_Layout.removeAllViews();
        }
        dots=new ImageView[layouts.length];

        for(int i=0;i<layouts.length;i++)
        {
            dots[i]=new ImageView(this);
            if(i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.defaults_dots));
            }
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            Dots_Layout.addView(dots[i],params);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bnback:
                loadbackslide();
                break;
            case R.id.bnNext:
                loadnextslide();
                break;
        }

    }
    private void loadHome()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    private  void loadnextslide()
    {
        int next_slide=mPager.getCurrentItem()+1;
        if(next_slide<layouts.length)
        {
            mPager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
    private  void loadbackslide()
    {
        int next_slide=mPager.getCurrentItem()-1;
        if(next_slide<layouts.length)
        {
            mPager.setCurrentItem(next_slide);
        }
    }


}
