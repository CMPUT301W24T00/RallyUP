package com.example.rallyup.uiReference;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.User;
import com.example.rallyup.uiReference.admin.AdminHomepageActivity;
import com.example.rallyup.uiReference.attendees.AttendeeHomepageActivity;
import com.example.rallyup.uiReference.organizers.OrganizerEventListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This class contains the activity for the main menu splash screen
 * @author Isla Medina
 */
public class splashScreen extends AppCompatActivity implements FirestoreCallbackListener {

    Button attendeeBtn;
    Button organizerBtn;
    Button adminBtn;
    FirestoreController fc = FirestoreController.getInstance();
    LocalStorageController lc = LocalStorageController.getInstance();

    // RIP STRAIGHT FROM: https://firebase.google.com/docs/cloud-messaging/android/client
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                    // set the wantNotifications of the user to true
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Toast.makeText(getBaseContext(),
                            "Notifications denied!",
                            Toast.LENGTH_SHORT).show();
                    // Set the wantNotifications of the user to false
                }
                fc.getUserByID(lc.getUserID(this), this);
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
                // set the wantNotifications of the user to true
                // (just in case if they allowed notifications in the settings
                // and not from the notification permission dialog)
                fc.getUserByID(lc.getUserID(this), this);
            //} else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    public void onGetUser(User user) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Update the data in the Firebase
            fc.updateUserBooleanFields(lc.getUserID(this), "wantNotifications", ContextCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED,this);

            Toast.makeText(getBaseContext(), "Notification Perm: " + (ContextCompat.checkSelfPermission(getBaseContext(),
                    Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(), "Made it to onGetUser", Toast.LENGTH_SHORT).show();
        }
        Log.d("splashScreen", String.valueOf(user.getAdmin()));
        if (user.getAdmin()) {
            adminBtn.setVisibility(View.VISIBLE);
        }
    }
    // TODO: For 6 April 2024 - If notificationPermissions allowed -> user.setWantNotifications(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Initializing the Admin SDK for Firebase
        //FirebaseApp.initializeApp(this);

        organizerBtn = findViewById(R.id.organizer_button);
        attendeeBtn = findViewById(R.id.attendee_button);
        adminBtn = findViewById(R.id.admin_button);

        // Ask for notification permissions RIGHT AT THE BEGINNING
        askNotificationPermission();
        getFCMToken();

        //MyFirebaseMessagingService.getRegistrationToken();

        organizerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), OrganizerEventListActivity.class);
                startActivity(intent);
            }
        });

        attendeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AttendeeHomepageActivity.class);  // placeholder for attendee opener
                startActivity(intent);
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AdminHomepageActivity.class);  // placeholder for attendee opener
                startActivity(intent);
            }
        });

        FirestoreController fc = FirestoreController.getInstance();
        fc.getUserByID(lc.getUserID(this), this);
    }

    /**
     * Method that displays the current FCMToken associated with the currently downloaded
     * RallyUp. Token WILL CHANGE if App Data wipes or is reset, one way or another.
     */
    void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    Log.w("FirebaseMessaging - SplashScreen", "Fetching FCM Token Failed!", task.getException());
                    return;
                }
                String token = task.getResult();
                String msg = String.format("Token retrieved: " + token);
                Log.d("FirebaseMessaging - SplashScreen", msg);
                //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}