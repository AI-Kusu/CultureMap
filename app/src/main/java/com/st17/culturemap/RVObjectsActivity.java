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

        loadNote();
    }

    public void loadNote() {
        objectsRV = findViewById(R.id.idRVObjects);

        db.collection("PlaceObject").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        int i = 1;
                        for (DocumentSnapshot d : list) {

                            PlaceObject object1 = new PlaceObject();

                            object1.name = "test" + i;
                            object1.description = "des" + i;
                            object1.point = new Point(0,0);

                            objects.add(object1);

                            i += 1;

                            PlaceObject object = new PlaceObject();

                            Map<String, Object> info = d.getData();
                            Map<String, Object> location = (Map<String, Object>) d.get("point");

                            object.name = info.get("name").toString();
                            object.description = info.get("description").toString();
                            object.point = new Point(Double.parseDouble(location.get("latitude").toString()), Double.parseDouble(location.get("longitude").toString()));


                            objects.add(object);

                        }

                        objectsRV.setLayoutManager(new LinearLayoutManager(RVObjectsActivity.this));
                        adapter = new ObjectRVAdapter(RVObjectsActivity.this, objects);
                        objectsRV.setAdapter(adapter);

                    }
                });
    }

    public void ReturnMainClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}