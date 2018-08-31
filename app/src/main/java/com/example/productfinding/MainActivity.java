package com.example.productfinding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.productfinding.adapter.ResultRecycleViewAdapter;
import com.example.productfinding.login.LoginActivity;
import com.example.productfinding.model.Catalog;
import com.example.productfinding.model.ResponseObject;
import com.example.productfinding.model.User;
import com.example.productfinding.util.IntentUtil;
import com.example.productfinding.util.KeyboardUtil;
import com.example.productfinding.util.LocationUtil;
import com.example.productfinding.util.ResultListUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private User mLoginUser;
    private NavigationView mNavigationView;
    private ImageView mDrawerToggle, mSearchButton, mMoreButton;
    private EditText mUserSearchText;
    private TextView mSearchResult;
    private List<Catalog> mDisplayCatalogList = new ArrayList<>();
    private List<Catalog> mFullCatalogList = new ArrayList<>();
    private Location mCurrentLocation;

    private static Boolean isSortByAscending;
    private static int distanceFilter;

    //recycle view things
    private RecyclerView.Adapter mRecycleAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isSortByAscending == null) isSortByAscending = true;
        if (distanceFilter == 0) distanceFilter = 10;

        initializeParameter();
        initializeRecycleView();
        initNavigationDrawerHeader();
    }

    private void initializeRecycleView() {
        //RECYCLE VIEW THINGS

        mRecyclerView = findViewById(R.id.search_result_recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        getCurrentLocation();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mRecycleAdapter = new ResultRecycleViewAdapter(mDisplayCatalogList, mCurrentLocation);
        mRecyclerView.setAdapter(mRecycleAdapter);
    }

    private void initializeParameter() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mUserSearchText = findViewById(R.id.search_et_search_item);
        mUserSearchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mSearchButton.performClick();
                return true;
            }
            return false;
        });

        mDrawerToggle = findViewById(R.id.search_iv_drawer_toggle);
        mDrawerToggle.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));

        mSearchResult = findViewById(R.id.mapsearch_tv_search_result);

        mSearchButton = findViewById(R.id.search_iv_search_button);
        mSearchButton.setOnClickListener((View v) -> {

            if (mUserSearchText.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "You do not type anything inside :(", Toast.LENGTH_LONG).show();
                mUserSearchText.setText("");
            } else {
                clearRecycleView();
                mSearchResult.setText(mUserSearchText.getText().toString());
                KeyboardUtil.hideSoftKeyboard(this);
                getCatalogList(mUserSearchText.getText().toString());
            }
        });

        mLoginUser = IntentUtil.getLoginUserFromIntent(getIntent());

        mMoreButton = findViewById(R.id.search_iv_more);
        mMoreButton.setOnClickListener(v -> showPopup(v));
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this::onMenuItemClick);

        setSortPopUpTitle(popup.getMenu().findItem(R.id.main_menu_sort));
        setMenuDistance(popup.getMenu().findItem(R.id.main_menu_distance));

        popup.show();
    }

    private void getCatalogList(String userSearch) {
        Log.d(TAG, "getCatalogList: Executing");
        String url = getString(R.string.url_catalog);
        mFullCatalogList.clear();
        mDisplayCatalogList.clear();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("action", "searchall");
            jsonObject.put("user_search", userSearch);
        } catch (JSONException e) {
            Log.w(TAG, "getCatalogList: Error while putting jsonObject:", e.getCause());
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                (JSONObject response) -> {
                    try {
                        final ObjectMapper objectMapper = new ObjectMapper();
                        ResponseObject<List<Catalog>> responseObject =
                                objectMapper.readValue(response.toString(), new TypeReference<ResponseObject<List<Catalog>>>() {
                                });
//
                        if (responseObject.isStatusSuccess()) {
                            Log.d(TAG, "getCatalogList: Success");

                            if (responseObject.getQuery_result().size() > 0) {
                                for (int i = 0; i < responseObject.getQuery_result().size(); i++) {
                                    mFullCatalogList.add(responseObject.getQuery_result().get(i));
                                }
                                populateRecycleView();
                            } else
                                Toast.makeText(this, "There is no result on your search.", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        mDrawerLayout.closeDrawers();

        Intent _intent;
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
                _intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(_intent);
                return true;
            case R.id.nav_profile:
//                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                _intent = new Intent(this, ProfileActivity.class);
                IntentUtil.setLoginUserForIntent(_intent, mLoginUser);
                startActivity(_intent);
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
     * To open the drawer when the user taps on the main_menu drawer button,
     * override onOptionsItemSelected() to hook into the options menu framework and listen for when the user taps the item with the ID android.R.id.home.
     * Then call openDrawer() to open the main_menu drawer:
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

        View headerView = mNavigationView.inflateHeaderView(R.layout.navigation_drawer_header);

        TextView userDisplayName = headerView.findViewById(R.id.nav_header_tv_name);
        String _name = mLoginUser.getName();
        userDisplayName.setText(_name);

        TextView userEmail = headerView.findViewById(R.id.nav_header_tv_email);
        String _email = mLoginUser.getEmail();
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
            sharedPreferences.edit().clear().apply();
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

    private void getCurrentLocation() {
        LocationUtil locationUtil = new LocationUtil(this, getApplicationContext());
        mCurrentLocation = locationUtil.getMyCurrentLocation();
    }

    private void clearRecycleView() {
        mDisplayCatalogList.clear();
        mDisplayCatalogList.clear();
        mRecycleAdapter.notifyDataSetChanged();
    }

    private void populateRecycleView() {
        if (mFullCatalogList.isEmpty()) return;

        mDisplayCatalogList.clear();

        if (distanceFilter != 0) {
            for (int i = 0; i < mFullCatalogList.size(); i++) {
                Location shopLocation = new Location("");
                shopLocation.setLatitude(mFullCatalogList.get(i).getShop().getLatitude());
                shopLocation.setLongitude(mFullCatalogList.get(i).getShop().getLongitude());

                if (LocationUtil.distanceToKM(mCurrentLocation.distanceTo(shopLocation)) <= (float) distanceFilter) {
                    mDisplayCatalogList.add(mFullCatalogList.get(i));
                }
            }

        } else {
            for (int i = 0; i < mFullCatalogList.size(); i++) {
                mDisplayCatalogList.add(mFullCatalogList.get(i));
            }
        }

        if (!mDisplayCatalogList.isEmpty()) {
            if (isSortByAscending)
                ResultListUtil.sortCatalogListBy(mDisplayCatalogList, mCurrentLocation, ResultListUtil.ACCENDING);
            else
                ResultListUtil.sortCatalogListBy(mDisplayCatalogList, mCurrentLocation, ResultListUtil.DECENDING);
            mRecycleAdapter.notifyDataSetChanged();
        } else {
//            Toast.makeText(this, "The Search List is Empty", Toast.LENGTH_SHORT).show();
        }
    }


    /*Popup Menu Item Click*/
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.main_menu_sort:
                isSortByAscending = !isSortByAscending;
                setSortPopUpTitle(item);
                populateRecycleView();
                return true;
            case R.id.main_menu_distance:
                createPopUpSeekBar();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSortPopUpTitle(MenuItem menuItem) {
        menuItem.setTitle(isSortByAscending ?
                "Sort By: Ascending" : "Sort By: Descending");
    }

    private void setMenuDistance(MenuItem menuItem) {
        menuItem.setTitle("Filter Distance: " + distanceFilter + " KM");
    }

    private void createPopUpSeekBar() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView distanceTextView = new TextView(this);
        distanceTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        distanceTextView.setText(distanceFilter + "KM");

        SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(100);
        seekBar.setProgress(distanceFilter);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distanceTextView.setText(String.valueOf(progress) + "KM");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        TextView messageTextView = new TextView(this);
        messageTextView.setText("Set to 0 to turn off filter");
        messageTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(distanceTextView);
        linearLayout.addView(seekBar);
        linearLayout.addView(messageTextView);

        new AlertDialog.Builder(this)
                .setTitle("Filter Distance")
                .setView(linearLayout)
                .setPositiveButton("OK", (dialog, which) -> {
                    distanceFilter = seekBar.getProgress();
                    populateRecycleView();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
