package com.example.productfinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddNewShop extends AppCompatActivity {
    private static final String TAG = "AddNewShop";

    private EditText mShopName, mShopDesc;
    private TextView mLat, mLng;
    private Button mSubmit;

    private double lat, lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shop);


        String shopName = getIntent().getStringExtra("shop_name");
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        mShopName = findViewById(R.id.add_shop_et_shop_name);
        mShopDesc = findViewById(R.id.add_shop_et_shop_desc);
        mLat = findViewById(R.id.add_shop_tv_latitude);
        mLng = findViewById(R.id.add_shop_tv_longitude);
        mSubmit = findViewById(R.id.add_shop_btn_submit);

        mShopName.setText(shopName);
        mLat.setText(String.valueOf(lat));
        mLng.setText(String.valueOf(lng));

        mSubmit.setOnClickListener(v -> {
            if (mShopName.getText().toString().trim().isEmpty()) {
                mShopName.setError("This Field is required");
                return;
            }
            setButtonAvailability(mSubmit, false);
            addNewShop();
        });
    }

    private void addNewShop() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("action", "addNewShop");
            jsonObject.put("shop_name", mShopName.getText().toString().trim());
            jsonObject.put("shop_desc", mShopDesc.getText().toString().trim());
            jsonObject.put("lat", lat);
            jsonObject.put("lng", lng);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST, getString(R.string.url_shop), jsonObject,
                response -> {

                    try {
                        if (response.getString("status").equalsIgnoreCase("success")) {
                            Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            setButtonAvailability(mSubmit, true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                , Throwable::printStackTrace);

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void setButtonAvailability(Button button, boolean isEnable) {
        button.setEnabled(isEnable);
        button.setClickable(isEnable);
    }
}
