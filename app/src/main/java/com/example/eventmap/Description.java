package com.example.eventmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.eventmap.db.AppDatabase;
import com.example.eventmap.db.User;
import com.example.eventmap.db.Event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Description extends AppCompatActivity {
    public static AppDatabase database;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);

        // Получаем координаты из Intent
        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

        database = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "eventmap-database")
                .fallbackToDestructiveMigration()
                .build();

        Button loginBtn = findViewById(R.id.addDesc);
        EditText descriptionText = findViewById(R.id.descriptionText);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionText.getText().toString();

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    // Используем latitude и longitude из интента
                    Event event = new Event((float) latitude, (float) longitude, description);
                    database.eventDao().insert(event);
                });

                Intent intent = new Intent(Description.this, MainView.class);
                startActivity(intent);
            }
        });
    }
}

