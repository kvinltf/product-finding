package com.example.productfinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getDisplayName() != null) {
            Toast.makeText(getApplicationContext(), "You Login As: " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
        }else Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_SHORT).show();
    }
}
