package com.example.rallyup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rallyup.uiReference.attendees.AttendeeUpdateActivity;
import com.example.rallyup.notification.NotificationObject;
import com.example.rallyup.progressBar.ProgressBarActivity;

import org.checkerframework.checker.units.qual.A;

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
        Button adminBtn = findViewById(R.id.AdminPages);

//        Button attendeeUpdateInfoButton = findViewById(R.id.AttendeeUpdateInfoButton);


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

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(com.example.rallyup.MainActivity.this,
                                com.example.rallyup.uiReference.admin.AdminHomepageActivity.class);
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