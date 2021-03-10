package com.example.covidcare;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class fragment_buttom_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView tvHome;
    ProgressBar progressBar;
    String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_bottom_home, container, false);
       progressBar = new ProgressBar(getContext());
       //tvHome = view.findViewById(R.id.textView_btm_home);
       Content content = new Content();
       content.execute();


       return view;
    }

    private class Content extends AsyncTask<Void, Void,Void>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));


        }
        @Override
        protected void onPostExecute(Void avoid){
            super.onPostExecute(avoid);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
            //tvHome.setText(title);
        }

        @Override
        protected void onCancelled(){
            super.onCancelled();
        }



        @Override
        protected Void doInBackground(Void... voids) {

            try {
                System.out.println("hello");
                Document doc = Jsoup.connect("https://jsoup.org/cookbook/input/load-document-from-url").get();
                System.out.println("TITLE OF DOCUMENT IS : " + doc.title());
                title = doc.title();




            }
            catch (Exception e){
                e.printStackTrace();
            }




            return null;
        }
    }

}