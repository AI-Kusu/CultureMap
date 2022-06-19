package com.st17.culturemap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.st17.culturemap.objects.PlaceObject;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.TextStyle;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements UserLocationObjectListener{

    BottomNavigationView bottomNavigationView;

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private UserLocationLayer userLocationLayer;

    public static List<String> types;

    Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey("05f9eba7-bcf9-4e5f-981f-0c08fffc855b");
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_map);

        super.onCreate(savedInstanceState);

        mapView = findViewById(R.id.mapview);

        mapView.getMap().move(
                new CameraPosition(new Point(56.838011, 60.597446), 11.5f, 0.0f, 0.0f));

        MapKit mapKit = MapKitFactory.getInstance();

        context = MapActivity.this;

        //стиль карты
        setMapStyle();

        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(false);
        userLocationLayer.setObjectListener(this);

        mapObjects = mapView.getMap().getMapObjects();

        loadObjects();

        if(types == null || types.contains("event")) {
            loadEvents();
        }

        //bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.map);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intentMain = new Intent(MapActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        return true;
                    case R.id.map:
                        return true;
                    case R.id.person:
                        Intent intentPerson = new Intent(MapActivity.this, PersonActivity.class);
                        startActivity(intentPerson);
                        return true;
                }
                return false;
            }
        });
    }

    public void loadObjects() {
        db.collection("PlaceObject").get()
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
                            object.type = info.get("type").toString();
                            object.imageURLs = (ArrayList<String>) d.get("imageURLs");
                            object.point = new Point(Double.parseDouble(location.get("latitude").toString()), Double.parseDouble(location.get("longitude").toString()));

                            ImageProvider imageProvider = ImageProvider.fromBitmap(selectImage(object.type));

                            if(types == null || types.contains(object.type)){
                                createTappablePoint(object, imageProvider);
                            }
                        }
                    }
                });
    }

    public void loadEvents() {
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
                            object.type = "event";
                            object.point = new Point(Double.parseDouble(location.get("latitude").toString()), Double.parseDouble(location.get("longitude").toString()));

                            ImageProvider imageProvider = ImageProvider.fromBitmap(selectImage(object.type));

                            createTappablePoint(object, imageProvider);

                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public void onObjectAdded(UserLocationView userLocationView) {
        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView view, @NonNull ObjectEvent event) {
    }

    // Сильная ссылка на слушателя.
    private MapObjectTapListener pointMapObjectTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(MapObject mapObject, Point point) {
            if (mapObject instanceof PlacemarkMapObject) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();

                PlacemarkMapObject placemarkMapObject = (PlacemarkMapObject)mapObject;

                Object userData = placemarkMapObject.getUserData();

                if (userData instanceof MapObjectUserData) {
                    MapObjectUserData mapObjectUserData = (MapObjectUserData)userData;

                    bottomSheetDialog.object = mapObjectUserData.object;

                    bottomSheetDialog.show(getSupportFragmentManager(), "ModalBottomSheet");
                }
            }

            return true;
        }
    };

    private void createTappablePoint(PlaceObject placeObject, ImageProvider imageProvider) {
        PlacemarkMapObject placemark = mapObjects.addPlacemark(placeObject.point);

        //текст
        TextStyle textStyle = new TextStyle();

        textStyle.setColor(Color.BLACK);
        textStyle.setSize(8);
        textStyle.setOffsetFromIcon(true);
        textStyle.setPlacement(TextStyle.Placement.BOTTOM);

        placemark.setText(placeObject.name, textStyle);

        //иконка
        IconStyle iconStyle = new IconStyle();

        iconStyle.setScale(0.045f);

        placemark.setIcon(imageProvider, iconStyle);

        placemark.setUserData(new MapObjectUserData(placeObject));

        placemark.addTapListener(pointMapObjectTapListener);
    }

    private static class MapObjectUserData {
        final PlaceObject object;

        MapObjectUserData(PlaceObject object) {
            this.object = object;
        }
    }

    //выбор значка
    public Bitmap selectImage(String type){
        Bitmap bitmap;

        switch (type){
            case ("science"):
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.science).copy(Bitmap.Config.ARGB_8888, true);
                return bitmap;
            case ("religion"):
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.religion).copy(Bitmap.Config.ARGB_8888, true);
                return bitmap;
            case ("art"):
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.art).copy(Bitmap.Config.ARGB_8888, true);
                return bitmap;
            case ("music"):
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.music).copy(Bitmap.Config.ARGB_8888, true);
                return bitmap;
            case ("event"):
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.event).copy(Bitmap.Config.ARGB_8888, true);
                return bitmap;
            case ("food"):
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.food).copy(Bitmap.Config.ARGB_8888, true);
                return bitmap;
            case ("greenzone"):
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.greenzone).copy(Bitmap.Config.ARGB_8888, true);
                return bitmap;
            case ("game"):
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.game).copy(Bitmap.Config.ARGB_8888, true);
                return bitmap;
            case ("love"):
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.love).copy(Bitmap.Config.ARGB_8888, true);
                return bitmap;
            default:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.search_result).copy(Bitmap.Config.ARGB_8888, true);
                return bitmap;
        }
    }

    private void setMapStyle(){
        String style = "[" +
                "        {" +
                "            \"types\": \"point\"," +
                "            \"tags\": {" +
                "                \"all\": [" +
                "                    \"poi\"" +
                "                ]" +
                "            }," +
                "            \"stylers\": {" +
                "                \"visibility\": \"off\"" +
                "            }" +
                "        }" +
                "    ]";
        mapView.getMap().setMapStyle(style);
    }
}
