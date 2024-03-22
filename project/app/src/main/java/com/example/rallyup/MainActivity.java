package com.example.rallyup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// Need to import the manifest for some reason to be able
// to do Manifest.permission.POST_NOTIFICATIONS
import android.Manifest;

import com.example.rallyup.uiReference.attendees.AttendeeUpdateActivity;
import com.example.rallyup.notification.NotificationObject;
import com.example.rallyup.progressBar.ProgressBarActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This class contains the main activity of the app which will temporarily hold direct access to features
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Initializes the main activity when it is first created
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                    Toast toast = Toast.makeText(getBaseContext(),
                            "Will not show notifications",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            // } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
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

    // Notification plan:
    // for every Event that user is registered or checked in, add it as a topic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create your notification channels AS SOON as the App begins
        // Doesn't hurt if you keep recreating new ones
        NotificationObject notificationObject = new NotificationObject(this);
        notificationObject.createNotificationChannel(getString(R.string.notification_channel_ID_milestone),
                getString(R.string.notification_channel_description_milestone),
                getString(R.string.notification_channel_description_milestone),
                NotificationCompat.PRIORITY_DEFAULT);

        Button progressButton = findViewById(R.id.ProgressBarButton);
        Button uiLayoutButton = findViewById(R.id.UILayoutButton);

//        Button attendeeUpdateInfoButton = findViewById(R.id.AttendeeUpdateInfoButton);

        // Ask for permissions to send notifications
        askNotificationPermission();

        // Retrieve the current registration token for FCM (Notification) services
//        FirebaseMessaging.getInstance().getToken()
//                        .addOnCompleteListener(new OnCompleteListener<String>() {
//                            @Override
//                            public void onComplete(@NonNull Task<String> task) {
//                                if (!task.isSuccessful()) {
//                                    Log.w("FCM", "Fetching FCM registration token failed", task.getException());
//                                    return;
//                                }
//
//                                // Get new FCM registration token
//                                String token = task.getResult();
//
//                                // Log and toast
//                                String msg = "Token retrieved: " + token;
//                                Log.d("FCM", msg);
//                                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
//                            }
//                        });

        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(com.example.rallyup.MainActivity.this,
                                ProgressBarActivity.class);
                startActivity(intent);
            }
        });
        uiLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(com.example.rallyup.MainActivity.this,
                                com.example.rallyup.uiReference.splashScreen.class);
                startActivity(intent);
            }
        });
      
        /*
        attendeeUpdateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(MainActivity.this, AttendeeUpdateActivity.class);
                startActivity(intent);
            }
        });
    }*/
    }
}