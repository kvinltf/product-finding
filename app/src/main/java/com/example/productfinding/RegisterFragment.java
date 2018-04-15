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
import android.widget.TextView;
import android.widget.Toast;

import com.example.productfinding.util.KeyboardUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";
    private FirebaseAuth mFirebaseAuth;

    private TextView mName;
    private TextView mEmail;
    private TextView mPassword;
    private View mView;
    private Button registerBtn;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Create Register Fragment");
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create Register Fragment View");

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_register, container, false);
        init();
        return mView;
    }

    private void init() {
        Log.d(TAG, "init: Initializing Value");
        mName = mView.findViewById(R.id.register_et_name);
        mEmail = mView.findViewById(R.id.register_et_email);
        mPassword = mView.findViewById(R.id.register_et_password);
        registerBtn = mView.findViewById(R.id.register_btn_register);

        registerBtn.setOnClickListener(v -> {
                    Log.d(TAG, "init: Register Button Clicked");
            KeyboardUtil.hideSoftKeyboard(getActivity());
                    registerUser();
                }
        );
    }

    private void registerUser() {
        Log.d(TAG, "registerUser: Registering User");

        if (!isEditTextFieldValid()) return;
        showProgressBar(true);

        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(registerTask -> {
                            if (registerTask.isSuccessful()) {
                                Log.d(TAG, "registerUser: Success");
                                Toast.makeText(getContext(), "Success Register", Toast.LENGTH_SHORT).show();
                                updateDisplayName();
                                backToLoginFragment();

                            } else {
                                Log.e(TAG, "registerUser: Fail", registerTask.getException());
                                Toast.makeText(getContext(), registerTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            showProgressBar(false);
                        }
                );
    }

    private void updateDisplayName() {
        Log.d(TAG, "updateDisplayName: Update User Display Name");
        String name = mName.getText().toString();

        mFirebaseAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build())
                .addOnCompleteListener(
                        updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Log.d(TAG, "updateDisplayName: Success Update Display Name");
                            } else
                                Log.e(TAG, "updateDisplayName: Fail Update Display Name", updateTask.getException());
                        }
                );
    }

    private boolean isEditTextFieldValid() {
        Log.d(TAG, "isEditTextFieldValid: Check is Edit Text Field Valid");
        String fieldRequiredErr = getString(R.string.err_field_required);

        if (mName.getText().toString().isEmpty()) {
            mName.setError(fieldRequiredErr);
            mName.requestFocus();
            return false;
        } else if (mEmail.getText().toString().isEmpty()) {
            mEmail.setError(fieldRequiredErr);
            mEmail.requestFocus();
            return false;
        } else if (!isValidEmail(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.err_wrong_email_format));
            mEmail.requestFocus();
            return false;
        } else if (mPassword.getText().toString().isEmpty()) {
            mEmail.setError(fieldRequiredErr);
            mEmail.requestFocus();
            return false;
        } else if (mPassword.getText().toString().length() < 6) {
            mPassword.setError(getString(R.string.err_password_length));
            mPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void showProgressBar(Boolean show) {
        mView.findViewById(R.id.progress_bar_layout).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public final static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void backToLoginFragment(){
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.login_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_login);
    }
}
