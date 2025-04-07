package com.example.eventmap;

import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import androidx.room.Room;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.database.sqlite.SQLiteConstraintException;

import androidx.appcompat.app.AppCompatActivity;
import com.example.eventmap.db.AppDatabase;
import com.example.eventmap.db.User;

public class Registration extends AppCompatActivity {
    public static AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        database = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "eventmap-database")
                .fallbackToDestructiveMigration()
                .build();

        SharedPreferences prefs = getSharedPreferences("eventmap_prefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("is_first_run", true);

        Button registerBtn = findViewById(R.id.registerBtn);
        TextView haveAccount = findViewById(R.id.haveAccount);

        if (isFirstRun) {
            // 3. Если да — добавляем дефолтного пользователя в фоне
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                User defaultUser = new User("admin", "adminpassword");
                database.userDao().insert(defaultUser);
            });

            // 4. Устанавливаем флаг, что запуск уже был
            prefs.edit().putBoolean("is_first_run", false).apply();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loginField = findViewById(R.id.registerLogin);
                EditText passwordField = findViewById(R.id.registerPassword);
                TextView errorText = findViewById(R.id.errorText);
                errorText.setVisibility(View.GONE);

                String login = loginField.getText().toString();
                String password = passwordField.getText().toString();

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    try {
                        User user = new User(login, password);
                        database.userDao().insert(user);

                        runOnUiThread(() -> {
                            Intent intent = new Intent(Registration.this, Welcome.class);
                            startActivity(intent);
                        });

                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            errorText.setVisibility(View.VISIBLE);
                        });
                    }
                });
            }
        });

        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

    }
}
