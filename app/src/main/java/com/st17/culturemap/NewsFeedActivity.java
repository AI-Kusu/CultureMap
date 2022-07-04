package com.st17.culturemap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewsFeedActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.newsfeed);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        Intent intentMain = new Intent(NewsFeedActivity.this, MainActivity.class);
                        startActivity(intentMain);
                        return true;
                    case R.id.map:
                        Intent intentMap = new Intent(NewsFeedActivity.this, MapActivity.class);
                        startActivity(intentMap);
                        return true;
                    case R.id.newsfeed:
                        return true;
                    case R.id.person:
                        Intent intentPerson = new Intent(NewsFeedActivity.this, PersonActivity.class);
                        startActivity(intentPerson);
                        return true;
                }
                return false;
            }
        });
    }
}