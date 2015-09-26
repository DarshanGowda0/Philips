package com.awaneesh.rohan.kewal.darshan.philips;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    public static ArrayList<TimelineData> list = new ArrayList<>();
    public static RecyclerView recyclerView;
    Toolbar toolbar;
    public static TimelineAdapter mTimelineAdapter;
    static ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Loading....");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.show();

        new FetchTimelineTask("DIABETES", 0).execute();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        list.clear();
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
                        if (menuItem.getItemId() == R.id.nav_weight) {
                            checkWeight();
                        }
                        return true;
                    }
                });
    }

    void checkWeight() {
        double min = 63.58, max = 57.22, avg = 0, calorie = 0;

        Log.d("Kewal",""+Login.weightValue.size()+" "+Login.weightValue);
        for (int i = 0; i < Login.weightValue.size(); i++) {
            if ((i % 2) != 0) {
                Log.d("Kewal","no i="+i+": "+Double.parseDouble(Login.weightValue.get(i)));
                continue;
            } else {
                avg += Double.parseDouble(Login.weightValue.get(i));
                Log.d("Kewal","yes i="+i+": "+Double.parseDouble(Login.weightValue.get(i)));
            }
        }
        avg = avg / (Login.weightValue.size()/2);
        Log.d("weight",""+avg);
        calorie = 655 + (19.6*avg) + (1.8*170) - (4.7 * 20);
        if (avg >= min && avg <= max ){
            Toast.makeText(this,"Healthy!! Can consume upto "+calorie+" calories",Toast.LENGTH_LONG).show();
        }
        else if (avg > max ){
            Toast.makeText(this,"Overweight!! Gotta reduce.. Should consume atmax "+(calorie-400)+" calories",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this,"Underweight!! Gotta Increase..Can consume upto "+calorie+" calories",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
