package com.example.covidcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_menu);
        navigationView.bringToFront();
        auth = FirebaseAuth.getInstance();


        //NavController navController = navHostFragment.getNavController();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new fragment_buttom_home()).commit();
        }
       // NavigationUI.setupWithNavController(bottomNavigationView,navController);

    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            auth.signOut();
            Intent intent = new Intent(dashboard.this,Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onBackPressed();
        }

    }





    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){

                case R.id.bottom_menu_home:
                    selectedFragment = new fragment_buttom_home();
                    break;
                case R.id.bottom_menu_map:
                    selectedFragment = new fragment_buttom_map();
                    break;
                case R.id.bottom_menu_appointment:
                    selectedFragment = new fragment_buttom_appointment();
                    break;
                case R.id.bottom_menu_covid_status:
                    selectedFragment = new fragment_buttom_status();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selectedFragment).commit();
            return true;
        }
    };
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new fragment_drawer_home()).commit();
                //Toast.makeText(this, "Hello there", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new fragment_drawer_profile()).commit();
                //Toast.makeText(this, "Hello there", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new fragment_drawer_message()).commit();
                //Toast.makeText(this, "Hello there", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_news:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new fragment_drawer_news()).commit();
                break;

            case R.id.menu_vaccine_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new fragment_drawer_vaccine_info()).commit();
                break;

            case R.id.menu_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new fragment_drawer_help()).commit();
                //Toast.makeText(this, "Hello there", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new fragment_drawer_about()).commit();
                break;
            case R.id.menu_logout:
               auth.signOut();
               Intent intent = new Intent( dashboard.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);

                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }




}