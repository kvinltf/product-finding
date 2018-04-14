package com.example.productfinding;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {


    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        Button resetBtn = view.findViewById(R.id.reset_pass_btn_reset);
        resetBtn.setOnClickListener(v ->
                Toast.makeText(getContext(), getString(R.string.txt_reset_password), Toast.LENGTH_SHORT).show());
        return view;
    }

}
