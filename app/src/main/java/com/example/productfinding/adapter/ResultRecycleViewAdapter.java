package com.example.productfinding.adapter;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productfinding.MapPopupActivity;
import com.example.productfinding.R;
import com.example.productfinding.model.Catalog;
import com.example.productfinding.model.User;
import com.example.productfinding.util.IntentUtil;
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

        public ViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.search_result_layout_image);
            itemName = itemView.findViewById(R.id.search_result_layout_product_name);
            distanceToShop = itemView.findViewById(R.id.search_result_layout_distance);
            shopName = itemView.findViewById(R.id.search_result_layout_product_shop);

            itemView.setOnClickListener((View v) -> {
                Toast.makeText(itemView.getContext(), itemName.getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(v.getContext(), MapPopupActivity.class);
                i.putExtra("catalog",catalog);
                v.getContext().startActivity(i);
            });
        }
    }
}
