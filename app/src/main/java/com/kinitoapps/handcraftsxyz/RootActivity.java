package com.kinitoapps.handcraftsxyz;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.relex.circleindicator.CircleIndicator;

import static com.bumptech.glide.Glide.with;

public class RootActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Home.OnFragmentInteractionListener,shop_by_category.OnFragmentInteractionListener{
//    private static final String URL_PRODUCTS ="http://handicraft-com.stackstaging.com/myapi/api.php";
//    ImageView tile1,tile2,tile3,tile4,tile5,tile6,tile7,tile8;
    int selected;
    boolean mDrawerItemClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        android.support.v4.app.Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = Home.class;
        try {
            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
            selected = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                android.support.v4.app.Fragment fragment = null;
                Class fragmentClass = null;

                if(mDrawerItemClicked){
                    if(selected == 1){
                        fragmentClass = Home.class;
                        try {
                            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        FragmentManager fragmentManager = getSupportFragmentManager();

                        fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
                    }
                    else if(selected == 2){
                        fragmentClass = shop_by_category.class;
                        try {
                            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
                    }

                    mDrawerItemClicked = false;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
//        ViewPager viewPager = findViewById(R.id.viewpager);
//        ViewPagerAdapterRootSlider viewPagerAdapter = new ViewPagerAdapterRootSlider(this);
//        tile1 = findViewById(R.id.tile_image1);
//        tile2 = findViewById(R.id.tile_image2);
//        tile3 = findViewById(R.id.tile_image3);
//        tile4 = findViewById(R.id.tile_image4);
//        tile5 = findViewById(R.id.tile_image5);
//        tile6 = findViewById(R.id.tile_image6);
//        tile7 = findViewById(R.id.tile_image7);
//        tile8 = findViewById(R.id.tile_image8);
//        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
//        viewPager.setAdapter(viewPagerAdapter);
//        indicator.setViewPager(viewPager);


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
        getMenuInflater().inflate(R.menu.root, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            selected = 1;
        } else if (id == R.id.nav_categories) {
            selected = 2;
        } else if (id == R.id.nav_newarrivals) {
            selected = 3;
        }
        mDrawerItemClicked = true;

//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
