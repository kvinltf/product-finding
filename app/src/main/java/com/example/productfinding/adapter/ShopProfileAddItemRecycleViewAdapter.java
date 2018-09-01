package com.example.productfinding.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.productfinding.R;
import com.example.productfinding.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ShopProfileAddItemRecycleViewAdapter extends RecyclerView.Adapter<ShopProfileAddItemRecycleViewAdapter.ViewHolder> {

    public interface MyOnItemCheckListener {
        void onItemCheck(Item item);

        void onItemUncheck(Item item);
    }

    private List<Item> mItemList;
    private List<Item> mCheckedItemList;
    private List<Item> mItemListFiltered;
    @NonNull
    private MyOnItemCheckListener myOnItemCheckListener;


    public List<Item> getmCheckedItemList() {
        return mCheckedItemList;
    }

    public ShopProfileAddItemRecycleViewAdapter(List<Item> mItemList, MyOnItemCheckListener myOnItemCheckListener) {
        this.mItemList = mItemList;
        this.mItemListFiltered = mItemList;
        this.mCheckedItemList = new ArrayList<>();
        this.myOnItemCheckListener = myOnItemCheckListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_holder_checkbox, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String itemName = mItemListFiltered.get(position).getName();
        holder.checkBoxItem.setText(itemName);
        holder.desc.setText(mItemListFiltered.get(position).getDescription());
        holder.brandName.setText(mItemListFiltered.get(position).getBrand().getName());

        holder.checkBoxItem.setChecked(false);


        if (!mCheckedItemList.isEmpty()) {
            for (Item i : mCheckedItemList) {
                if (i.getId() == mItemListFiltered.get(position).getId()) {
                    holder.checkBoxItem.setChecked(true);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItemListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxItem;
        TextView desc, brandName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.checkBoxItem = itemView.findViewById(R.id.recycleview_holder_checkbox);
            this.desc = itemView.findViewById(R.id.recycleview_holder_desc);
            this.brandName = itemView.findViewById(R.id.recycleview_holder_brand);


            checkBoxItem.setClickable(false);
            itemView.setOnClickListener(v -> {
                if (!checkBoxItem.isChecked()) {
                    checkBoxItem.setChecked(true);
                    mCheckedItemList.add(mItemListFiltered.get(getAdapterPosition()));
                    myOnItemCheckListener.onItemCheck(mItemListFiltered.get(getAdapterPosition()));
                } else {
                    checkBoxItem.setChecked(false);
                    mCheckedItemList.remove(mItemListFiltered.get(getAdapterPosition()));
                    myOnItemCheckListener.onItemUncheck(mItemListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}
