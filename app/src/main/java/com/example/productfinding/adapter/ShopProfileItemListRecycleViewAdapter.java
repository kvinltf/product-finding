package com.example.productfinding.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.productfinding.R;
import com.example.productfinding.model.Item;

import java.util.List;

public class ShopProfileItemListRecycleViewAdapter extends RecyclerView.Adapter<ShopProfileItemListRecycleViewAdapter.ViewHolder> {
    private List<Item> mItemList;

    public ShopProfileItemListRecycleViewAdapter(List<Item> mItemList) {
        this.mItemList = mItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_holder_single_text, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String _itemName = mItemList.get(position).getName();

        holder.itemName.setText(_itemName);

    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.recycleview_holder_singletext_textview);
        }
    }
}
