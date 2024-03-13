package com.example.rallyup;

import android.app.Application;

/**
 * This class represents the application for the project
 */
public class RallyUpApplication extends Application {
    /**
     * Initializes the local storage and singletons upon the creation of the app
     */
    @Override
    public void onCreate() {
        super.onCreate();
        MySingletonInitializer();
        LocalStorageController lc = LocalStorageController.getInstance();
        lc.initialization(this);
    }

    /**
     * This method initializes the singletons
     */
    public static void MySingletonInitializer() {
        // This static block ensures that singletons are initialized in the desired order
        FirestoreController.getInstance();
        LocalStorageController.getInstance();
    }
}

