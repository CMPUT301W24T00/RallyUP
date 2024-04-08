package com.example.rallyup.uiReference.attendees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.Notification;
import com.example.rallyup.uiReference.NotificationAdapter;
import com.example.rallyup.uiReference.admin.AdminBrowseImagesActivity;
import com.example.rallyup.uiReference.admin.AdminBrowseImagesAdapter;

import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class contains the event activity for an attendee's registered events
 * @author Isla Medina
 */
public class AttendeeRegisteredEvent extends AppCompatActivity implements FirestoreCallbackListener {

    private View backgroundOverlay;
    ListView announcementsList;
    ImageButton backBtn;

    ImageView poster;
    TextView eventName, eventDate, eventLocation, eventDetails;
    Event displayEvent;

//    TextView dateTextView;
//    TextView locationTextView;
//    TextView descriptionTextView;
//    TextView nameTextView;
    FirestoreController controller = FirestoreController.getInstance();
    NotificationAdapter notificationAdapter;

    private String eventID;

    /**
     * Upon getting a notification list, it will initialize the necessary views with the notification's details
     * @param notifications a list containing the notifications
     */
    @Override
    public void onGetNotifications(List<Notification> notifications) {
        notificationAdapter = new NotificationAdapter(AttendeeRegisteredEvent.this, notifications);
        announcementsList.setAdapter(notificationAdapter);
    }

    /**
     * Upon getting an event it will set the proper fields
     * @param event an event that contains important details
     */
    @Override
    public void onGetEvent(Event event) {
//        dateTextView.setText(event.getEventDate());
//        locationTextView.setText(event.getEventLocation());
//        descriptionTextView.setText(event.getEventDescription());
//        nameTextView.setText(event.getEventName());
      
        eventID = event.getEventID();
        FirestoreController fc = FirestoreController.getInstance();
        fc.getNotificationsByEventID(eventID, this);
      
        displayEvent = event;
        setFields();
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

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("key");

        controller.getEventByID(eventID, this);

        poster = findViewById(R.id.imageView);
        eventName = findViewById(R.id.att_registered_event_name);
        eventDate = findViewById(R.id.att_register_event_date);
        eventLocation = findViewById(R.id.att_register_event_location);
        eventDetails = findViewById(R.id.att_register_event_details);

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


    }

    /**
     * This method sets the fields for an event's details
     */
    public void setFields() {
        controller.getPosterByEventID(displayEvent.getPosterRef(), this, poster);
        eventName.setText(displayEvent.getEventName());
        String uneditedDate = displayEvent.getEventDate();
        String editedDate = getProperDateFormatting(uneditedDate);
        String uneditedTime = displayEvent.getEventTime();
        String editedTime = uneditedTime.substring(0,2) + ":" + uneditedDate.substring(2,4);
        String fullDateText = editedDate + " At " + editedTime;
        eventDate.setText(fullDateText);
        eventLocation.setText(displayEvent.getEventLocation());
        eventDetails.setText(displayEvent.getEventDescription());
    }

    /**
     * This method corrects the string format of a date
     * @param date a string for a date
     * @return the string of the correct date format
     */
    public String getProperDateFormatting(String date) {
        String year = date.substring(0,4);
        String month;
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.CANADA);
        int monthNum=(Integer.parseInt(date.substring(4,6)))-1;
        cal.set(Calendar.MONTH,monthNum);
        month = month_date.format(cal.getTime());
        String day = date.substring(6,8);
        return month + " " + day + ", " + year;
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