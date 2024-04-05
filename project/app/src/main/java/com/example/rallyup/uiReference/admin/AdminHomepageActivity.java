package com.example.rallyup.uiReference.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.rallyup.R;
import com.example.rallyup.uiReference.attendees.AttendeeHomepageActivity;
import com.example.rallyup.uiReference.organizers.OrganizerEventListActivity;
import com.example.rallyup.uiReference.splashScreen;

/**
 * This class contains the home page activity for the admin that allows them to choose between different administrative options
 */
public class AdminHomepageActivity extends AppCompatActivity {

    Button eventsBtn;
    Button profilesBtn;

    Button imagesBtn;

    ImageButton backBtn;

    /**
     * Initializes the admin home page activity when it is launched
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        eventsBtn = findViewById(R.id.admin_browse_events_btn);
        profilesBtn = findViewById(R.id.admin_browse_profiles_btn);
        imagesBtn= findViewById(R.id.admin_browse_images_btn);
        backBtn = findViewById(R.id.admin_homepage_back_button);

        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AdminBrowseEventsActivity.class);
                startActivity(intent);
            }
        });

        profilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AdminBrowseProfiles.class);  // placeholder for attendee opener
                startActivity(intent);
            }
        });

        imagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AdminBrowseImages.class);  // placeholder for attendee opener
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), splashScreen.class);  // placeholder for attendee opener
            startActivity(intent);
        });

    }
}