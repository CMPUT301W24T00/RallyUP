package com.example.rallyup.uiReference.attendees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class contains the event activity for an attendee when they browse and click on an event
 * @author Kaye Maranan
 */
public class AttendeeEventDetails extends AppCompatActivity implements FirestoreCallbackListener {

    ImageButton attEventBackButton;
    ImageView poster;
    TextView eventName, eventDate, eventLocation, eventDetails;

    Boolean checkIn;

    Event displayEvent;


    FirestoreController controller = new FirestoreController();

    /**
     * Upon getting an event it will set the proper fields
     * @param event an event that contains important details
     */
    @Override
    public void onGetEvent(Event event) {
        displayEvent = event;
        setFields();

    }

    /**
     * Initializes the attendee event details activity when it is first launched
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_event_details);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("key");
        checkIn = intent.getBooleanExtra("checkIn", false);

        controller.getEventByID(eventID, this);


        attEventBackButton = findViewById(R.id.attendee_event_back_button);
        poster = findViewById(R.id.imageView);
        eventName = findViewById(R.id.att_registered_event_name);
        eventDate = findViewById(R.id.att_register_event_date);
        eventLocation = findViewById(R.id.att_register_event_location);
        eventDetails = findViewById(R.id.att_register_event_details);



        attEventBackButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(getBaseContext(), AttendeeBrowseEventsActivity.class);
            startActivity(intent1);
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
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        int monthNum=Integer.parseInt(date.substring(4,6));
        cal.set(Calendar.MONTH,monthNum);
        month = month_date.format(cal.getTime());
        String day = date.substring(6,8);
        return month + " " + day + ", " + year;
    }
}