package com.example.productfinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateBrandActivity extends AppCompatActivity {
    private static final String TAG = "CreateBrandActivity";
    private static final String ADD_NEW_BRAND = "addNewBrand";
    public static final String RETRIEVE_ALL_BRAND = "retrieveAllBrand";

    private EditText mBrandName, mBrandDescription;
    private Button mSubmitButton;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_brand);

        url = getString(R.string.url_brand);

        mBrandDescription = findViewById(R.id.add_brand_et_brand_desc);
        mBrandName = findViewById(R.id.add_brand_et_brand_name);
        mSubmitButton = findViewById(R.id.add_brand_btn_submit);

        mSubmitButton.setOnClickListener(v ->
        {
            setButtonAvailability(mSubmitButton, false);
            addNewBrand(mBrandName.getText().toString(), mBrandDescription.getText().toString());
        });
    }

    private void setButtonAvailability(Button button, boolean isEnable) {
        button.setEnabled(isEnable);
        button.setClickable(isEnable);
    }

    private void addNewBrand(String brandName, String brandDescription) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("action", ADD_NEW_BRAND);
            jsonObject.put("brand_name", brandName.trim());
            jsonObject.put("brand_description", brandDescription.trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        if (response.getString("status").equalsIgnoreCase("Success")) {
                            mBrandName.setText("");
                            mBrandDescription.setText("");
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setButtonAvailability(mSubmitButton, true);
                },
                Throwable::printStackTrace);
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}
