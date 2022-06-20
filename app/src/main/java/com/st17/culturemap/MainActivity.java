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
        RVObjectsActivity.lastPage = "main";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenArtClick(View view) {
        RVObjectsActivity.type = "art";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Искусство";
        RVObjectsActivity.lastPage = "main";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenFoodClick(View view) {
        RVObjectsActivity.type = "food";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Еда";
        RVObjectsActivity.lastPage = "main";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenGameClick(View view) {
        RVObjectsActivity.type = "game";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Игры";
        RVObjectsActivity.lastPage = "main";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenMusicClick(View view) {
        RVObjectsActivity.type = "music";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Музыка";
        RVObjectsActivity.lastPage = "main";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenGreenZoneClick(View view) {
        RVObjectsActivity.type = "greenzone";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Зелёная зона";
        RVObjectsActivity.lastPage = "main";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenLoveClick(View view) {
        RVObjectsActivity.type = "love";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Романтические места";
        RVObjectsActivity.lastPage = "main";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenScienceClick(View view) {
        RVObjectsActivity.type = "science";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Наука и образование";
        RVObjectsActivity.lastPage = "main";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenRuinClick(View view) {
        RVObjectsActivity.type = "ruin";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Разрушенные и несуществующие постройки";
        RVObjectsActivity.lastPage = "main";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }

    public void OpenArchClick(View view) {
        RVObjectsActivity.type = "arch";
        RVObjectsActivity.collection = "PlaceObject";
        RVObjectsActivity.title = "Архитектура";
        RVObjectsActivity.lastPage = "main";
        Intent intent = new Intent(this, RVObjectsActivity.class);
        startActivity(intent);
    }
}