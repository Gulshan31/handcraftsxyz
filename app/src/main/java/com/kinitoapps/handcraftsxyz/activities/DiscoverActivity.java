package com.kinitoapps.handcraftsxyz.activities;

import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kinitoapps.handcraftsxyz.fragments.DefaultDiscoverFragment;
import com.kinitoapps.handcraftsxyz.fragments.DiscoverProductFragment;
import com.kinitoapps.handcraftsxyz.fragments.DiscoverStoreFragment;
import com.kinitoapps.handcraftsxyz.R;

import java.util.ArrayList;
import java.util.Random;

public class DiscoverActivity extends AppCompatActivity implements DefaultDiscoverFragment.OnFragmentInteractionListener,DiscoverStoreFragment.OnFragmentInteractionListener,
        DiscoverProductFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        android.support.v4.app.Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = DefaultDiscoverFragment.class;
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = findViewById(R.id.toolbar_title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        try {
            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        FloatingActionButton fab = findViewById(R.id.fab);
//        centerTitle();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.Fragment fragment = null;
                Class fragmentClass = null;
                Random random = new Random();
                if(random.nextInt(2)==0){
                    fragmentClass = DiscoverProductFragment.class;
                    try {
                        fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
                }
                else {
                    fragmentClass = DiscoverStoreFragment.class;
                    try {
                        fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
                }

            }
        });
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
