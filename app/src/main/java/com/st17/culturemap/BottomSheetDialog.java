package com.st17.culturemap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.st17.culturemap.databinding.ActivityMainBinding;
import com.st17.culturemap.objects.ImageRVAdapter;

import java.util.List;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    String name;
    String description;
    List<String> imageURLs;

    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        TextView place_name = (TextView) v.findViewById(R.id.place_name);
        place_name.setText(name);

        TextView place_description = (TextView) v.findViewById(R.id.place_description);
        place_description.setText(description);


        RecyclerView recyclerView = v.findViewById(R.id.BottomSheet_RV);

        RecyclerViewLayoutManager = new LinearLayoutManager(v.getContext());

        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        ImageRVAdapter rvAdapter = new ImageRVAdapter(inflater, imageURLs);

        HorizontalLayout = new LinearLayoutManager(v.getContext(),LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(HorizontalLayout);

        recyclerView.setAdapter(rvAdapter);


        return v;
    }
}