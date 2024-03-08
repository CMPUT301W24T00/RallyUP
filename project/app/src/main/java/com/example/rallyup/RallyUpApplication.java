package com.example.rallyup;

import android.app.Application;

public class RallyUpApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MySingletonInitializer();
        LocalStorageController lc = LocalStorageController.getInstance();
        lc.initialization(this);
    }
    public static void MySingletonInitializer() {
        // This static block ensures that singletons are initialized in the desired order
        FirestoreController.getInstance();
        LocalStorageController.getInstance();
    }


}

