package com.example.covidcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ViewPager mslideViewPager;
    private LinearLayout dot_layout;
    private Button btnNext, btnPrev;




    private Distance_Lat_Lang dt; // Creating an instance of class Distance_lat_lang

    private TextView [] mDots;
    private int mCurrentPage;


    private slide_page_adaptar slide_adaptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dt = new Distance_Lat_Lang();
        btnNext = (Button)findViewById(R.id.btn_next);
        btnPrev = (Button)findViewById(R.id.btn_prev);

        mslideViewPager = (ViewPager)findViewById(R.id.mslideViewPager);
        dot_layout = (LinearLayout) findViewById(R.id.dotLayout);
       // callNotification();

        slide_adaptar = new slide_page_adaptar(this);
        mslideViewPager.setAdapter(slide_adaptar);
        addDotIndicator(0);
        mslideViewPager.addOnPageChangeListener(viewListener);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mslideViewPager.setCurrentItem(mCurrentPage-1);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn = (String) btnNext.getText();

                if(btn== "Finish"){
                    Toast.makeText(MainActivity.this, "Welcome to the login page!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,Login.class);
                    startActivity(intent);
                }
                else{
                    mslideViewPager.setCurrentItem(mCurrentPage+1);
                }
            }
        });

    }

    public void addDotIndicator(int position){
        mDots = new TextView[2];
        dot_layout.removeAllViews();
        for(int i = 0;i<mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.black));

            dot_layout.addView(mDots[i]);


        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotIndicator(position);
            mCurrentPage = position;

            if(position == 0){
                btnPrev.setEnabled(false);
                btnNext.setEnabled(true);
                btnPrev.setVisibility(View.INVISIBLE);
                btnNext.setText("Next");

            }
            else if(position ==mDots.length-1){
                btnPrev.setEnabled(true);
                btnNext.setEnabled(true);
                btnPrev.setVisibility(View.VISIBLE);
                btnNext.setText("Finish");
                btnPrev.setText("Back");
            }
            else{
                btnPrev.setEnabled(true);
                btnNext.setEnabled(true);
                btnPrev.setVisibility(View.VISIBLE);
                btnNext.setText("Next");
                btnPrev.setText("Back");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }





}