package com.example.productfinding;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.CheckBox;
import android.widget.Switch;

public class CustomDialog extends AlertDialog {

    //filter condition
    private CheckBox mItemNameCheckBox, mItemDescCheckBox, mShopNameCheckBox, mShopDescCheckBox, mBrandNameCheckBox, mBrandDescCheckBox, mCategoryCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    protected CustomDialog(@NonNull Context context) {
        super(context);
    }

    protected CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    private void setFilterCheckBox(boolean isEnable) {
        mItemNameCheckBox.setChecked(isEnable);
        mItemNameCheckBox.setEnabled(isEnable);
        mItemNameCheckBox.setClickable(isEnable);

        mItemDescCheckBox.setChecked(isEnable);
        mItemDescCheckBox.setEnabled(isEnable);
        mItemDescCheckBox.setClickable(isEnable);

        mShopNameCheckBox.setChecked(isEnable);
        mShopNameCheckBox.setEnabled(isEnable);
        mShopNameCheckBox.setClickable(isEnable);

        mShopDescCheckBox.setChecked(isEnable);
        mShopDescCheckBox.setEnabled(isEnable);
        mShopDescCheckBox.setClickable(isEnable);

        mBrandNameCheckBox.setChecked(isEnable);
        mBrandNameCheckBox.setEnabled(isEnable);
        mBrandNameCheckBox.setClickable(isEnable);

        mBrandDescCheckBox.setChecked(isEnable);
        mBrandDescCheckBox.setEnabled(isEnable);
        mBrandDescCheckBox.setClickable(isEnable);

        mCategoryCheckBox.setChecked(isEnable);
        mCategoryCheckBox.setEnabled(isEnable);
        mCategoryCheckBox.setClickable(isEnable);

    }
}
