package com.example.productfinding.login;


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

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.productfinding.R;
import com.example.productfinding.model.ResponseObject;
import com.example.productfinding.model.User;
import com.example.productfinding.util.EmailUtil;
import com.example.productfinding.util.KeyboardUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A {@link Fragment} subclass that register new user.
 */
public class RegisterFragment extends Fragment {
    private static final String TAG = "RegisterFragment";

    private TextView mName;
    private TextView mEmail;
    private TextView mPassword;
    private TextView mRePassword;
    private View mView;
    private Button registerBtn;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Create Register Fragment");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create Register Fragment View");

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.login_activity_fragment_resiger, container, false);
        initializeParameter();
        return mView;
    }

    /**
     * Initialize Login Fragment's Variable and
     * <p>set Listener to Button</p>
     */
    private void initializeParameter() {
        Log.d(TAG, "initializeParameter: Initializing Variable");

        mName = mView.findViewById(R.id.register_et_name);
        mEmail = mView.findViewById(R.id.register_et_email);
        mPassword = mView.findViewById(R.id.register_et_password);
        registerBtn = mView.findViewById(R.id.register_btn_register);
        mRePassword = mView.findViewById(R.id.register_et_repassword);

        registerBtn.setOnClickListener((View v) -> {
                    Log.d(TAG, "initializeParameter: Register Button Clicked");
                    KeyboardUtil.hideSoftKeyboard(getActivity());
                    if (!isInputValid()) return;
                    else registerUser();
                }
        );
    }

    private void registerUser() {
        Log.d(TAG, "registerUser: Registering User");

        showProgressBar(true);

        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();
        String name = mName.getText().toString();
        String url = getString(R.string.url_user);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("action", "register");
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            Log.d(TAG, "registerUser: JSONException " + e.getMessage());
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                response -> {
                    try {
                        final ObjectMapper objectMapper = new ObjectMapper();
                        ResponseObject responseObject = objectMapper.readValue(response.toString(),ResponseObject.class);

                        //Request SUCCESS
                        if (responseObject.isStatusSuccess()) {
                            Toast.makeText(getContext(), "Success Register New User", Toast.LENGTH_SHORT).show();
                            backToLoginFragment();
                        }
                        //Request FAIL
                        else {
                            Toast.makeText(
                                    getContext(),
                                    "Fail Register New User \nPlease Change Another Email Address",
                                    Toast.LENGTH_LONG).show();
                        }
                        Log.d(TAG, "registerUser() Message-> " + response.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        showProgressBar(false);
                    }
                },
                error -> {
                    Log.d(TAG, "registerUser: Error Listener-> " + error.getMessage());
                    error.printStackTrace();
                    showProgressBar(false);
                }
        );
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    private void updateDisplayName() {
        Log.d(TAG, "updateDisplayName: Update User Display Name");
        String name = mName.getText().toString();

        //todo: change to my custom methods
//        mFirebaseAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build())
//                .addOnCompleteListener(
//                        updateTask -> {
//                            if (updateTask.isSuccessful()) {
//                                Log.d(TAG, "updateDisplayName: Success Update Display Name");
//                            } else
//                                Log.e(TAG, "updateDisplayName: Fail Update Display Name", updateTask.getException());
//                        }
//                );
    }

    private boolean isInputValid() {
        Log.d(TAG, "isInputValid: Checking The User's Input Validity");
        String fieldRequiredErr = getString(R.string.err_field_required);

        if (mName.getText().toString().isEmpty()) {
            mName.setError(fieldRequiredErr);
            mName.requestFocus();
            return false;
        } else if (mEmail.getText().toString().isEmpty()) {
            mEmail.setError(fieldRequiredErr);
            mEmail.requestFocus();
            return false;
        } else if (!EmailUtil.isValidEmail(mEmail.getText().toString())) {
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
        } else if (!mRePassword.getText().toString().equalsIgnoreCase(mPassword.getText().toString())) {
            mRePassword.setError(getString(R.string.err_repassword));
            mRePassword.requestFocus();
        }
        return true;
    }

    private void showProgressBar(Boolean show) {
        mView.findViewById(R.id.progress_bar_layout).setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void backToLoginFragment() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.login_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_login);
    }
}
