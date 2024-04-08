package com.example.rallyup.uiReference.organizers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.uiReference.EventAdapter;
import com.example.rallyup.uiReference.attendees.AttendeeBrowseEventsActivity;
import com.example.rallyup.uiReference.attendees.AttendeeEventDetails;
import com.example.rallyup.uiReference.attendees.AttendeeMyEventsActivity;
import com.example.rallyup.uiReference.splashScreen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the activity for an organizer's event list, allowing them to manage their events
 * @author Kaye Maranan
 */
public class OrganizerEventListActivity extends AppCompatActivity implements FirestoreCallbackListener {
    ListView listView;
    ArrayList<Integer> arrayList = new ArrayList<>();
    ImageButton orgEventListBackBtn;
    FloatingActionButton createEventButton;
    FirestoreController controller;
    EventAdapter eventAdapter;


    /**
     * Initializes the organizer's event list activity when it is first launched
     *       If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    public void onGetEvents(List<Event> events) {
        eventAdapter = new EventAdapter(OrganizerEventListActivity.this, events);
        listView.setAdapter(eventAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_list);

        orgEventListBackBtn = findViewById(R.id.organizer_events_back_button);
        createEventButton = findViewById(R.id.createEventButton);
        listView = findViewById(R.id.org_events_list);
        eventAdapter = new EventAdapter(this, new ArrayList<>());

        //arrayList.add(R.drawable.poster1);
        //arrayList.add(R.drawable.poster2);
        //arrayList.add(R.drawable.poster1);
        //arrayList.add(R.drawable.poster2);
        //arrayList.add(R.drawable.poster1);
        //arrayList.add(R.drawable.poster2);

        //ListAdapter listAdapter = new ListAdapter(OrganizerEventListActivity.this, arrayList);
        //listView.setAdapter(listAdapter);
        // real list

        LocalStorageController ls = LocalStorageController.getInstance();
        controller = FirestoreController.getInstance();
        controller.getEventsByOwnerID(ls.getUserID(this), this);


        orgEventListBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), splashScreen.class);
                startActivity(intent);
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddEventActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Event selectedEvent = eventAdapter.getItem(i);

                String eventID = selectedEvent.getEventID();
                Intent intent = new Intent(OrganizerEventListActivity.this, OrganizerEventDetailsActivity.class);
                intent.putExtra("key", eventID);
                startActivity(intent);
            }
        });
    }
}