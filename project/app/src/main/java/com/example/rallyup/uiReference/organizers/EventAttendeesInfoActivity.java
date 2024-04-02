package com.example.rallyup.uiReference.organizers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Attendance;
import com.example.rallyup.firestoreObjects.Registration;
import com.example.rallyup.uiReference.AttendeeCheckInAdapter;
import com.example.rallyup.uiReference.AttendeeCheckInAdapter;
import com.example.rallyup.uiReference.AttendeeRegisteredAdapter;
import com.example.rallyup.uiReference.testingClasses.AttListArrayAdapter;
import com.example.rallyup.uiReference.testingClasses.AttendeeStatsClass;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the activity for the attendee's info in an event
 * @author Kaye Maranan
 */
public class EventAttendeesInfoActivity extends AppCompatActivity implements FirestoreCallbackListener{

    ImageButton eventAttBackButton;
    ArrayList<AttendeeStatsClass> dataList;
    private ListView checkInList;      // the view that everything will be shown on
    private ListView registeredList;
    private final FirestoreController controller = FirestoreController.getInstance();

    @Override
    public void onGetAttendants(List<Attendance> attendantList) {
        AttendeeCheckInAdapter attendeeCheckInAdapter = new AttendeeCheckInAdapter(EventAttendeesInfoActivity.this, attendantList);
        checkInList.setAdapter(attendeeCheckInAdapter);
    }

    @Override
    public void onGetRegisteredAttendants(List<Registration> registrationList){
        AttendeeRegisteredAdapter attendeeRegisteredAdapter = new AttendeeRegisteredAdapter(EventAttendeesInfoActivity.this, registrationList);
        registeredList.setAdapter(attendeeRegisteredAdapter);
    }

    /**
     * Initializes an event's attendees info activity when it is first launched
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendees_info);
        Intent intent = getIntent();
        String eventID = intent.getStringExtra("key");

        checkInList = findViewById(R.id.attnCheckInList);        // the view that displays all the books
        registeredList = findViewById(R.id.registeredAttnList);
        eventAttBackButton = findViewById(R.id.event_attendees_back_button);
        controller.getCheckedInAttendees(eventID, this);
        controller.getRegisteredAttendees(eventID, this);

        eventAttBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), OrganizerEventDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}