package com.example.productfinding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productfinding.login.LoginActivity;
import com.example.productfinding.model.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    //    private Toolbar toolbar;
    private ActionBar actionBar;
    private User loginUser;
    private NavigationView navigationView;
    private ImageView drawerToggle;
    private ImageView mSearchButton;
    private EditText mSearchEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeParameter();
        initNavigationDrawerHeader();
    }

    private void initializeParameter() {
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSearchEt = findViewById(R.id.search_et_search_item);

        drawerToggle = findViewById(R.id.search_iv_drawer_toggle);
        drawerToggle.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));

        mSearchButton = findViewById(R.id.search_iv_search_button);
        mSearchButton.setOnClickListener((View v) -> {
            Toast.makeText(this, "Search: " + mSearchEt.getText().toString(), Toast.LENGTH_SHORT).show();
        });

        loginUser = (User) getIntent().getSerializableExtra("loginUser");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        mDrawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.nav_history:
                Toast.makeText(this, "History Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_logout:
                signOut();
                return true;
            case R.id.nav_setting:
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_map:
                Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                return true;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (isTaskRoot()) {
                exitApplication();
            } else
                super.onBackPressed();
        }
    }

    /*
     * Open the drawer when the button is tapped
     * To open the drawer when the user taps on the nav drawer button,
     * override onOptionsItemSelected() to hook into the options menu framework and listen for when the user taps the item with the ID android.R.id.home.
     * Then call openDrawer() to open the nav drawer:
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initNavigationDrawerHeader() {
        Log.d(TAG, "initNavigationDrawer: Initialize Navigation Drawer Header");

        View headerView = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);

        TextView userDisplayName = headerView.findViewById(R.id.nav_header_tv_name);
        String _name = loginUser.getName();
        userDisplayName.setText(_name);

        TextView userEmail = headerView.findViewById(R.id.nav_header_tv_email);
        String _email = loginUser.getEmail();
        userEmail.setText(_email);
    }


    /**
     * Logout User and Clear Shared Preferences Data
     */
    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out");
        builder.setMessage("Confirm to Sign Out?");
        builder.setPositiveButton("Sign Out", (dialog, which) -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.share_preference_current_user), MODE_PRIVATE);
            sharedPreferences.edit().clear().commit();
            startActivity(i);
            finish();
        });
        builder.create();
        builder.show();
    }

    private void exitApplication() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Confirm Exit Application?");
        builder.setPositiveButton("Exit", ((dialog, which) -> finish()));
        builder.setNegativeButton("No", (dialog, which) -> {
        });
        builder.create();
        builder.show();
    }


}
