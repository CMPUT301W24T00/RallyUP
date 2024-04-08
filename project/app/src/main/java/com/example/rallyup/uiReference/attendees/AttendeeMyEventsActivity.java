package com.example.rallyup.uiReference.attendees;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;
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

    EventAdapter eventAdapter;

    ImageButton attMyEventsBackBtn;
    FloatingActionButton QRCodeScannerBtn;
    LocalStorageController ls = LocalStorageController.getInstance();
    String userID;

    boolean geoLocation;
    FusedLocationProviderClient fusedLocationProviderClient;



    ListView listView;
//     ArrayList<Integer> arrayList = new ArrayList<>();



    /**
     * Upon getting the event ID of from the scanned QR code, it saves the value and uses that value to get the verification status
     * of the user for that event
     * @param eventID the unique ID of the event
     */
    @Override
    public void onGetEventID(String eventID) {
        scannedEvent = eventID;
        fc.getVerified(scannedEvent, userID, this);
    }

    /**
     * Upon getting the verification status of the user for this event, we perform the required check-in or share action
     * @param verified the verification status of the user for this event
     */
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
    public void onGetEvents(List<Event> events) {
        eventAdapter = new EventAdapter(AttendeeMyEventsActivity.this, events);
        listView.setAdapter(eventAdapter);
    }

    private void getLastLocation() {
        // check if user has geolocation enabled
        if (!geoLocation) {
            // set point to null
            fc.updateUserGeoPointFields(ls.getUserID(this), "latlong",null,AttendeeMyEventsActivity.this);
            return;
        }

        // similar permissions check where if location was not granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            fc.updateUserGeoPointFields(ls.getUserID(this), "latlong",null,AttendeeMyEventsActivity.this);
            return;
        }

        // task for location provider
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        // check if was able to initialize location provider
        if (fusedLocationProviderClient != null) {

            // if successful
            task.addOnSuccessListener(location -> {

                // check if location of device is null
                if (location != null) {

                    GeoPoint newPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                    fc.updateUserGeoPointFields(ls.getUserID(this), "latlong",newPoint,AttendeeMyEventsActivity.this);
                    Log.d("getLastLocation", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                } else {
//                    Toast.makeText(this, "bruh location is null", Toast.LENGTH_SHORT).show();
                    Log.d("getLastLocation", "Location is null");

                }
            });

            // upon failure of task
            task.addOnFailureListener(e -> {
                Log.d("getLastLocation", "Task failure");

//                Toast.makeText(AttendeeHomepageActivity.this, "Doomed", Toast.LENGTH_LONG).show();

            });
            // location provide is null
        } else {
            Log.d("getLastLocation", "Location provider null");
        }
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
                    getLastLocation();
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
            Intent intent = new Intent(AttendeeMyEventsActivity.this, AttendeeRegisteredEvent.class);
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
            if(verified){
                fc.updateAttendance(scannedEvent, userID,  this);
                Intent intent;
                intent = new Intent(AttendeeMyEventsActivity.this, AttendeeRegisteredEvent.class);
                intent.putExtra("key", scannedEvent);
                startActivity(intent);
                Toast.makeText(this, "Check-In Successful! Enjoy the event!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Please register for this event before you attempt to check-in!", Toast.LENGTH_LONG).show();
                Intent intent;
                intent = new Intent(AttendeeMyEventsActivity.this, AttendeeEventDetails.class);
                intent.putExtra("key", scannedEvent);
                startActivity(intent);
            }

        }
        else{
            Intent intent;
            intent = new Intent(AttendeeMyEventsActivity.this, AttendeeEventDetails.class);
            intent.putExtra("key", scannedEvent);
            startActivity(intent);
        }
    }
}