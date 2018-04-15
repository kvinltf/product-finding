package com.example.productfinding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private FirebaseUser firebaseUser;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        init();
        isUserExist();
        initNavigationDrawerHeader();
    }

    private void init() {
        Log.d(TAG, "init: Initialize Variable");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getParent(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        setSupportActionBar(toolbar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private boolean isUserExist() {
        Log.d(TAG, "isUserExist: Check is User Exist");
        if (firebaseUser.getDisplayName() != null) {
            Toast.makeText(getApplicationContext(), "You Login As: " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            return true;
        } else
            Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: Select Nav Item");
        switch (item.getItemId()) {
            case R.id.nav_logout:
                signOut();
                break;
            case R.id.nav_profile:
                Toast.makeText(getApplicationContext(), "Profile clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_map:
                Toast.makeText(getApplicationContext(), "Map clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_history:
                Toast.makeText(getApplicationContext(), "History clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_setting:
                Toast.makeText(getApplicationContext(), "Setting clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
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

    private void initNavigationDrawerHeader() {
        Log.d(TAG, "initNavigationDrawer: Initialize Navigation Drawer Header");

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_nav);

        TextView userDisplayName = headerView.findViewById(R.id.nav_header_tv_name);
        String _name = firebaseUser.getDisplayName();
        userDisplayName.setText(_name);

        TextView userEmail = headerView.findViewById(R.id.nav_header_tv_email);
        String _email = firebaseUser.getEmail();
        userEmail.setText(_email);

//        ImageView userProfilePic = headerView.findViewById(R.id.nav_header_iv_profile);
//        Uri _profilePic = firebaseUser.getPhotoUrl();
//        userProfilePic.setImageURI(_profilePic);
    }

    private void signOut() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        FirebaseAuth.getInstance().signOut();
        startActivity(i);
        finish();
    }
}
