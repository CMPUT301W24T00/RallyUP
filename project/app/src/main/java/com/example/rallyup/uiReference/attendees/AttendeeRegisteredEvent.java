package com.example.rallyup.uiReference.attendees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.Notification;

import java.util.List;

/**
 * This class contains the event activity for an attendee's registered events
 * @author Isla Medina
 */
public class AttendeeRegisteredEvent extends AppCompatActivity implements FirestoreCallbackListener {

    private View backgroundOverlay;
    ListView announcementsList;
    ImageButton backBtn;

    TextView dateTextView;
    TextView locationTextView;
    TextView descriptionTextView;
    TextView nameTextView;

    private String eventID;

    /**
     * Upon getting a notification list, it will initialize the necessary views with the notification's details
     * @param notifications a list containing the notifications
     */
    @Override
    public void onGetNotifications(List<Notification> notifications) {
        for (Notification notification : notifications) {
            // add to the list
        }
    }

    /**
     * Upon getting an event, it will initialize the necessary views with the event's details
     * @param event an object containing the details of a specific event
     */
    @Override
    public void onGetEvent(Event event) {
        dateTextView.setText(event.getEventDate());
        locationTextView.setText(event.getEventLocation());
        descriptionTextView.setText(event.getEventDescription());
        nameTextView.setText(event.getEventName());
        eventID = event.getEventID();

        FirestoreController fc = FirestoreController.getInstance();
        fc.getNotificationsByEventID(eventID, this);
    }

    /**
     * Initializes the registered event details activity when it is first launched
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_attendee_registered_event);

        dateTextView = findViewById(R.id.att_register_event_date);
        locationTextView = findViewById(R.id.att_register_event_location);
        descriptionTextView = findViewById(R.id.att_register_event_details);
        nameTextView = findViewById(R.id.att_registered_event_name);

        backgroundOverlay = findViewById(R.id.backgroundOverlay);
        backBtn = findViewById(R.id.att_registered_back_btn);
        announcementsList = findViewById(R.id.announcements_list);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AttendeeMyEventsActivity.class);
                startActivity(intent);
            }
        });

        FirestoreController fc = FirestoreController.getInstance();
        fc.getEventByID("Actual last test before pushing lol", this);
    }

    /**
     * This method shows a popup fragment
     */
    public void showPopupFragment() {
        AnnouncementPopupFragment popupFragment = new AnnouncementPopupFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainLayoutRegistered, popupFragment)
                .addToBackStack(null)
                .commit();

        backgroundOverlay.setVisibility(View.VISIBLE);
    }

    /**
     * This method hides a pop up fragment
     */
    public void hidePopupFragment() {
        getSupportFragmentManager().popBackStack();
        backgroundOverlay.setVisibility(View.GONE);
    }
}