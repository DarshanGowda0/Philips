package com.awaneesh.rohan.kewal.darshan.philips;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    public static ArrayList<TimelineData> list = new ArrayList<>();
    public static RecyclerView recyclerView;
    Toolbar toolbar;
    public static TimelineAdapter mTimelineAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchTimelineTask("DIABETES",0).execute();

        setUpRecyclerView();

        setupToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //post Question
                Intent in = new Intent(MainActivity.this, PostQuestion.class);
                startActivity(in);
            }
        });

    }

    private void setupToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);

            //nav bar
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
            drawerLayout.setDrawerListener(actionBarDrawerToggle);


            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            if (navigationView != null) {
                setupDrawerContent(navigationView);
            }


        }


    }

    private void setUpRecyclerView() {
        //initialise  recView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //setting the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        //set up the adapter
        mTimelineAdapter = new TimelineAdapter(MainActivity.this);
        recyclerView.setAdapter(mTimelineAdapter);

        /*recyclerView.setLoadingMore(true);
        recyclerView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int i, int i1, int i2) {

            }
        }, 1);*/


    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                    Log.d("Darshanrohantesting",""+menuItem.getItemId());
                        if (menuItem.getItemId() == R.id.nav_weight){
                            checkWeight();
                        }
                        return true;
                    }
                });
    }
    void checkWeight(){
        /*String weight;
        try {
            weight=Login.details.getString("weight");
            double min = 57.22,max = 63.58;
            if (Double.parseDouble(weight) >= min && Double.parseDouble(weight) <= max){
                Toast.makeText(MainActivity.this,"Healthy Weight",Toast.LENGTH_SHORT).show();
            }
            else if (Double.parseDouble(weight) > max){
                Toast.makeText(MainActivity.this,"OVERWEIGHT!! LOSE SOME WEIGHT",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this,"UNDERWEIGHT!! GAIN SOME WEIGHT",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }



}
