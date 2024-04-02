package com.example.rallyup.notification;

import android.adservices.topics.Topic;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.uiReference.attendees.AttendeeEventDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Locale;
import java.util.Objects;

/**
 * A class where we Override what we want to do when generating a new token,
 * receiving message, or sending message from Firebase Cloud Messaging (FCM)
 * AND ONLY THAT. NO MORE. It's not a Listener, not an Object class, just
 * a class that extends FirebaseMessagingService
 * @author Chih-Hung Wu
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService
        implements FirestoreCallbackListener {

    private static final String TAG = "MyFirebaseMessagingService";
    private String token;
    FirestoreController fc = FirestoreController.getInstance();
    LocalStorageController lc = LocalStorageController.getInstance();

    // Sources:
    // https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages?authuser=1#androidnotification

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
                "fcmToken",
                token,
                this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d(TAG, "From: " + message.getFrom());

        if (!message.getData().isEmpty()){
            Log.d(TAG, "Message data payload: " + message.getData());
            // If the data "topic" "eventID" == "eventID_announcements" then bring the user
            if(Objects.equals(message.getData().get("topic"), message.getData().get("eventID") + "_announcements")){
                Intent intent = new Intent(getBaseContext(), AttendeeEventDetails.class);
                intent.putExtra("key", message.getData().get("eventID"));
                startActivity(intent);
            }

        }
    }

}
