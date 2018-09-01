package com.example.productfinding;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.productfinding.adapter.UserProfileShopListRecycleViewAdapter;
import com.example.productfinding.model.ResponseObject;
import com.example.productfinding.model.Shop;
import com.example.productfinding.model.User;
import com.example.productfinding.util.IntentUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";

    private User mCurrentLoginUser;
    private TextView mUserName, mUserEmail;
    private Button mAddShopButton;
    private List<Shop> mAllShopList = new ArrayList<>();
    private List<Shop> mOwnShopList = new ArrayList<>();
    final private ObjectMapper objectMapper = new ObjectMapper();
    private String url;

    //recycle view things
    private RecyclerView.Adapter mRecycleAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity_main_layout);
        mCurrentLoginUser = IntentUtil.getLoginUserFromIntent(getIntent());

        mUserName = findViewById(R.id.profile_tv_user_name);
        mUserEmail = findViewById(R.id.profile_tv_user_email);
        mAddShopButton = findViewById(R.id.profile_btn_add_shop);

        mUserName.setText(mCurrentLoginUser.getName());
        mUserEmail.setText(mCurrentLoginUser.getEmail());

        mAddShopButton.setOnClickListener(addShopOnClickListener);
        url = getString(R.string.url_shop_owner);

        initializeRecycleView();
        getUserOwnedShop();
    }

    private void initializeRecycleView() {
        //RECYCLE VIEW THINGS

        mRecyclerView = findViewById(R.id.profile_rv_shop_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mRecycleAdapter = new UserProfileShopListRecycleViewAdapter(mOwnShopList);
        mRecyclerView.setAdapter(mRecycleAdapter);
    }


    private View.OnClickListener addShopOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAddShopButton.setEnabled(false);
            Log.d(TAG, "onClick: add Shop");

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("action", "retrieveNotOwnShopList");
                jsonObject.put("user_id", mCurrentLoginUser.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST,
                    url, jsonObject,
                    (JSONObject response) -> {
                        Log.d(TAG, "onClick: " + response.toString());

                        try {
                            ResponseObject<List<Shop>> responseObject = objectMapper.readValue(response.toString(), new TypeReference<ResponseObject<List<Shop>>>() {
                            });
                            if (responseObject.isStatusSuccess()) {

                                String[] shopLists = new String[responseObject.getQuery_result().size()];
                                for (int i = 0; i < responseObject.getQuery_result().size(); i++) {
                                    mAllShopList.add(responseObject.getQuery_result().get(i));
                                    shopLists[i] = responseObject.getQuery_result().get(i).getId() + ". " + responseObject.getQuery_result().get(i).getName();
                                }

                                ArrayList<Shop> checkedShop = new ArrayList<>();

                                new AlertDialog.Builder(v.getContext()).setTitle("Add Shop")
                                        .setMultiChoiceItems(shopLists, null, (dialog, which, isChecked) -> {
                                            if (isChecked) {
                                                checkedShop.add(mAllShopList.get(which));
                                            } else {
                                                checkedShop.remove(mAllShopList.get(which));
                                            }
                                        })
                                        .setPositiveButton("OK", (dialog, which) -> {
                                            addShopToUser(checkedShop);
                                        })
                                        .setNegativeButton("Cancle", null).show().setOnDismissListener(dialog -> mAddShopButton.setEnabled(true));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    },
                    Throwable::printStackTrace
            );
            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        }
    };

    private boolean addShopToUser(ArrayList<Shop> addShop) {
        boolean status = false;

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            for (Shop shop : addShop) {
                String tmpStr = objectMapper.writeValueAsString(shop);
                JSONObject jo = new JSONObject(tmpStr);
                jsonArray.put(jo);
            }
            jsonObject.put("action", "addshopowner");
            jsonObject.put("user", new JSONObject(objectMapper.writeValueAsString(mCurrentLoginUser)));
            jsonObject.put("shop_list", (Object) jsonArray);

            Log.d(TAG, "addShopToUser: " + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                JsonObjectRequest.Method.POST, url, jsonObject,
                response -> {
                    Log.d(TAG, "addShopToUser: " + response.toString());
                    getUserOwnedShop();
                },
                Throwable::printStackTrace
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
        return status;
    }

    private void getUserOwnedShop() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", "retrieveOwnShopList");
            jsonObject.put("user_id", mCurrentLoginUser.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST, url, jsonObject,
                response -> {
                    try {
                        ResponseObject<List<Shop>> responseObject = objectMapper.readValue(response.toString(), new TypeReference<ResponseObject<List<Shop>>>() {
                        });
                        if (responseObject.isStatusSuccess()) {
                            if (responseObject.getQuery_result() == null || responseObject.getQuery_result().isEmpty()) {
                                Toast.makeText(this, "You do not own any shop\nDo you wan to add one?", Toast.LENGTH_SHORT).show();
                            } else
                                populateRecycleView(responseObject.getQuery_result());
                        }else {
                            Toast.makeText(this, responseObject.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void populateRecycleView(List<Shop> shopList) {
            mOwnShopList.clear();
            for (int i = 0; i < shopList.size(); i++) {
                mOwnShopList.add(shopList.get(i));
            }
            mRecycleAdapter.notifyDataSetChanged();
    }

}
