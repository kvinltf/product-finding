package com.example.productfinding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private FirebaseAuth mFirebaseAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // TODO: 4/15/2018
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            //user is signed in
            /*updateUI(currentUser);*/

        } else {
            //no user is signedin
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        TextView mEmail = view.findViewById(R.id.login_et_email);
        TextView mPassword = view.findViewById(R.id.login_et_password);

        Button loginBtn = view.findViewById(R.id.login_btn_login);
        loginBtn.setOnClickListener((View v) -> {
            Log.d(TAG, "onCreateView: Login Button Clicked");

            Toast.makeText(getContext(), "Hello, This is LOGIN Button", Toast.LENGTH_SHORT).show();


        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
