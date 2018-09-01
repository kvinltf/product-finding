package com.example.productfinding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.productfinding.adapter.ShopProfileItemListRecycleViewAdapter;
import com.example.productfinding.model.Item;
import com.example.productfinding.model.ResponseObject;
import com.example.productfinding.model.Shop;
import com.example.productfinding.model.User;
import com.example.productfinding.util.IntentUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShopProfileActivity extends AppCompatActivity {
    private static final String TAG = "ShopProfileActivity";
    private static final String ITEM_LIST_IN_SHOP = "itemListInShop";

    private Shop mCurrentShop;
    private User mCurrentUser;
    private Button mAddShopButton;
    private TextView mShopName;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecycleAdapter;
    final private ObjectMapper objectMapper = new ObjectMapper();

    private String url;
    private List<Item> mItemListInCatalog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.shop_profile_activity_main_layout);
        mCurrentUser = IntentUtil.getLoginUserFromIntent(getIntent());
        mCurrentShop = IntentUtil.getCurrentShopFromIntent(getIntent());

        url = getString(R.string.url_catalog);

        mShopName = findViewById(R.id.shop_profile_tv_shop_name);
        mShopName.setText(mCurrentShop.getName());

        mAddShopButton = findViewById(R.id.shop_profile_btn_add_item);
        mAddShopButton.setOnClickListener(v -> {
            //todo Add Function
            Intent i = new Intent(this, AddItemToShopActivity.class);
            IntentUtil.setCurrentSelectedShopForIntent(i, mCurrentShop);
            startActivity(i);
        });

        initializeRecycleView();
        getItemListInCatalog();
    }

    private void initializeRecycleView() {
        //RECYCLE VIEW THINGS
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = findViewById(R.id.shop_profile_recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecycleAdapter = new ShopProfileItemListRecycleViewAdapter(mItemListInCatalog);
        mRecyclerView.setAdapter(mRecycleAdapter);
    }

    private void getItemListInCatalog() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("action", ITEM_LIST_IN_SHOP);
            jsonObject.put("shop_id", mCurrentShop.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                JsonObjectRequest.Method.POST, url, jsonObject,
                response -> {
                    try {
                        ResponseObject<List<Item>> responseObject = objectMapper.readValue(response.toString(), new TypeReference<ResponseObject<List<Item>>>() {
                        });
                        if (responseObject.isStatusSuccess()) {
                            if (responseObject.getQuery_result() == null || responseObject.getQuery_result().isEmpty()) {
                                Toast.makeText(this, "This Shop Do Not Have any Item", Toast.LENGTH_SHORT).show();
                            } else {
                                populateRecycleView(responseObject.getQuery_result());
                            }
                        }
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                },
                Throwable::printStackTrace
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void populateRecycleView(List<Item> itemList) {
        mItemListInCatalog.clear();
        mItemListInCatalog.addAll(itemList);
        mRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
        recreate();
    }

}