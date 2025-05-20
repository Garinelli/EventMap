package com.example.eventmap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.eventmap.db.AppDatabase;
import com.example.eventmap.db.Event;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Description extends AppCompatActivity {
    public static AppDatabase database;

    private double latitude;
    private double longitude;

    private final String OPENWEATHER_API_KEY = "e5c2bef805c6917f13b20ce15f08801c"; // Вставь сюда свой ключ OpenWeatherMap

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

        loginBtn.setOnClickListener(v -> {
            String description = descriptionText.getText().toString();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    // Получаем погоду по координатам
                    String weather = getWeather(latitude, longitude);

                    // Создаем событие с погодой
                    Event event = new Event((float) latitude, (float) longitude, description, weather);

                    // Сохраняем в базу
                    database.eventDao().insert(event);

                    runOnUiThread(() -> {
                        Toast.makeText(Description.this, "Событие сохранено с погодой: " + weather, Toast.LENGTH_SHORT).show();
                        // Переход обратно на главную
                        Intent intent = new Intent(Description.this, MainView.class);
                        startActivity(intent);
                        finish();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(Description.this, e.toString(), Toast.LENGTH_SHORT).show());
                }
            });
        });
    }

    private String getWeather(double lat, double lon) throws Exception {
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat +
                "&lon=" + lon + "&appid=" + OPENWEATHER_API_KEY + "&units=metric&lang=ru";

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            if (status != 200) {
                throw new Exception("Ошибка сервера: " + status);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(response.toString());

            // Извлекаем описание погоды из JSON
            String weatherDescription = jsonObject.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description");

            // Можно добавить температуру или другую информацию, если нужно
            double temp = jsonObject.getJSONObject("main").getDouble("temp");

            return weatherDescription + ", " + temp + "°C";

        } finally {
            connection.disconnect();
        }
    }
}
