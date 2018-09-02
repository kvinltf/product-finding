package com.example.productfinding.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.productfinding.MapPopupActivity;
import com.example.productfinding.R;
import com.example.productfinding.model.Catalog;
import com.example.productfinding.model.User;
import com.example.productfinding.util.LocationUtil;

import java.util.List;

public class ResultRecycleViewAdapter extends RecyclerView.Adapter<ResultRecycleViewAdapter.ViewHolder> {
    private static final String TAG = "ResutltRecycleViewAdapt";

    private List<Catalog> catalogList;
    private Location location;
    private User user;
    private String JSONData;


    // Provide a suitable constructor (depends on the kind of dataset)
    public ResultRecycleViewAdapter(List<Catalog> catalogList, Location location) {
        this.catalogList = catalogList;
        this.location = location;
    }

    @NonNull
    @Override
    public ResultRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_recycle_view_holder, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String _shopName = catalogList.get(position).getShop().getName();
        String _itemName = catalogList.get(position).getItem().getName();

        Location shopLocation = new Location("");
        shopLocation.setLatitude(catalogList.get(position).getShop().getLatitude());
        shopLocation.setLongitude(catalogList.get(position).getShop().getLongitude());

        if (location != null) {
            float distance = LocationUtil.distanceToKM(location.distanceTo(shopLocation));
            holder.distanceToShop.setText(String.format("%.2f", distance) + " KM");
        } else {
            holder.distanceToShop.setText("Unable to get location, check your connectivity.");
        }
        holder.shopName.setText(_shopName);
        holder.itemName.setText(_itemName);
        holder.catalog = catalogList.get(position);
    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, distanceToShop, shopName;
        Catalog catalog;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.search_result_layout_image);
            itemName = itemView.findViewById(R.id.search_result_layout_product_name);
            distanceToShop = itemView.findViewById(R.id.search_result_layout_distance);
            shopName = itemView.findViewById(R.id.search_result_layout_product_shop);

            itemView.setOnClickListener((View v) -> {
//                Toast.makeText(itemView.getContext(), itemName.getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();

                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext(),R.style.Theme_AppCompat)
                        .setTitle(catalog.getItem().getName())
                        .setView(R.layout.item_detail_layout)
                        .setPositiveButton("OK",null)
                        .setNeutralButton("Open Map", (dialog, which) -> {
                            Intent i = new Intent(v.getContext(), MapPopupActivity.class);
                            i.putExtra("catalog", catalog);
                            v.getContext().startActivity(i);
                        })
                        .create();

                alertDialog.show();
                TextView mBrandName, mCategoryName, mShopName, mItemDesc;

                mBrandName = alertDialog.findViewById(R.id.item_detail_tv_brand_name);
                mCategoryName = alertDialog.findViewById(R.id.item_detail_tv_category);
                mShopName = alertDialog.findViewById(R.id.item_detail_tv_shop_name);
                mItemDesc = alertDialog.findViewById(R.id.item_detail_tv_item_desc);

                mBrandName.setText(catalog.getItem().getBrand().getName());
                mCategoryName.setText(catalog.getItem().getCategory().getName());
                mShopName.setText(catalog.getShop().getName());
                mItemDesc.setText(catalog.getItem().getDescription());

            });
        }
    }
}
