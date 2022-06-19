package com.st17.culturemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        return true;
                    case R.id.map:
                        Intent intentMap = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(intentMap);
                        return true;
                    case R.id.person:
                        Intent intentPerson = new Intent(MainActivity.this, PersonActivity.class);
                        startActivity(intentPerson);
                        return true;
                }
                return false;
            }
        });
    }

    public void OpenEventsClick(View view) {
        RVObjectsActivity.type = "event";
        RVObjectsActivity.collection = "Events";
        RVObjectsActivity.title = "Мероприятия";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenArtClick(View view) {
        RVObjectsActivity.type = "art";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Искусство";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenFoodClick(View view) {
        RVObjectsActivity.type = "food";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Еда";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenGameClick(View view) {
        RVObjectsActivity.type = "game";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Игры";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenMusicClick(View view) {
        RVObjectsActivity.type = "music";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Музыка";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenGreenZoneClick(View view) {
        RVObjectsActivity.type = "greenzone";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Зелёная зонв";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenLoveClick(View view) {
        RVObjectsActivity.type = "love";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Романтические места";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenScienceClick(View view) {
        RVObjectsActivity.type = "science";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Наука и образование";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }
}