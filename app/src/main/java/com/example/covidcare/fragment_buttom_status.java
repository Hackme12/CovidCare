package com.example.covidcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


public class fragment_buttom_status extends Fragment {

   private Button btnNext ;
   private TextView tv_cvd_volunteer;
   private RadioGroup Rbtn_volunteer;
   boolean status_check = false;
   boolean volunteer_check = false;
   boolean check_if_selected= false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_status, container, false);
        RadioGroup radioGroup = (RadioGroup) view .findViewById(R.id.radio_status);
        btnNext = (Button)view.findViewById(R.id.btn_status_next);
        tv_cvd_volunteer = (TextView)view.findViewById(R.id.text_cvd_volunteer);
        Rbtn_volunteer = (RadioGroup) view.findViewById(R.id.radio_volunteer);
        btnNext.setVisibility(View.INVISIBLE);

        tv_cvd_volunteer.setVisibility(View.INVISIBLE);
        Rbtn_volunteer.setVisibility(View.INVISIBLE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.radio_status_btn_yes:

                        tv_cvd_volunteer.setVisibility(View.VISIBLE);
                        Rbtn_volunteer.setVisibility(View.VISIBLE);
                        status_check = true;
                        btnNext.setVisibility(View.VISIBLE);

                        break;
                    case R.id.radio_status_btn_no:
                        tv_cvd_volunteer.setVisibility(View.INVISIBLE);
                        Rbtn_volunteer.setVisibility(View.INVISIBLE);
                        status_check = false;
                        btnNext.setVisibility(View.VISIBLE);

                        break;


                }
            }
        });


        Rbtn_volunteer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radio_volunteer_btn_yes:
                        volunteer_check = true;
                        btnNext.setVisibility(View.VISIBLE);

                        break;
                    case R.id.radio_volunteer_btn_no:
                        volunteer_check = false;
                        btnNext.setVisibility(View.VISIBLE);

                        break;



                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status_check && volunteer_check) {

                    /*

                            RUN THIS CODE EVERY AFTER HALF HOUR
                            GET THE LOCATION OF USER
                            UPDATE LOCATION TO DATA BASE
                            Store data only for two days
                            DATABASE ENTITY = EXPOSED AREA




                     */
                    getFragmentManager().beginTransaction().replace(R.id.frame_layout, new fragment_status_next()).commit();
                }
                else {

                    getFragmentManager().beginTransaction().replace(R.id.frame_layout, new fragment_status_volunteer_no()).commit();
                }




            }
        });





        return view;
    }






}