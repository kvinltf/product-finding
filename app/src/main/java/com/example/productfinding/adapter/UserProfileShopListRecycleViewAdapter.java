package com.example.productfinding.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.productfinding.R;
import com.example.productfinding.ShopProfileActivity;
import com.example.productfinding.model.Shop;
import com.example.productfinding.util.IntentUtil;

import java.util.List;

public class UserProfileShopListRecycleViewAdapter extends RecyclerView.Adapter<UserProfileShopListRecycleViewAdapter.ViewHolder> {
    private static final String TAG = "ProfileShopRecycleViewA";

    private List<Shop> mShopList;

    public UserProfileShopListRecycleViewAdapter(List<Shop> mShopList) {
        this.mShopList = mShopList;
    }

    @NonNull
    @Override
    public UserProfileShopListRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_holder_single_text, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserProfileShopListRecycleViewAdapter.ViewHolder holder, int position) {
        String _shopName = mShopList.get(position).getName();

        holder.shop = mShopList.get(position);
        holder.shopName.setText(_shopName);
    }

    @Override
    public int getItemCount() {
        return mShopList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName;//TextView shopDesc;
        Shop shop;

        public ViewHolder(View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.recycleview_holder_singletext_textview);
            itemView.setOnClickListener(v -> {
                Intent i = new Intent(v.getContext(), ShopProfileActivity.class);
                IntentUtil.setCurrentSelectedShopForIntent(i, shop);
                v.getContext().startActivity(i);
            });
        }
    }
}
