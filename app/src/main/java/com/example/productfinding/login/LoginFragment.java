package com.example.productfinding.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.productfinding.MainActivity;
import com.example.productfinding.R;
import com.example.productfinding.model.User;
import com.example.productfinding.util.KeyboardUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

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
            KeyboardUtil.hideSoftKeyboard(getActivity());
            loginNewUser();
        });
    }

    private void processExistUser() {
        Log.d(TAG, "processExistUser: User Exist, Processing");
        loginExistUser();
    }

    private void signoutExistUser() {
        Log.d(TAG, "signoutExistUser: Sign Out Existing User");

    }

    private void loginExistUser() {
        Log.d(TAG, "loginExistUser: Login with Exist User");
        startMainActivity();
    }

    private void loginNewUser() {
        Log.d(TAG, "loginNewUser: Logging In");

        if (!isEditTextValid()) return;

        showProgressBar(true);
        //if (isUserExist()) signoutExistUser();

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        String url = getString(R.string.url_user);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action","login");
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    try {
                        if (response.getString("status").equalsIgnoreCase("success")) {
                            Log.i(TAG, "onResponse: Success Verify User");
                            Log.d(TAG, "onResponse() called with: response = [" + response.toString() + "]");
                            Toast.makeText(getContext(), "Success Login", Toast.LENGTH_SHORT).show();

                            JSONObject userResult = response.getJSONObject("result");

                            User currentUser = new User(userResult.getInt("id"), userResult.getString("name"), userResult.getString("email"), userResult.getString("password"), new java.util.Date(Timestamp.valueOf(userResult.getString("created_on")).getTime()));

                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.share_preference_name), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("id",currentUser.getId());
                            editor.putString("name",currentUser.getName());
                            editor.putString("email",currentUser.getEmail());
                            editor.putString("password",currentUser.getPassword());
                            editor.putString("created_on",currentUser.getCreated_on().toString());
                            editor.commit();


                        } else if (response.getString("status").equalsIgnoreCase("fail")) {
                            Toast.makeText(getContext(), "Wrong Email or Password, Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showProgressBar(false);
                },
                error -> {
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                    error.printStackTrace();
                    showProgressBar(false);
                }
        );
        Volley.newRequestQueue(getContext()).add(stringRequest);
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

        return false;
    }
}
