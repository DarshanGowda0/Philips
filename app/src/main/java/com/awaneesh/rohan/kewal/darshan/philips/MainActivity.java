package com.awaneesh.rohan.kewal.darshan.philips;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    public static ArrayList<TimelineData> list = new ArrayList<>();
    public static RecyclerView recyclerView;
    Toolbar toolbar;
    public static TimelineAdapter mTimelineAdapter;
    static ProgressDialog progressDialog;
   // int str[] = {R.drawable.md1, R.drawable.md2, R.drawable.md3}, i;
    DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;
    CircleImageView circleImageView;
String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
SharedPreferences sharedPreferences=getSharedPreferences("Yes",MODE_PRIVATE);
        username=sharedPreferences.getString("name","kewal");
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Loading....");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.show();

        new FetchTimelineTask("DIABETES", 0).execute();
      //  i = (int) ((System.currentTimeMillis() / 1000) % 3);
        LinearLayout v = (LinearLayout) findViewById(R.id.nav);
        v.setBackgroundResource(R.drawable.md3);
        setUpUIL();
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

    private void setUpUIL() {
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
                .displayer(new SimpleBitmapDisplayer()).build();

        config = new ImageLoaderConfiguration.Builder(
                MainActivity.this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        circleImageView = (CircleImageView) findViewById(R.id.circleView);

        ImageLoader.getInstance().displayImage(getSharedPreferences("Yes", MODE_PRIVATE).getString("picture", ""), circleImageView, defaultOptions);

        TextView user = (TextView) findViewById(R.id.myName);
        user.setText(username);
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
        Intent in = new Intent(this, Calorie.class);
        startActivity(in);
        finish();
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
