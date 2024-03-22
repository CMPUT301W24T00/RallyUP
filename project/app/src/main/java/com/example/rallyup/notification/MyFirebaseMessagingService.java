package com.example.rallyup.notification;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.firestoreObjects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Class that's supposed to deal with FirebaseMessagingService
 * All for push notifications
 * @author Chih-Hung
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService implements FirestoreCallbackListener {

    String userID;
    String currentFCMToken;


    @Override
    public void onGetUser(User user) {
        userID = user.getId();
    }


    @Override
    public void onNewToken(@NonNull String token) {
        //super.onNewToken(token);
        Log.d("FCM", "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Initialize the two different data controllers
        FirestoreController fc = new FirestoreController();
        LocalStorageController lc = new LocalStorageController();
        lc.initialization(getApplicationContext());
        userID = lc.getUserID(getApplicationContext());
        // updates FCM token by using the firebase .update() method
        fc.updateFCMToken(userID, getCurrentFCMToken());
        //
    }

    /**
     * Method (SAME AS FROM FIREBASE.GOOGLE) that retrieves and sets our FCM Token
     */
    private void retrieveCurrentFCMToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MyFirebaseMessagingService",
                                    "Fetching FCM registration token failed",
                                    task.getException());
                            return;

                        }

                        currentFCMToken = task.getResult();

                        // Log and toast
                        String msg = "Token retrieved: " + currentFCMToken;
                        Log.d("MyFirebaseMessagingService", msg);
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Method that just returns what our current FCM token is, by first calling retrieveCurrentFCMToken
     * This sets our token, which is what this method returns
     * @return String object of the current FCM Token of this device.
     */
    public String getCurrentFCMToken(){
        retrieveCurrentFCMToken();
        return currentFCMToken;
    }

}
