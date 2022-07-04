package com.st17.culturemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.st17.culturemap.objects.ObjectRVAdapter;
import com.st17.culturemap.objects.PlaceObject;
import com.yandex.mapkit.geometry.Point;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RVObjectsActivity extends AppCompatActivity{

    public static String lastPage;

    ObjectRVAdapter adapter;
    private RecyclerView objectsRV;
    List<PlaceObject> objects = new ArrayList<>();

    public static String type;
    public static String collection;
    public static String title;
    public static String userList;

    public List<String> favourites;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvobjects);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        TextView textView = findViewById(R.id.textView_rvobject_title);
        textView.setText(title);

        if(type != null){
            loadObjectsByType(type);
        }else{
            loadFavouriteObjects();
        }

        objectsRV.setLayoutManager(new LinearLayoutManager(RVObjectsActivity.this));
        adapter = new ObjectRVAdapter(RVObjectsActivity.this, objects, objectClickListener);
        objectsRV.setAdapter(adapter);
    }

    private void loadObjectsByType(String type) {
        objectsRV = findViewById(R.id.idRVObjects);

        db.collection("PlaceObject").whereEqualTo("type", type).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot d : list) {

                            PlaceObject object = new PlaceObject();

                            Map<String, Object> info = d.getData();
                            Map<String, Object> location = (Map<String, Object>) d.get("point");

                            object.id = d.getId();
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

    private void loadFavouriteObjects() {
        objectsRV = findViewById(R.id.idRVObjects);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dbUser = db.collection("users").document(currentUser.getUid());

        dbUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    favourites = (List<String>) document.get(userList);

                    for (String name : favourites) {
                        addObject(name);
                    }
                }
            }
        });
    }

    private void addObject(String value) {
        db.collection("PlaceObject").document(value)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot d = task.getResult();

                            PlaceObject object = new PlaceObject();
                            Map<String, Object> info = d.getData();

                            object.id = d.getId();
                            object.name = info.get("name").toString();
                            object.description = info.get("description").toString();
                            object.descriptionShort = info.get("description_short").toString();
                            object.imageURLs = (ArrayList<String>) d.get("imageURLs");

                            objects.add(object);

                            objectsRV.setLayoutManager(new LinearLayoutManager(RVObjectsActivity.this));
                            adapter = new ObjectRVAdapter(RVObjectsActivity.this, objects, objectClickListener);
                            objectsRV.setAdapter(adapter);
                        }
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

    public boolean ReturnLastClick(View view) {
        Intent intent;
        if(lastPage == "main"){
            intent = new Intent(this, MainActivity.class);
        }else{
            intent = new Intent(this, PersonActivity.class);
        }
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