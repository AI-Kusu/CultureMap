package com.st17.culturemap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Circle;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CircleMapObject;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import java.util.Random;

public class MapActivity extends Activity implements UserLocationObjectListener{

    BottomNavigationView bottomNavigationView;

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private UserLocationLayer userLocationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MapKitFactory.setApiKey("05f9eba7-bcf9-4e5f-981f-0c08fffc855b");
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_map);

        super.onCreate(savedInstanceState);

        mapView = findViewById(R.id.mapview);

        mapView.getMap().move(
                new CameraPosition(new Point(0, 0), 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);

        MapKit mapKit = MapKitFactory.getInstance();

        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(false);
        userLocationLayer.setObjectListener(this);

        //


        ImageProvider imageProvider = ImageProvider.fromResource(
                MapActivity.this, R.drawable.search_result);

        mapObjects = mapView.getMap().getMapObjects().addCollection();

        createMapObjects();

        createMapObject(new Point(56.842893, 60.678715), imageProvider);

        //bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                    case R.id.map:
                        return true;
                    case R.id.person:
                        startActivity(new Intent(getApplicationContext(), PersonActivity.class));
                        overridePendingTransition(0, 0);
                }

                return false;
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
        userLocationLayer.setAnchor(
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.5)),
                new PointF((float)(mapView.getWidth() * 0.5), (float)(mapView.getHeight() * 0.83)));

        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView view) {
    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView view, @NonNull ObjectEvent event) {
    }

    private void createMapObject(Point point,ImageProvider imageProvider) {
        createTappablePoint(point, imageProvider);
    }

    // Сильная ссылка на слушателя.
    private MapObjectTapListener pointMapObjectTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(MapObject mapObject, Point point) {
            if (mapObject instanceof PlacemarkMapObject) {
                Toast toast = Toast.makeText(getApplicationContext(), "text",Toast.LENGTH_SHORT);

                toast.show();
            }

            return true;
        }
    };

    class PointMapObjectUserData {
        final int id;
        final String description;

        PointMapObjectUserData(int id, String description) {
            this.id = id;
            this.description = description;
        }
    }

    private void createTappablePoint(Point point, ImageProvider imageProvider) {
        PlacemarkMapObject placemark = mapObjects.addPlacemark(point,imageProvider,new IconStyle());

        placemark.setUserData(new PointMapObjectUserData(0, "point"));

        // Клиентский код должен сохранять строгую ссылку на прослушиватель.
        placemark.addTapListener(pointMapObjectTapListener);
    }


    //original

    private void createMapObjects() {
        createTappableCircle();
    }

    // Сильная ссылка на слушателя.
    private MapObjectTapListener circleMapObjectTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(MapObject mapObject, Point point) {
            if (mapObject instanceof CircleMapObject) {
                CircleMapObject circle = (CircleMapObject)mapObject;

                float randomRadius = 100.0f + 50.0f * new Random().nextFloat();

                Circle curGeometry = circle.getGeometry();
                Circle newGeometry = new Circle(curGeometry.getCenter(), randomRadius);
                circle.setGeometry(newGeometry);

                Object userData = circle.getUserData();
                if (userData instanceof CircleMapObjectUserData) {
                    CircleMapObjectUserData circleUserData = (CircleMapObjectUserData)userData;

                    Toast toast = Toast.makeText(
                            getApplicationContext(),
                            "Circle with id and description tapped",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            return true;
        }
    };

    class CircleMapObjectUserData {
        final int id;
        final String description;

        CircleMapObjectUserData(int id, String description) {
            this.id = id;
            this.description = description;
        }
    }

    private void createTappableCircle() {
        CircleMapObject circle = mapObjects.addCircle(new Circle(new Point(56.842964, 60.675376), 100), Color.GREEN, 2, Color.RED);

        circle.setZIndex(100.0f);
        circle.setUserData(new CircleMapObjectUserData(42, "Tappable circle"));

        // Клиентский код должен сохранять строгую ссылку на прослушиватель.
        circle.addTapListener(circleMapObjectTapListener);
    }
}
