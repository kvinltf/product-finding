package com.example.productfinding.adapter;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productfinding.R;
import com.example.productfinding.model.Shop;
import com.example.productfinding.model.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResutltRecycleViewAdapter extends RecyclerView.Adapter<ResutltRecycleViewAdapter.ViewHolder> {
    private static final String TAG = "ResutltRecycleViewAdapt";

    private List<Shop> itemList;
    private Location location;
    private User user;
    private String JSONData;


    // Provide a suitable constructor (depends on the kind of dataset)
    public ResutltRecycleViewAdapter(List<Shop> itemList, Location location) {
        this.itemList = itemList;
        this.location = location;
    }

    @NonNull
    @Override
    public ResutltRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_recycle_view_holder, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String myString = itemList.get(position).getName();
        Location shopLocation = new Location("");
        shopLocation.setLatitude(itemList.get(position).getLatLng().latitude);
        shopLocation.setLongitude(itemList.get(position).getLatLng().longitude);

        if (location != null) {
            float distance = location.distanceTo(shopLocation);
            distance = distance / (float) 1000;

            holder.distance.setText(String.format("%.2f", distance) + " KM");
        } else {
            holder.distance.setText("Unable to get location, check your connectivity.");
        }

        holder.shop.setText(myString);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, distance, shop;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.search_result_layout_image);
            name = itemView.findViewById(R.id.search_result_layout_product_name);
            distance = itemView.findViewById(R.id.search_result_layout_distance);
            shop = itemView.findViewById(R.id.search_result_layout_product_shop);


            itemView.setOnClickListener((View v) -> {
                Toast.makeText(itemView.getContext(), name.getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
