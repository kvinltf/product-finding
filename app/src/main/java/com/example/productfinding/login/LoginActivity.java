package com.example.productfinding.login;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.productfinding.R;

public class LoginActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main_layout);

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
}
