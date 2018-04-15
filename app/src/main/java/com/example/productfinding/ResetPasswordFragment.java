package com.example.productfinding;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.productfinding.util.KeyboardUtil;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {
    private static final String TAG = "ResetPasswordFragment";
    private FirebaseAuth mFirebaseAuth;
    private EditText mEmail;
    private Button mResetBtn;
    private View mView;


    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Create Reset Password Fragment");
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_reset_password, container, false);
        init();
        return mView;
    }

    private void init() {
        Log.d(TAG, "init: Initialize Variable");

        mEmail = mView.findViewById(R.id.reset_pass_et_email);

        mResetBtn = mView.findViewById(R.id.reset_pass_btn_reset);
        mResetBtn.setOnClickListener(v -> {
            Log.d(TAG, "init: Reset Button Clicked");
            KeyboardUtil.hideSoftKeyboard(getActivity());
            resetPassword();
        });
    }

    private void resetPassword() {
        Log.d(TAG, "resetPassword: Resetting Password");
        if (!isEditTextFieldValid()) return;

        showProgressBar(true);

        mFirebaseAuth.sendPasswordResetEmail(mEmail.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "resetPassword: Success");
                        Toast.makeText(getContext(), "Verification Email Send", Toast.LENGTH_SHORT).show();
                        backToLoginFragment();
                    } else {
                        Log.e(TAG, "resetPassword: Fail", task.getException());
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    showProgressBar(false);
                });
    }

    private boolean isEditTextFieldValid() {
        Log.d(TAG, "isEditTextFieldValid: Check is Edit Text Field Valid");
        String fieldRequiredErr = getString(R.string.err_field_required);

        if (mEmail.getText().toString().isEmpty()) {
            mEmail.setError(fieldRequiredErr);
            mEmail.requestFocus();
            return false;
        } else if (!isValidEmail(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.err_wrong_email_format));
            mEmail.requestFocus();
            return false;
        }
        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void showProgressBar(Boolean show) {
        mView.findViewById(R.id.progress_bar_layout).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void backToLoginFragment(){
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.login_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_login);
    }
}
