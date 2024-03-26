package com.example.rallyup.uiReference.attendees;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.R;


import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.uiReference.EventAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

// import java.util.ArrayList;

/**
 * This class contains the activity for the attendee's registered events
 * @author Kaye Maranan
 */
public class AttendeeMyEventsActivity extends AppCompatActivity implements FirestoreCallbackListener {
    String scannedEvent;
    FirestoreController fc = FirestoreController.getInstance();
    boolean checkIn;
    boolean verified;

    List<String> eventIDs = new ArrayList<>();

    EventAdapter eventAdapter;

    ImageButton attMyEventsBackBtn;
    FloatingActionButton QRCodeScannerBtn;
    LocalStorageController ls = LocalStorageController.getInstance();
    String userID;



    ListView listView;
//     ArrayList<Integer> arrayList = new ArrayList<>();



    @Override
    public void onGetEventID(String eventID) {
        scannedEvent = eventID;
        fc.getVerified(scannedEvent, userID, this);
    }

    @Override
    public void onGetVerified(boolean verified) {
        this.verified = verified;
        switchPage();
    }

    /**
     * Upon getting the list of events, it links the necessary adapter
     * @param events a collection of event objects
     */
    @Override
    public void onGetEventsFromIDs(List<Event> events) {
        eventAdapter = new EventAdapter(AttendeeMyEventsActivity.this, events);
        listView.setAdapter(eventAdapter);
    }

    @Override
    public void onGetEventIDs(List<String> eventIDS){
        this.eventIDs = eventIDS;
        fc.getEventListFromEventIDs(eventIDs, this);

    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                // If nothing was scanned
                if(result.getContents() == null) {
                    Toast.makeText(AttendeeMyEventsActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    // function calls for when an id has been scanned go here
                    //Toast.makeText(AttendeeHomepageActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                    String read = result.getContents();
                    //Log.d("Scanned QR Code", "QR CODE ID: " + read.substring(1));
                    checkIn = read.charAt(0) == 'c';
                    String qrID = read.substring(1);
                    fc.getEventByQRID(qrID, this);
                }
            });


    /**
     * Initializes the attendee's registered event list activity when it is first launched
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_my_events);
        userID = ls.getUserID(this);

        attMyEventsBackBtn = findViewById(R.id.browse_events_back_button);
        QRCodeScannerBtn = findViewById(R.id.QRScannerButton);
      
        listView = findViewById(R.id.att_my_events_list);

        FirestoreController fc = FirestoreController.getInstance();
        fc.getEventsByUserID(userID, this);

        //controller = FirestoreController.getInstance();
        //controller.getEventsByUserID(ls.getUserID(this), this);

        attMyEventsBackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AttendeeHomepageActivity.class);
            startActivity(intent);
        });

        QRCodeScannerBtn.setOnClickListener(v -> {
            // options for the scanner
            ScanOptions options = new ScanOptions();
            options.setOrientationLocked(false);
            options.setBeepEnabled(false);
            options.setCaptureActivity(ScannerActivity.class);
            barcodeLauncher.launch(options);
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            //Integer poster = (Integer) adapterView.getItemAtPosition(i);
            Event selectedEvent = eventAdapter.getItem(i);

            String eventID = selectedEvent.getEventID();
            Intent intent = new Intent(AttendeeMyEventsActivity.this, AttendeeEventDetails.class);
            intent.putExtra("key", eventID);
            startActivity(intent);
        });

    }


    /**
     * Upon getting the eventID after scanning the QRCode, this function deals with accessing the firestore controller to create or update a new instance of event attendance
     * and then displays the appropriate toast message and switches to the correct page depending on what type of QRCode was scanned
     */
    public void switchPage() {
        if(checkIn) {
            fc.updateAttendance(scannedEvent, userID, verified, this);
            Intent intent;
            intent = new Intent(AttendeeMyEventsActivity.this, AttendeeHomepageActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Check-In Successful! Enjoy the event!", Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent;
            intent = new Intent(AttendeeMyEventsActivity.this, AttendeeEventDetails.class);
            intent.putExtra("key", scannedEvent);
            startActivity(intent);
        }
    }
}