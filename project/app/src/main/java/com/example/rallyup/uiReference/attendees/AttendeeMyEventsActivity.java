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
    Boolean checkIn;
    @Override
    public void onGetEventID(String eventID) {
        scannedEvent = eventID;
        switchPage();
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
                    checkIn = false;
                    if(read.charAt(0) == 'c'){
                        checkIn = true;
                    }
                    String qrID = read.substring(1);
                    fc.getEventByQRID(qrID, this);
                }
            });
    ImageButton attMyEventsBackBtn;
    FloatingActionButton QRCodeScannerBtn;

    FirestoreController controller;


    ListView listView;
//     ArrayList<Integer> arrayList = new ArrayList<>();

    /**
     * Upon getting the list of events, it links the necessary adapter
     * @param events a collection of event objects
     */
    @Override
    public void onGetEvents(List<Event> events){
        EventAdapter eventAdapter = new EventAdapter(AttendeeMyEventsActivity.this, events);
        listView.setAdapter(eventAdapter);
    }


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

        attMyEventsBackBtn = findViewById(R.id.browse_events_back_button);
        QRCodeScannerBtn = findViewById(R.id.QRScannerButton);
      
        listView = findViewById(R.id.att_my_events_list);

        FirestoreController fc = FirestoreController.getInstance();
        LocalStorageController ls = LocalStorageController.getInstance();
        fc.getEventsByOwnerID(ls.getUserID(this), this);

        controller = FirestoreController.getInstance();
        controller.getEventsByOwnerID(ls.getUserID(this), this);

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

            Integer poster = (Integer) adapterView.getItemAtPosition(i);

            Intent appInfo = new Intent(getBaseContext(), AttendeeRegisteredEvent.class);
//                appInfo.putExtra("poster", poster);
            startActivity(appInfo);
        });
    }


    /**
     * Upon getting the eventID after scanning the QRCode, this function deals with accessing the firestore controller to create or update a new instance of event attendance
     * and then displays the appropriate toast message and switches to the correct page depending on what type of QRCode was scanned
     */
    public void switchPage() {
        if(checkIn) {
            boolean verified = false; // will actually get later when signups are set up
            LocalStorageController lc = LocalStorageController.getInstance();
            String userID = lc.getUserID(this);
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