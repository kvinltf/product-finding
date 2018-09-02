package com.example.productfinding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.productfinding.model.Brand;
import com.example.productfinding.model.Category;
import com.example.productfinding.model.ResponseObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateItemActivity extends AppCompatActivity {
    private static final String TAG = "CreateItemActivity";
    private static final String RETRIEVE_ALL = "retrieveAll";
    private static final String CREATE_NEW = "createNew";

    private List<Category> categoryList;
    private List<Brand> brandList;

    private EditText mItemName, mItemDesc;
    private Spinner mCategorySpinner, mBrandSpinner;
    private Button mSubmitButton, mAddBrand;

    private ObjectMapper objectMapper;

    private boolean isBrandListReady, isCategoryListReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        init();
        initializeCategoryList();
        initializeBrandList();


    }

    private void init() {
        objectMapper = new ObjectMapper();
        categoryList = new ArrayList<>();
        brandList = new ArrayList<>();

        isBrandListReady = isCategoryListReady = false;

        mItemName = findViewById(R.id.add_item_et_item_name);
        mItemDesc = findViewById(R.id.add_item_et_item_desc);
        mCategorySpinner = findViewById(R.id.add_item_spinner_category);
        mBrandSpinner = findViewById(R.id.add_item_spinner_brand);
        mAddBrand = findViewById(R.id.add_item_btn_add_brand);

        mSubmitButton = findViewById(R.id.add_item_btn_submit);
        mSubmitButton.setOnClickListener(v -> {
            if (mItemName.getText().toString().trim().isEmpty()) {
                mItemName.setError("This Field is Required");
                return;
            }

            int brandId = brandList.get(mBrandSpinner.getSelectedItemPosition()).getId();
            int categoryId = categoryList.get(mCategorySpinner.getSelectedItemPosition()).getId();

            String itemName = mItemName.getText().toString().trim();
            String itemDesc = mItemDesc.getText().toString().trim();

            addNewItem(itemName, itemDesc, brandId, categoryId);
        });

        mAddBrand.setOnClickListener(v -> {
            Intent i = new Intent(this, CreateBrandActivity.class);
            startActivity(i);
        });
        isListReady();

    }

    private void addNewItem(String itemName, String itemDesc, int brandId, int categoryId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", CREATE_NEW);
            jsonObject.put("item_name", itemName);
            jsonObject.put("item_desc", itemDesc);
            jsonObject.put("brand_id", brandId);
            jsonObject.put("category_id", categoryId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                JsonObjectRequest.Method.POST, getString(R.string.url_item), jsonObject,
                response -> {
                    try {
                        Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        if (response.getString("status").equalsIgnoreCase("success")) {
                            mItemName.setText("");
                            mItemDesc.setText("");
                            recreate();
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void initializeCategoryList() {
        Log.d(TAG, "initializeCategoryList: START");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "retrieveAll");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                JsonObjectRequest.Method.POST, getString(R.string.url_category), jsonObject,
                response -> {
                    try {
                        ResponseObject<List<Category>> responseObject = objectMapper.readValue(response.toString(), new TypeReference<ResponseObject<List<Category>>>() {
                        });
                        if (responseObject.isStatusSuccess()) {
                            categoryList.addAll(responseObject.getQuery_result());
                            List<String> categoryNameList = new ArrayList<>();
                            for (Category c : categoryList) {
                                categoryNameList.add(c.getName());
                            }
                            ArrayAdapter<String> categoryArrayAdapter =
                                    new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categoryNameList);
                            categoryArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            mCategorySpinner.setAdapter(categoryArrayAdapter);

                            isCategoryListReady = true;
                            isListReady();
                        } else
                            Toast.makeText(this, responseObject.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.d(TAG, "initializeCategoryList: ERROR");
                        e.printStackTrace();
                    }

                },
                Throwable::printStackTrace
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void initializeBrandList() {
        Log.d(TAG, "initializeBrandList: START");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "retrieveAllBrand");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                JsonObjectRequest.Method.POST, getString(R.string.url_brand), jsonObject,
                response -> {
                    try {
                        ResponseObject<List<Brand>> responseObject = objectMapper.readValue(response.toString(), new TypeReference<ResponseObject<List<Brand>>>() {
                        });
                        if (responseObject.isStatusSuccess()) {
                            brandList.addAll(responseObject.getQuery_result());

                            List<String> brandNameList = new ArrayList<>();
                            for (Brand b : brandList) {
                                brandNameList.add(b.getName());
                            }
                            ArrayAdapter<String> arrayAdapter =
                                    new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, brandNameList);
                            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            mBrandSpinner.setAdapter(arrayAdapter);

                            isBrandListReady = true;
                            isListReady();
                        } else
                            Toast.makeText(this, responseObject.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, "initializeBrandList: ERROR");
                    }
                },
                Throwable::printStackTrace
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void isListReady() {
        if (isBrandListReady && isCategoryListReady) {
            mSubmitButton.setClickable(true);
            mSubmitButton.setEnabled(true);
        } else {
            mSubmitButton.setClickable(false);
            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}
