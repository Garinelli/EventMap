package com.example.eventmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventmap.db.AppDatabase;
import com.example.eventmap.db.Event;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class Options extends AppCompatActivity {
    public static AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        Button mainPageBtn = findViewById(R.id.mainPageBtn);
        Button logout = findViewById(R.id.logoutBtn);
        TextView userLoginText = findViewById(R.id.textView4);

        // Получаем логин из SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String login = prefs.getString("login", "UserLogin");

        // Устанавливаем логин в TextView
        userLoginText.setText("Ваш логин: " + login);

        RecyclerView recyclerView = findViewById(R.id.eventRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Получаем все события из БД
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            database = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "eventmap-database")
                    .fallbackToDestructiveMigration()
                    .build();
            List<Event> events = database.eventDao().getAll();

            runOnUiThread(() -> {
                EventAdapter adapter = new EventAdapter(events);
                recyclerView.setAdapter(adapter);
            });
        });

        // Переход на главную страницу
        mainPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Options.this, MainView.class);
                startActivity(intent);
            }
        });

        // Логика выхода из приложения и удаления логина из SharedPreferences
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Очищаем SharedPreferences при выходе
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("login"); // Удаляем логин
                editor.apply(); // Применяем изменения

                // Переходим в экран логина
                Intent intent = new Intent(Options.this, Login.class);
                startActivity(intent);
                finish(); // Закрываем текущую активность
            }
        });
    }
}
