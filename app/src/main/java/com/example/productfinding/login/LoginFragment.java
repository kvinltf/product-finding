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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.productfinding.MainActivity;
import com.example.productfinding.R;
import com.example.productfinding.model.User;
import com.example.productfinding.util.EmailUtil;
import com.example.productfinding.util.IntentUtil;
import com.example.productfinding.util.KeyboardUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass that login user.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private TextView mEmail;
    private TextView mPassword;
    private Button mLoginBtn;
    private CheckBox mRememberMeCb;
    private View mView;
    private SharedPreferences sharedPreferences;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Create Login Fragment");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create Login Fragment View");

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login, container, false);

        initializeParameter();
        //check is user logged in? else Proceed to Login Process
        if (isUserExist()) {
            Toast.makeText(getContext(), "Logging In...Please Wait", Toast.LENGTH_SHORT).show();
            processExistUser();
        }

        return mView;
    }

    /**
     * Initialize Login Fragment's Variable and
     * <p>set Listener to Button</p>
     */
    private void initializeParameter() {
        Log.d(TAG, "initializeParameter: Initializing Variable");
        mEmail = mView.findViewById(R.id.login_et_email);
        mPassword = mView.findViewById(R.id.login_et_password);
        mLoginBtn = mView.findViewById(R.id.login_btn_login);
        mRememberMeCb = mView.findViewById(R.id.login_cb_remember_me);

        sharedPreferences = getContext().getSharedPreferences(getString(R.string.share_preference_current_user), Context.MODE_PRIVATE);

        mLoginBtn.setOnClickListener((View v) -> {
            Log.d(TAG, "Login Button CLicked");
            KeyboardUtil.hideSoftKeyboard(getActivity());

            if (isInputValid()) {
                userLogin();
            } else
                return;
        });
    }

    private void userLogin() {
        Log.d(TAG, "userLogin: Logging In");

        showProgressBar(true);

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String url = getString(R.string.url_user);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("action", "login");
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            Log.d(TAG, "userLogin: JSONException on putting parameter: " + e.getMessage());
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    try {
                        //Request SUCCESS
                        if (response.getString("status").equalsIgnoreCase("success")) {
                            Log.i(TAG, "onResponse: Success Verify User");
                            Log.d(TAG, "onResponse() called with: response = [" + response.toString() + "]");

                            JSONObject userResult = response.getJSONObject("result");
                            User currentUser = new User(
                                    userResult.getInt("id"),
                                    userResult.getString("name"),
                                    userResult.getString("email"),
                                    userResult.getString("password"),
                                    userResult.getString("created_on")
                            );

                            if (mRememberMeCb.isChecked()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("id", currentUser.getId());
                                editor.putString("name", currentUser.getName());
                                editor.putString("email", currentUser.getEmail());
                                editor.putString("password", currentUser.getPassword());
                                editor.putString("created_on", currentUser.getCreated_on().toString());
                                editor.commit();
                            }
                            startMainActivity(currentUser);
                        }
                        //Request FAIL
                        else if (response.getString("status").equalsIgnoreCase("fail")) {
                            Toast.makeText(getContext(), "Wrong Email or Password, Please Try Again", Toast.LENGTH_SHORT).show();
                            showProgressBar(false);
                        }
                    }
                    //Something Error Happen
                    catch (JSONException e) {
                        Log.d(TAG, "userLogin: Error --> JSONException: " + e.getMessage());
                        e.printStackTrace();
                        showProgressBar(false);
                    }
                },
                error -> {
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                    error.printStackTrace();
                    showProgressBar(false);
                }
        );
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    private void processExistUser() {
        Log.d(TAG, "processExistUser: User Exist, Processing");
        int id = sharedPreferences.getInt("id", 1);
        String name = sharedPreferences.getString("name", "UNKNOWN");
        String email = sharedPreferences.getString("email", "UNKNOW@UNKNOW.com");
        String password = sharedPreferences.getString("password", "UNKNOW");
        String created_on = sharedPreferences.getString("created_on", "2018-07-21 14:37:03");

        startMainActivity(new User(id, name, email, password, created_on));

    }

    /**
     * @return <ul>
     * <li>TRUE if all checking pass</li>
     * <li>FALSE if one of the condition fail</li>
     * </ul>
     */
    private boolean isInputValid() {
        Log.d(TAG, "isInputValid: Checking The User's Input Validity");
        if (mEmail.getText().toString().isEmpty()) {
            mEmail.setError(getString(R.string.err_field_required));
            mEmail.requestFocus();
            return false;
        } else if (!EmailUtil.isValidEmail(mEmail.getText().toString())) {
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
        } else
            return true;
    }

    private void startMainActivity(User user) {
        Log.d(TAG, "startMainActivity: Start Activity");
        Intent i = new Intent(getContext(), MainActivity.class);
        IntentUtil.setLoginUserForIntent(i, user);
        startActivity(i);

        getActivity().finish();
    }

    private boolean isUserExist() {
        Log.d(TAG, "isUserExist: Check is User Exist");
        return !sharedPreferences.getAll().isEmpty();
    }

    /**
     * @param show <ul>
     *             <li>TRUE - Show Progress Bar</li>
     *             <li>FALSE - Dismiss Progress Bar</li>
     *             </ul>
     */
    private void showProgressBar(Boolean show) {
        mView.findViewById(R.id.progress_bar_layout).setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
