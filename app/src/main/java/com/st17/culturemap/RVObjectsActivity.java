package com.st17.culturemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.st17.culturemap.objects.ObjectRVAdapter;
import com.st17.culturemap.objects.PlaceObject;
import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RVObjectsActivity extends AppCompatActivity{

    ObjectRVAdapter adapter;
    private RecyclerView objectsRV;
    List<PlaceObject> objects = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvobjects);



        loadEvents();
    }

    public void loadEvents() {
        objectsRV = findViewById(R.id.idRVObjects);

        db.collection("Events").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {

                            PlaceObject object = new PlaceObject();

                            Map<String, Object> info = d.getData();
                            Map<String, Object> location = (Map<String, Object>) d.get("point");

                            object.name = info.get("name").toString();
                            object.description = info.get("description").toString();
                            object.imageURLs = (ArrayList<String>) d.get("imageURLs");
                            object.point = new Point(Double.parseDouble(location.get("latitude").toString()), Double.parseDouble(location.get("longitude").toString()));

                            objects.add(object);
                        }



                        objectsRV.setLayoutManager(new LinearLayoutManager(RVObjectsActivity.this));
                        adapter = new ObjectRVAdapter(RVObjectsActivity.this, objects, objectClickListener);
                        objectsRV.setAdapter(adapter);

                    }
                });
    }

    ObjectRVAdapter.OnObjectClickListener objectClickListener = new ObjectRVAdapter.OnObjectClickListener() {
        @Override
        public void onObjectClick(PlaceObject object, int position) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();

            bottomSheetDialog.name = object.name;
            bottomSheetDialog.description = object.description;
            bottomSheetDialog.imageURLs = object.imageURLs;

            bottomSheetDialog.show(getSupportFragmentManager(), "ModalBottomSheet");
        }
    };

    public void ReturnMainClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}