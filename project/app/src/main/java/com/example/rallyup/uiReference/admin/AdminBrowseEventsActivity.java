package com.example.rallyup.uiReference.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.uiReference.EventAdapter;

import java.util.List;

/**
 * This class contains an activity that allows an admin to browse events to be modified
 */
public class AdminBrowseEventsActivity extends AppCompatActivity {
    ImageButton backBtn;

    /**
     * Initializes the browse event activity when it is launched
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_browse_events);

        backBtn = findViewById(R.id.admin_events_back_button);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AdminHomepageActivity.class);  // placeholder for attendee opener
            startActivity(intent);
        });
    }
}