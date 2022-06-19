package com.st17.culturemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    public static String type;
    public static String collection;
    public static String title;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvobjects);

        TextView textView = findViewById(R.id.textView_rvobject_title);
        textView.setText(title);

        loadObjects(collection, type);
    }

    private void loadObjects(String collection, String type) {
        objectsRV = findViewById(R.id.idRVObjects);

        db.collection(collection).whereEqualTo("type", type).get()
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
                            object.descriptionShort = info.get("description_short").toString();
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

            bottomSheetDialog.object = object;

            bottomSheetDialog.show(getSupportFragmentManager(), "ModalBottomSheet");
        }
    };

    public boolean ReturnMainClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    public boolean OpenMapClick(View view) {
        MapActivity.types = new ArrayList<>();
        MapActivity.types.add(type);
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        return true;
    }
}