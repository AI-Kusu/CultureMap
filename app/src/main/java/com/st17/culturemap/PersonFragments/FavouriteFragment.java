package com.st17.culturemap.PersonFragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.st17.culturemap.BottomSheetDialog;
import com.st17.culturemap.R;
import com.st17.culturemap.RVObjectsActivity;
import com.st17.culturemap.SQLite.DBSQLiteHelper;
import com.st17.culturemap.databinding.FragmentPersonFavouriteBinding;
import com.st17.culturemap.objects.ObjectRVAdapter;
import com.st17.culturemap.objects.PlaceObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class FavouriteFragment extends Fragment {

    ObjectRVAdapter adapter;
    private RecyclerView objectsRV;
    List<PlaceObject> objects = new ArrayList<>();

    DBSQLiteHelper dbsqLiteHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_person_favourite, container, false);

        objectsRV = v.findViewById(R.id.favourite_list);
        dbsqLiteHelper = new DBSQLiteHelper(v.getContext());

        loadFavourites();

        return v;
    }

    public void loadFavourites() {

        db = dbsqLiteHelper.getReadableDatabase();

        userCursor = db.rawQuery("select * from " + DBSQLiteHelper.TABLE_FAVOURIRE, null);

        userCursor.moveToFirst();

        if(!userCursor.isAfterLast()){
            do{
                PlaceObject object = new PlaceObject();

                object.name = userCursor.getString(1);
                object.type = userCursor.getString(2);
                object.descriptionShort = userCursor.getString(3);
                object.description = userCursor.getString(4);
                object.imageURLs = new ArrayList<>();
                object.imageURLs.add(userCursor.getString(5));

                objects.add(object);
            }
            while (userCursor.moveToNext());
        }

        objectsRV.setLayoutManager(new LinearLayoutManager(v.getContext()));
        adapter = new ObjectRVAdapter(v.getContext(), objects, objectClickListener);
        objectsRV.setAdapter(adapter);
    }

    ObjectRVAdapter.OnObjectClickListener objectClickListener = new ObjectRVAdapter.OnObjectClickListener() {
        @Override
        public void onObjectClick(PlaceObject object, int position) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();

            bottomSheetDialog.object = object;

            bottomSheetDialog.show(getFragmentManager(), "ModalBottomSheet");
        }
    };

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FavouriteFragment.this)
                        .navigate(R.id.action_favouriteFragment_to_personContentFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        v = null;
    }
}