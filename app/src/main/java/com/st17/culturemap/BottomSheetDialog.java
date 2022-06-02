package com.st17.culturemap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.st17.culturemap.databinding.ActivityMainBinding;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    String name;
    String description;
    String image = "https://firebasestorage.googleapis.com/v0/b/culturemap-d04ec.appspot.com/o/images%2FXXXL.jpg?alt=media&token=520528d3-159b-400e-a13a-75276555fddf";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        TextView place_name = (TextView) v.findViewById(R.id.place_name);
        place_name.setText(name);

        TextView place_description = (TextView) v.findViewById(R.id.place_description);
        place_description.setText(description);

        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

        Picasso.get().load(image).into(imageView);

        return v;
    }
}