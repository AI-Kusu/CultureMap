package com.st17.culturemap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    String name;
    String description;

    public void onCreate(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet_layout,
                container, false);

        TextView place_name = (TextView) v.findViewById(R.id.place_name);
        place_name.setText(name);

        TextView place_description = (TextView) v.findViewById(R.id.place_description);
        place_description.setText(description);

        return v;
    }


}