package com.example.productfinding.login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.productfinding.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public static final int PERMISSIONS_REQUEST_LOCATION = 99;

    public LoginActivity() {
    }

    /**
     * Use to change the Fragment in layout: Activity Login
     *
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.login_layout_content, fragment).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main_layout);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.login_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener((MenuItem item) -> {
                    switch (item.getItemId()) {
                        case R.id.bottom_navigation_login:
                            replaceFragment(new LoginFragment());
                            return true;
                        case R.id.bottom_navigation_register:
                            replaceFragment(new RegisterFragment());
                            return true;
                        case R.id.bottom_navigation_forget_password:
                            replaceFragment(new ResetPasswordFragment());
                            return true;
                    }
                    return false;
                }
        );
        bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_login);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted

                } else {
                    //permission not granted
//                    boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        new AlertDialog.Builder(this)
                                .setTitle("Location Permission")
                                .setMessage("This Application needs Location Permission to Function Normally." +
                                        "\n\nPlease go to setting to enable the Location Permissions" +
                                        "\n\nUnder \"Permission\" --> \"Location\"")
                                .setPositiveButton("I understand", (dialog, which) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }).show();
                    } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
                    }
                }
                return;
            }
        }
    }
}
