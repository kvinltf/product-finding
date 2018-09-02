package com.example.productfinding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.productfinding.adapter.ShopProfileAddItemRecycleViewAdapter;
import com.example.productfinding.model.Item;
import com.example.productfinding.model.ResponseObject;
import com.example.productfinding.model.Shop;
import com.example.productfinding.util.IntentUtil;
import com.example.productfinding.util.KeyboardUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddItemToShopActivity extends AppCompatActivity {
    private static final String TAG = "AddItemToShopActivity";

    private static final String SAVE_ITEM_TO_CATALOG = "saveItemToShopCatalog";
    private static final String ITEM_NOT_IN_SHOP = "itemListNotInShop";
    private String url;

    final private ObjectMapper objectMapper = new ObjectMapper();
    private Shop mCurrentShop;
    private List<Item> itemListNotInCatalog = new ArrayList<>();
    private List<Item> mCheckedItemList = new ArrayList<>();
    private List<Item> fullItemList = new ArrayList<>();
    private TextView shopNameTV;
    private EditText filterEditText;
    private ImageView resetImageView;
    private Button addItemBtn, mNewItemBtn;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_profile_add_item_to_shop_layout);

        url = getString(R.string.url_catalog);
        mCurrentShop = IntentUtil.getCurrentShopFromIntent(getIntent());

        shopNameTV = findViewById(R.id.shop_profile_add_item_shop_name);
        addItemBtn = findViewById(R.id.shop_profile_add_item_btn_add_item);

        filterEditText = findViewById(R.id.shop_profile_add_item_filter_text);

        mNewItemBtn = findViewById(R.id.shop_profile_add_item_btn_new_item);
        mNewItemBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateItemActivity.class));
        });

        resetImageView = findViewById(R.id.shop_profile_add_item_reset_btn);
        resetImageView.setOnClickListener(v -> filterEditText.setText(""));

        shopNameTV.setText(mCurrentShop.getName());


        addItemBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Add " + mCheckedItemList.size() + " Items", Toast.LENGTH_SHORT).show();
            submitItemToDatabase();
        });


        initializeRecycleView();
        getItemListNotInCatalog();

        filterEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardUtil.hideSoftKeyboard(this);

                filterEditText.getText().toString();


                return true;
            }
            return false;
        });

        filterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });
    }

    private void submitItemToDatabase() {
        Log.d(TAG, "submitItemToDatabase: start");
        JSONObject jsonObject = new JSONObject();
        ArrayList<Integer> itemIdList = new ArrayList<>();

        for (Item i : mCheckedItemList) {
            itemIdList.add(i.getId());
        }
        try {
            jsonObject.put("action", SAVE_ITEM_TO_CATALOG);
            jsonObject.put("shop_id", mCurrentShop.getId());
            jsonObject.put("item_id_list", (Object) new JSONArray(itemIdList));
            Log.d(TAG, "submitItemToDatabase: " + jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST, url, jsonObject,
                response -> {
                    Log.d(TAG, "submitItemToDatabase: Response::" + response.toString());
                    recreate();
                },
                Throwable::printStackTrace);
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void filter(String filterText) {
        ArrayList<Item> filteredList = new ArrayList<>();
        filteredList.clear();
        if (filterText.isEmpty()) {
            filteredList.addAll(fullItemList);
        } else {
            for (Item item : fullItemList) {
                if (item.getName().toLowerCase().contains(filterText.toLowerCase())
                        || item.getDescription().toLowerCase().contains(filterText.toLowerCase())
                        || item.getBrand().getName().toLowerCase().contains(filterText.toLowerCase())
                        || item.getCategory().getName().toLowerCase().contains(filterText.toLowerCase())
                        ) {
                    filteredList.add(item);
                }
            }
        }
        populateRecycleView(filteredList);
    }

    private void getItemListNotInCatalog() {
        Log.d(TAG, "getItemListNotInCatalog: getting Item List");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action", ITEM_NOT_IN_SHOP);
            jsonObject.put("shop_id", mCurrentShop.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(JsonObjectRequest.Method.POST, url, jsonObject,
                response -> {
                    try {
                        ResponseObject<List<Item>> responseObject = objectMapper.readValue(
                                response.toString(), new TypeReference<ResponseObject<List<Item>>>() {
                                });
                        if (responseObject.isStatusSuccess()) {
                            if (responseObject.getQuery_result() == null || responseObject.getQuery_result().isEmpty()) {
                                Toast.makeText(this, "There Is no New Item to Add\nDo you wan to add one?", Toast.LENGTH_SHORT).show();
                            } else {
                                fullItemList.addAll(responseObject.getQuery_result());
                                populateRecycleView(fullItemList);
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
                Throwable::printStackTrace);
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void initializeRecycleView() {
        //RECYCLE VIEW THINGS
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = findViewById(R.id.shop_profile_add_item_rv);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecycleAdapter = new ShopProfileAddItemRecycleViewAdapter(itemListNotInCatalog, new ShopProfileAddItemRecycleViewAdapter.MyOnItemCheckListener() {
            @Override
            public void onItemCheck(Item item) {
                mCheckedItemList.add(item);
            }

            @Override
            public void onItemUncheck(Item item) {
                mCheckedItemList.remove(item);
            }
        });
        mRecyclerView.setAdapter(mRecycleAdapter);
    }

    private void populateRecycleView(List<Item> itemList) {
        itemListNotInCatalog.clear();
        for (int i = 0; i < itemList.size(); i++) {
            itemListNotInCatalog.add(itemList.get(i));
        }
        mRecycleAdapter.notifyDataSetChanged();
    }
}
