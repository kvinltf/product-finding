package com.example.productfinding.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.productfinding.R;
import com.example.productfinding.model.Shop;

import java.util.List;

public class ProfileShopRecycleViewAdapter extends RecyclerView.Adapter<ProfileShopRecycleViewAdapter.ViewHolder> {
    private static final String TAG = "ProfileShopRecycleViewA";

    private List<Shop> mShopList;

    public ProfileShopRecycleViewAdapter(List<Shop> mShopList) {
        this.mShopList = mShopList;
    }

    @NonNull
    @Override
    public ProfileShopRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_shop_recycle_view_holder, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileShopRecycleViewAdapter.ViewHolder holder, int position) {
        String _shopName = mShopList.get(position).getName();
        String _desc = mShopList.get(position).getDescription();

        holder.shopName.setText(_shopName);
//        holder.shopDesc.setText(_desc);
    }

    @Override
    public int getItemCount() {
        return mShopList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView shopName;//TextView shopDesc;

        public ViewHolder(View itemView){
            super(itemView);

            shopName = itemView.findViewById(R.id.profile_shop_name);
//            shopDesc = itemView.findViewById(R.id.profile_shop_description);
        }
    }
}
