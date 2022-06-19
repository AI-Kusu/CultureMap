package com.st17.culturemap;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.st17.culturemap.SQLite.DBSQLiteHelper;
import com.st17.culturemap.objects.ImageRVAdapter;
import com.st17.culturemap.objects.PlaceObject;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    public PlaceObject object;

    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;

    FloatingActionButton moreFab, likeFab, addToPathFab;
    boolean isFABsVisible;

    DBSQLiteHelper dbsqLiteHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        TextView place_name = v.findViewById(R.id.textView_place_name);
        place_name.setText(object.name);

        TextView place_description = v.findViewById(R.id.place_description);
        place_description.setText(object.description);

        // RV
        RecyclerView recyclerView = v.findViewById(R.id.BottomSheet_RV);
        RecyclerViewLayoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);
        ImageRVAdapter rvAdapter = new ImageRVAdapter(inflater, object.imageURLs);
        HorizontalLayout = new LinearLayoutManager(v.getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(rvAdapter);

        //db
        dbsqLiteHelper = new DBSQLiteHelper(v.getContext());

        // FAB
        moreFab = v.findViewById(R.id.fab_more);
        likeFab = v.findViewById(R.id.fab_like);
        addToPathFab = v.findViewById(R.id.fab_addToPath);

        likeFab.setVisibility(View.GONE);
        addToPathFab.setVisibility(View.GONE);
        isFABsVisible = false;

        moreFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABsVisible) {
                    likeFab.show();
                    addToPathFab.show();

                    isFABsVisible = true;
                } else {
                    likeFab.hide();
                    addToPathFab.hide();

                    isFABsVisible = false;
            }
        }});

        likeFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SQLiteDatabase database = dbsqLiteHelper.getWritableDatabase();

                        ContentValues contentValues = new ContentValues();

                        contentValues.put(DBSQLiteHelper.KEY_NAME, object.name);
                        contentValues.put(DBSQLiteHelper.KEY_TYPE, object.type);
                        contentValues.put(DBSQLiteHelper.KEY_DESCRIPTION_SHORT, object.descriptionShort);
                        contentValues.put(DBSQLiteHelper.KEY_DESCRIPTION, object.description);
                        contentValues.put(DBSQLiteHelper.KEY_IMAGE_URL, object.imageURLs.get(0));

                        database.insert(DBSQLiteHelper.TABLE_FAVOURIRE, null, contentValues);

                        Toast.makeText(v.getContext(), "Add to favourite", Toast.LENGTH_SHORT).show();
                    }
                });


        addToPathFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(v.getContext(), "Alarm Added", Toast.LENGTH_SHORT).show();
                    }
                });

        return v;
    }
}