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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);

        database = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "eventmap-database")
                .fallbackToDestructiveMigration()
                .build();


        Button loginBtn = findViewById(R.id.addDesc);
        // Описание места
        EditText descriptionText = findViewById(R.id.descriptionText);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionText.getText().toString();

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    Event event = new Event(12.5f, 13.5f, description);
                    database.eventDao().insert(event);
                });

                Intent intent = new Intent(Description.this, MainView .class);
                startActivity(intent);
            }
        });
    }
}
