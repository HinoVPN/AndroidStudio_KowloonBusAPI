package com.example.myapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.myapp.ui.fragment.BusRouteListFragment;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String tag = BusRouteListFragment.TAG;
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            BusRouteListFragment fragment = BusRouteListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .commit();
        }
    }


}