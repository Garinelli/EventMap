package com.example.eventmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapWindow;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.CameraPosition;

public class MainView extends AppCompatActivity {

    private MapView mapView;
    private Point lastTappedPoint; // сохраняем координаты последней метки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mapView = findViewById(R.id.mapview);

        // Перемещаем камеру на стартовую точку
        mapView.getMap().move(
                new CameraPosition(new Point(55.751574, 37.573856), 10.0f, 0.0f, 0.0f)
        );

        // Добавляем слушатель нажатий по карте
        mapView.getMap().addInputListener(new InputListener() {
            @Override
            public void onMapTap(@NonNull Map map, @NonNull Point point) {
                // Сохраняем координаты
                lastTappedPoint = point;

                // Добавляем метку
                map.getMapObjects().addPlacemark(
                        point,
                        ImageProvider.fromResource(MainView.this, R.drawable.baseline_location_on_24)
                );
            }

            @Override
            public void onMapLongTap(@NonNull Map map, @NonNull Point point) {
                // Сохраняем координаты
                lastTappedPoint = point;

                // Добавляем метку
                map.getMapObjects().addPlacemark(
                        point,
                        ImageProvider.fromResource(MainView.this, R.drawable.baseline_location_on_24)
                );
            }
        });

        Button loginBtn = findViewById(R.id.addPlace);
        ImageView optionsBtn = findViewById(R.id.settingsIcon);

        loginBtn.setOnClickListener(v -> {
            if (lastTappedPoint != null) {
                // Передаем координаты в Intent
                Intent intent = new Intent(MainView.this, Description.class);
                intent.putExtra("latitude", lastTappedPoint.getLatitude());
                intent.putExtra("longitude", lastTappedPoint.getLongitude());
                startActivity(intent);
            }
        });

        optionsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainView.this, Options.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}
