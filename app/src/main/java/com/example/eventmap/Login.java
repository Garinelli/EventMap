package com.example.eventmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.example.eventmap.db.AppDatabase;
import com.example.eventmap.db.User;

public class Login extends AppCompatActivity {
    public static AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        database = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "eventmap-database")
                .fallbackToDestructiveMigration()
                .build();

        Button loginBtn = findViewById(R.id.loginBtn);
        TextView noAccount = findViewById(R.id.noAccount);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loginLogin = findViewById(R.id.loginLogin);
                EditText loginPassword = findViewById(R.id.loginPassword);
                TextView errorText = findViewById(R.id.errorText2);

                String login = loginLogin.getText().toString();
                String password = loginPassword.getText().toString();

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    User user = database.userDao().getByLogin(login);

                    runOnUiThread(() -> {
                        if (user == null || !user.password.equals(password)) {
                            errorText.setVisibility(View.VISIBLE);
                        } else {
                            errorText.setVisibility(View.GONE);
                            Intent intent = new Intent(Login.this, Welcome.class);
                            startActivity(intent);
                        }
                    });
                });
            }
        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

    }
}