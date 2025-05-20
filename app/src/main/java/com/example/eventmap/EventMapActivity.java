package com.example.eventmap;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

public class EventMapActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);

        mapView = findViewById(R.id.mapview);

        // Получаем координаты из Intent
        double latitude = getIntent().getDoubleExtra("latitude", 0.0);
        double longitude = getIntent().getDoubleExtra("longitude", 0.0);
        // Проверяем, что координаты валидные
        if (latitude == 0.0 && longitude == 0.0) {
            return;
        }

        Point point = new Point(latitude, longitude);

        // Устанавливаем камеру на координаты события
        mapView.getMap().move(new CameraPosition(point, 14.0f, 0.0f, 0.0f));

        // Добавляем метку на карту
        mapView.getMap().getMapObjects().addPlacemark(
                point,
                ImageProvider.fromResource(this, R.drawable.baseline_location_on_24)
        );
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
