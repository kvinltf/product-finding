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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {
    private static final String TAG = "ResetPasswordFragment";

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.login_activity_fragment_reset_password, container, false);
        initializeParameter();
        return mView;
    }

    private void initializeParameter() {
        Log.d(TAG, "initializeParameter: Initialize Variable");

        mEmail = mView.findViewById(R.id.reset_pass_et_email);
        mResetBtn = mView.findViewById(R.id.reset_pass_btn_reset);
        mResetBtn.setOnClickListener((View v) -> {
            Log.d(TAG, "initializeParameter: Reset Button Clicked");
            KeyboardUtil.hideSoftKeyboard(getActivity());
            resetPassword();
        });
    }

    private void resetPassword() {
        Log.d(TAG, "resetPassword: Resetting Password");
        if (!isInputValid()) return;

        showProgressBar(true);

        //reset password
        String email = mEmail.getText().toString();
        String url = getString(R.string.url_user);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("action", "forgetpassword");
            jsonObject.put("email", email);
        } catch (JSONException e) {
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

                        Toast.makeText(getContext(), responseObject.getMessage(), Toast.LENGTH_SHORT).show();
                        if (responseObject.isStatusSuccess()) {
                            backToLoginFragment();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        showProgressBar(false);
                    }
                },
                error -> {
                    Log.d(TAG, "JsonObjectRequest Error: " + error.getMessage());
                    error.printStackTrace();
                    showProgressBar(false);
                }
        );
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest).setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private boolean isInputValid() {
        Log.d(TAG, "isInputValid: Checking The User's Input Validity");
        String fieldRequiredErr = getString(R.string.err_field_required);

        if (mEmail.getText().toString().isEmpty()) {
            mEmail.setError(fieldRequiredErr);
            mEmail.requestFocus();
            return false;
        } else if (!EmailUtil.isValidEmail(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.err_wrong_email_format));
            mEmail.requestFocus();
            return false;
        } else
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
