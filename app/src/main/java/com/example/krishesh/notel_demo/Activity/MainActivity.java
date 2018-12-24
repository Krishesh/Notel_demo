package com.example.krishesh.notel_demo.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.krishesh.notel_demo.Fragments.Hotels;
import com.example.krishesh.notel_demo.Fragments.My_travel_plans;
import com.example.krishesh.notel_demo.Fragments.Profile;
import com.example.krishesh.notel_demo.Fragments.Recent_view;
import com.example.krishesh.notel_demo.Fragments.Travel_guide;
import com.example.krishesh.notel_demo.R;

import static com.example.krishesh.notel_demo.R.id.fab;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FloatingActionButton fab;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_HOTEL = "hotel";
    private static final String TAG_MY_TRAVELS_PLAN = "my travels plan";
    private static final String TAG_RECENT_VIEW = "recent vew";
    private static final String TAG_TRAVEL_GUIDE = "travel guide";
    public static String CURRENT_TAG = TAG_PROFILE;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override

            public void onClick(View view) {
                // to make call
                Intent callintent = new Intent(Intent.ACTION_DIAL);
                callintent.setData(Uri.parse("tel:0123456789"));
                startActivity(callintent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

/*    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {*/
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                   /* getSupportFragmentManager().beginTransaction().replace(R.id.nav_profile,
                        new Profile()).commit();*/
                    navItemIndex = 0;
                    CURRENT_TAG = TAG_PROFILE;

                } else if (id == R.id.nav_hotels) {
                    navItemIndex = 1;
                    CURRENT_TAG = TAG_HOTEL;

                } else if (id == R.id.nav_travel_plan) {
                    navItemIndex = 2;
                    CURRENT_TAG = TAG_MY_TRAVELS_PLAN;


                } else if (id == R.id.nav_recent) {
                    navItemIndex = 3;
                    CURRENT_TAG = TAG_RECENT_VIEW;

                } else if (id == R.id.nav_guide) {
                    navItemIndex = 4;
                    CURRENT_TAG = TAG_TRAVEL_GUIDE;


                } else if (id == R.id.nav_share) {
                    navItemIndex = 5;
                    startActivity(new Intent(MainActivity.this, Share.class));
           /* drawer.closeDrawers();*/
                    return true;

                } else if (id == R.id.nav_help) {
                    navItemIndex = 6;
                    startActivity(new Intent(MainActivity.this, Help_support.class));
           /* drawer.closeDrawers();*/
                    return true;


                } else if (id == R.id.nav_feedback) {
                    navItemIndex = 7;
                    startActivity(new Intent(MainActivity.this, Feedbacks.class));
            /*drawer.closeDrawers();*/
                    return true;

                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
           // });
            }


    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.content_main, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    //Togglefab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }


    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                //profile
                Profile profileFragment = new Profile();
                return profileFragment;
            case 1:
               //hotels
                Hotels hotelsFragment = new Hotels();
                return hotelsFragment ;
            case 2:
                //My travels plans
                My_travel_plans my_travel_plansFragment = new My_travel_plans();
                return my_travel_plansFragment;
            case 3:
                //recent view
                Recent_view Recent_viewFragment = new Recent_view();
                return Recent_viewFragment;

            case 4:
                // Travel guide
                Travel_guide Travel_guideFragment = new Travel_guide();
                return Travel_guideFragment;
            default:
                return new Profile();
        }
    }


}
