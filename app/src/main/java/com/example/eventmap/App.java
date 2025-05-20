package com.example.eventmap;

import android.app.Application;
import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey("4fb04877-ed20-4a8f-a8b2-d84c09c4775c");
        MapKitFactory.initialize(this);
    }
}
