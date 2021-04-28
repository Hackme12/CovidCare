package com.example.covidcare;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;


public class bottom_home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView global_total_recovers, global_total_deaths, global_total_cases, global_today_cases, global_today_deaths, global_today_recovered;
    private TextView usa_today_case, usa_today_death, usa_today_recovered, usa_total_cases, usa_total_death, usa_total_recovered;
    private TextView local_today_cases, local_today_recovers, local_today_deaths, local_total_cases, local_total_death, local_total_recovers;

    String globalTotalRecovered, globalTotalDeath, globalTotalCase, globalTodayCase, globalTodayDeath, globalTodayRecovered;
    String usaTotalCase, usaTotalDeath, usaTotalRecovered, usaTodayCase, usaTodayDeath, usaTodayRecovered;
    String localTotalCase, localTotalRecovered,localTotalDeaths, localTodayCase, localTodayRecovered, localTodayDeath;

    ProgressBar progressBar;
    //Array list to store required data
    ArrayList<String> list_usa_today_recovered;
    String city;
    //String cityName = getArguments().getString("City");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_home, container, false);





        progressBar = new ProgressBar(getContext());

        // Declaring all the variable from layout
        global_total_recovers = (TextView) view.findViewById(R.id.tvData_Total_Recovered);
        global_total_deaths = (TextView) view.findViewById(R.id.tvData_Total_Death);
        global_total_cases = (TextView) view.findViewById(R.id.tvData_Total_Case);
        global_today_cases = (TextView) view.findViewById(R.id.tvData_TodaysCase);
        global_today_deaths = (TextView) view.findViewById(R.id.tvData_TodayDeath);
        global_today_recovered = (TextView) view.findViewById(R.id.tvData_TodayRecovered);
        usa_today_case = (TextView) view.findViewById(R.id.tvData_Usa_Todays_Case);
        usa_today_death = (TextView) view.findViewById(R.id.tvData_Usa_today_Death);
        usa_today_recovered = (TextView) view.findViewById(R.id.tvData_Usa_todays_Recover);
        usa_total_cases = (TextView) view.findViewById(R.id.tvData_Usa_Total_Case);
        usa_total_death = (TextView) view.findViewById(R.id.tvData_Usa_Total_Death);
        usa_total_recovered = (TextView) view.findViewById(R.id.tvData_Usa_Total_Recovered);

        local_total_cases = (TextView)view.findViewById(R.id.tvData_Local_Total_Case);
        local_today_cases = (TextView)view.findViewById(R.id.tvData_local_Todays_Case);
        local_today_deaths = (TextView)view.findViewById(R.id.tvData_Local_today_Death);
        local_today_recovers = (TextView)view.findViewById(R.id.tvData_TodayRecovered);
        local_total_death = (TextView)view.findViewById(R.id.tvData_Local_Total_Death);
        local_total_recovers = (TextView)view.findViewById(R.id.tvData_Local_Total_Recovered);

        list_usa_today_recovered = new ArrayList<>();

        Content content = new Content();
        content.execute();
        return view;

    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);

            global_total_recovers.setText(globalTotalRecovered);
            global_total_deaths.setText(globalTotalDeath);
            global_total_cases.setText(globalTotalCase);
            global_today_cases.setText(globalTodayCase);
            global_today_deaths.setText(globalTodayDeath);
            global_today_recovered.setText(globalTodayRecovered);
            usa_today_case.setText(usaTodayCase);
            usa_today_death.setText(usaTodayDeath);
            usa_today_recovered.setText(usaTodayRecovered);
            usa_total_cases.setText(usaTotalCase);
            usa_total_death.setText(usaTotalDeath);
            usa_total_recovered.setText(usaTotalRecovered);

            local_total_cases.setText(localTotalCase);
            local_today_cases.setText(localTodayCase);
            local_total_death.setText(localTotalDeaths);
           // local_total_recovers.setText(localTotalRecovered);
            local_today_deaths.setText(localTodayDeath);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            viewWorldCovidCase();
            viewUsaCovidCase();

            viewLocalCovidCase();


            return null;
        }
    }


    public void viewWorldCovidCase() {
        try {
                /*
                    Getting all the required data for dashboard from the source worldometer. worldometer provides the data related to covid-19.
                    Jsoup is used to connect the website
                    To crawl data such as global covid-19 cases, global total death and global total recovered, we are using cssQuery select which accepts
                    the Html div class: maincounter-number. class maincounter-number has tag called span which has data of global covid-19 cases, global total death and global total recovered

                 */





            Document doc = Jsoup.connect("https://www.worldometers.info/coronavirus/").get();
            Elements global_total_data = doc.select("div.maincounter-number");
            String[] global_total_data_arr = new String[global_total_data.size()]; // array to store global covid-19 cases, global total death and global total recovered
            int i = 0;
            for (Element data : global_total_data)// loop to access all the data inside global_total_data
            {
                global_total_data_arr[i] = data.getElementsByTag("span").text();
                i++;
            }
            globalTotalCase = global_total_data_arr[0];//Assigning data to the string which will be used onPostExecute method
            globalTotalDeath = global_total_data_arr[1];
            globalTotalRecovered = global_total_data_arr[2];

            /*
                Extracting the new cases of Covid-19: global, usa and local from table in the website.
                Used of cssQuery to extract the table data

             */
            Element tableElement = doc.select("table").first();
            // Elements tableElements = tableElement.select("tbody tr td");
            Elements tableElements = tableElement.getElementsByClass("total_row_world");

            String global_todays_data[] = new String[tableElements.size()];
            for (int j = 0; j < tableElements.size(); j++) {
                global_todays_data[j] = tableElements.get(i).text();
            }
            String[] arr_data = global_todays_data[7].split(" ");
            globalTodayCase = arr_data[2];
            globalTodayDeath = arr_data[4];


            if (arr_data[6].matches("[a-zA-Z]")) {
                globalTodayRecovered = "0";
            } else {
                globalTodayRecovered = arr_data[6];
            }


            // Scrapping


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void viewUsaCovidCase() {


        try {
            Document doc_2 = Jsoup.connect("https://www.worldometers.info/coronavirus/country/us/").get();
            Elements usa_data = doc_2.select("div.maincounter-number");
            String[] usa_total_data = new String[usa_data.size()];
            int j = 0;
            for (Element data : usa_data) {
                usa_total_data[j] = data.getElementsByTag("span").text();
                j++;
            }
            usaTotalCase = usa_total_data[0];
            usaTotalDeath = usa_total_data[1];
            usaTotalRecovered = usa_total_data[2];


            Element table_usa_data = doc_2.select("table").first();
            Elements tableUsaElements = table_usa_data.getElementsByClass("total_row_usa");
            String[] arr_data_usa = tableUsaElements.get(0).text().split(" ");
            usaTodayCase = arr_data_usa[3];
            usaTodayDeath = arr_data_usa[5];

            Document doc = Jsoup.connect("https://www.worldometers.info/coronavirus/").get();
            Element tableElement = doc.select("table").first();
            for (Element data : tableElement.select("td:nth-of-type(8)")) {
                list_usa_today_recovered.add(data.text());
            }
            if (list_usa_today_recovered.get(8).equals("")) {
                usaTodayRecovered = "0";
            } else {
                usaTodayRecovered = list_usa_today_recovered.get(8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void viewLocalCovidCase()  {

        try{
            Document doc_2 = Jsoup.connect("https://www.worldometers.info/coronavirus/usa/texas/").get();
            Element table_usa_data = doc_2.select("table").first();

            ArrayList<String> list = new ArrayList<>();


            dashboard dash = (dashboard)getActivity();
            city = dash.getCityName();
            System.out.println("cccccccc"+ city + " dash: "+ dash.getCityName());
            city = "Lubbock";
            //System.out.println("cityyyy name:"+ city);

            int i = 0;
            int temp = 0;
            for(Element data: table_usa_data.select("td:nth-of-type(1)")){

                if(data.text().toLowerCase().equals(city.toLowerCase())) {

                    System.out.println("Index: " + i + " " + data.text());
                    temp = i;
                    System.out.println("VALUE OF TEMP: "+ temp);

                    Element local_tot_case = table_usa_data.select("td:nth-of-type(2)").get(temp);
                    localTotalCase = local_tot_case.text();

                    Element local_tot_reco_case = table_usa_data.select("td:nth-of-type(6)").get(temp);
                   localTotalRecovered = String.valueOf(Integer.parseInt(localTotalCase)- Integer.parseInt(local_tot_reco_case.text()));

                    System.out.println("Local recovered"+ localTotalRecovered);
                    Element local_tot_death_case = table_usa_data.select("td:nth-of-type(4)").get(temp);
                    System.out.println("LOCAL DEATH:"+ local_tot_death_case.text());
                    localTotalDeaths = local_tot_death_case.text();

                    Element local_today_case = table_usa_data.select("td:nth-of-type(3)").get(temp);
                    if(local_today_case ==null){
                        localTodayCase = local_today_case.text();
                    }
                    else{
                        localTodayCase = "0";
                    }


                    Element local_today_reco_case = table_usa_data.select("td:nth-of-type(2)").get(temp);
                    System.out.println("LOCAL DEATH:"+ local_today_reco_case.text());
                    localTodayRecovered = local_today_reco_case.text();


                    Element local_today_death_case = table_usa_data.select("td:nth-of-type(5)").get(temp);
                    if(local_today_death_case== null){
                        localTodayDeath = local_today_death_case.text();
                    }
                    else{
                        localTodayDeath = "0";
                    }


                    break;
                }
                i++;
            }

//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    dashboard dash = (dashboard)getActivity();
//                    city = dash.getCityName();
//                    city = "Lubbock";
//                    //System.out.println("cityyyy name:"+ city);
//
//                    int i = 0;
//                    int temp = 0;
//                    for(Element data: table_usa_data.select("td:nth-of-type(1)")){
//
//
//
//
//                        if(data.text().toLowerCase().equals(city.toLowerCase())) {
//
//                            System.out.println("Index: " + i + " " + data.text());
//                            temp = i;
//                            System.out.println("VALUE OF TEMP: "+ temp);
//                            for(Element data1: table_usa_data.select("td:nth-of-type(2)")){
//                                list.add(data1.text());
//                            }
//
//                            System.out.println("local total cases "+list.get(temp));
//                            localTotalCase = list.get(temp);
//
//                            break;
//                        }
//                        i++;
//                    }
//
//                    // String cityName = getArguments().getString("City");
//                    //System.out.println("CITY NAME IS : "+ cityName);
//                }
//            },5000);







        }
        catch (Exception exception){
            exception.printStackTrace();
        }

    }

}