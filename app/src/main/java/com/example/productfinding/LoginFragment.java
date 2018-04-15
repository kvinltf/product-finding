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

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private FirebaseAuth mFirebaseAuth;

    private TextView mEmail;
    private TextView mPassword;
    private Button mLoginBtn;
    private View mView;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Create Login Fragment");
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        if (isUserExist()) {
            processExistUser();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create Login Fragment View");

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        init();
        return mView;
    }

    private void init() {
        Log.d(TAG, "init: Initializing Variable");

        mEmail = mView.findViewById(R.id.login_et_email);
        mPassword = mView.findViewById(R.id.login_et_password);
        mLoginBtn = mView.findViewById(R.id.login_btn_login);

        mLoginBtn.setOnClickListener(v -> {
            Log.d(TAG, "init: Login Button CLicked");
            loginNewUser();
        });
    }
/*
    private void processExistUser() {
        Log.d(TAG, "processExistUser: User Exist, Processing");

        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("User Found");
        alertDialog.setMessage("Do you wan to Login as: " + currentUser.getDisplayName() + "?");
        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            loginExistUser();
        });
        alertDialog.setNegativeButton("No", ((dialog, which) -> {
            signoutExistUser();
        }));
        alertDialog.create();
        alertDialog.show();
    }*/

    private void processExistUser() {
        Log.d(TAG, "processExistUser: User Exist, Processing");
        loginExistUser();
    }

    private void signoutExistUser() {
        Log.d(TAG, "signoutExistUser: Sign Out Existing User");
        mFirebaseAuth.signOut();
    }

    private void loginExistUser() {
        Log.d(TAG, "loginExistUser: Login with Exist User");
        startMainActivity();
    }

    private void loginNewUser() {
        Log.d(TAG, "loginNewUser: Logging In");

        if (!isEditTextValid()) return;

        if (isUserExist()) signoutExistUser();

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        showProgressBar(true);

        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "loginNewUser: Success");
                Toast.makeText(getContext(), "Welcome, " + mFirebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                startMainActivity();
            } else {
                Log.e(TAG, "loginNewUser: Fail", task.getException());
                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                showProgressBar(false);
            }
        });

    }

    private void showProgressBar(Boolean show) {
        mView.findViewById(R.id.progress_bar_layout).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private boolean isEditTextValid() {
        Log.d(TAG, "isEditTextValid: Check Valid");
        if (mEmail.getText().toString().isEmpty()) {
            mEmail.setError(getString(R.string.err_field_required));
            mEmail.requestFocus();
            return false;
        } else if (!isValidEmail(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.err_wrong_email_format));
            mEmail.requestFocus();
            return false;
        } else if (mPassword.getText().toString().isEmpty()) {
            mPassword.setError(getString(R.string.err_field_required));
            mPassword.requestFocus();
            return false;
        } else if (mPassword.getText().toString().length() < 6) {
            mPassword.setError(getString(R.string.err_password_length));
            mPassword.requestFocus();
            return false;
        }
        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void startMainActivity() {
        Log.d(TAG, "startMainActivity: Start Activity");
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }

    private boolean isUserExist() {
        Log.d(TAG, "isUserExist: Check is User Exist");
        return (mFirebaseAuth.getCurrentUser() != null) ? true : false;
    }
}
