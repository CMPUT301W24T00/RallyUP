package com.example.rallyup.notification;

import android.adservices.topics.Topic;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.TopicManagementResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MyFirebaseMessagingService extends FirebaseMessagingService
        implements FirestoreCallbackListener {

    private static final String TAG = "MyFirebaseMessagingService";
    private String token;
    FirestoreController fc = FirestoreController.getInstance();
    LocalStorageController lc = LocalStorageController.getInstance();

    // Constructor for MyFirebaseMessagingService
    public MyFirebaseMessagingService(){

    }

    // Registration token may change when:
    // The app is restored on a new device
    // The user uninstalls/reinstall the app
    // The user clears app data
    @Override
    public void onNewToken(@NonNull String token) {
        //super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        // Update the fcmKey field of the User in Firebase
        fc.updateUserStringFields(lc.getUserID(getApplicationContext()),
                "fcmKey",
                token,
                this);
    }

    public String getRegistrationToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()){
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get the new FCM registration token
                        token = task.getResult();

                        // Log and toast
                        String msg = String.format(Locale.getDefault(), "FCM Token: " + token);
                        Log.d(TAG, msg);
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
        return token;
    }
}
