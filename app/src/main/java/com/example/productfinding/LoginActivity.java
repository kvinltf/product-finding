package com.example.productfinding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_login:
                    mTextMessage.setText(R.string.bottom_navigation_title_login);
                    return true;
                case R.id.bottom_navigation_register:
                    mTextMessage.setText(R.string.bottom_navigation_title_register);
                    return true;
                case R.id.bottom_navigation_forget_password:
                    mTextMessage.setText(R.string.bottom_navigation_title_forget_password);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTextMessage = (TextView) findViewById(R.id.login_tv_message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.login_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
