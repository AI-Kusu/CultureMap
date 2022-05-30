package com.st17.culturemap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.st17.culturemap.DBConnection.MySQLHelper;
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
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements UserLocationObjectListener{

    BottomNavigationView bottomNavigationView;

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private UserLocationLayer userLocationLayer;

    List<PlaceObject> objects = new ArrayList<>();

    MySQLHelper db = new MySQLHelper();

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

        //стиль карты
        setMapStyle();

        userLocationLayer = mapKit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(false);
        userLocationLayer.setObjectListener(this);

        //objects
        createObjects();

        ImageProvider imageProvider = ImageProvider.fromBitmap(drawSimpleBitmap("Каменные \n палатки"));

        mapObjects = mapView.getMap().getMapObjects();

//loadPlacemarks(imageProvider);

        for(PlaceObject place : objects){
            createTappablePoint(place, imageProvider);
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
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                    case R.id.map:
                        return true;
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

                    bottomSheetDialog.name = mapObjectUserData.name;
                    bottomSheetDialog.description = mapObjectUserData.description;

                    bottomSheetDialog.show(getSupportFragmentManager(), "ModalBottomSheet");
                }
            }

            return true;
        }
    };

    private void createTappablePoint(PlaceObject placeObject, ImageProvider imageProvider) {
        PlacemarkMapObject placemark = mapObjects.addPlacemark(placeObject.point,imageProvider,new IconStyle());

        placemark.setUserData(new MapObjectUserData(placeObject.id, placeObject.name, placeObject.description));

        placemark.addTapListener(pointMapObjectTapListener);
    }

    private static class MapObjectUserData {
        final int id;
        final String name;
        final String description;

        MapObjectUserData(int id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }

//void loadPlacemarks(ImageProvider imageProvider){

//    Thread thread = new Thread(new Runnable(){
//        @Override
//        public void run() {
//            try {
//                // Получаем все задания из базы данных
//                points = db.getPoints();

//                mapObjects.addPlacemarks(points, imageProvider, new IconStyle());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    });

//    thread.start();
//}

    //рисование значка
    public Bitmap drawSimpleBitmap(String name) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.search_result).copy(Bitmap.Config.ARGB_8888, true);;
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        // отрисовка текста
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(25 );
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);

        int y = 100;
        if (name.contains("\n"))
        {
            String[] texts = name.split("\n");

            for (String txt : texts)
            {
                canvas.drawText(txt, 50, y, paint);

                y += paint.getTextSize();
            }
        }
        else
        {
            canvas.drawText(name, 0, 100 - ((paint.descent() + paint.ascent()) / 2), paint);
        }

        return bitmap;
    }

    //список мест пока нет бд
    public void createObjects(){
        PlaceObject p = new PlaceObject();
        p.id = 1;
        p.name = "Екатеринбургский государственный цирк им. В. И. Филатова";
        p.description = "Первый стационарный цирк Екатеринбурга открылся 20 ноября 1883" +
                " года на Дровяной площади. Здание цирка строил приехавший с семьей в Россию" +
                " мастер циркового искусства Максимилиано Труцци[1]. Цирк представлял собой небольшое" +
                " деревянное строение, с коническими натяжными куполами, которое могло вместить до 900 зрителей." +
                " Строители предусмотрели внутри систему отопления, поэтому представления давались даже морозной зимой[2]." +
                " Семья Труцци располагала большой программой: наездничество во всех видах, икарийские игры, пантомима." +
                " В цирке проходили не только обычные представления, но ставились и драматические спектакли.";
        p.point = new Point(56.825943, 60.604998);
        objects.add(p);
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
