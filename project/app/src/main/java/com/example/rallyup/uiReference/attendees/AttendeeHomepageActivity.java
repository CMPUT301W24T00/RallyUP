package com.example.rallyup.uiReference.attendees;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;


import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Attendance;
import com.example.rallyup.firestoreObjects.User;
import com.example.rallyup.uiReference.splashScreen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Locale;

/**
 * This class contains the home page activity for attendees
 * @author Kaye Maranan
 */
public class AttendeeHomepageActivity extends AppCompatActivity implements FirestoreCallbackListener {

    Button attMyEventsBtn;
    Button attBrowseEventsBtn;
    FloatingActionButton editProfileBtn;
    ImageButton attHomepageBackBtn;
    TextView attProfilePictureTextView;
    ImageView attProfilePicture;
    TextView firstNameView;
    TextView lastNameView;
    TextView usernameView;
    String scannedEvent;
    FirestoreController fc = FirestoreController.getInstance();
    LocalStorageController ls = LocalStorageController.getInstance();
    String userID;
    boolean checkIn, verified = false;

    boolean geoLocation;
    Location curLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    /**
     * Upon getting a user, we will initialize the views using the details of the user
     * @param user an object containing the details of a user
     */
    @Override
    public void onGetUser(User user) {
        Log.d("HomepageActivity", user.getId());
        //userID = user.getId();
        firstNameView.setText(user.getFirstName());
        lastNameView.setText(user.getLastName());
        usernameView.setText(user.getId());
        geoLocation = user.getGeolocation();
        //attProfilePicture.setImageDrawable(user.getProfilePicture());
    }

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

    private void getLastLocation() {
        if (!geoLocation) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        if (fusedLocationProviderClient != null) {
            task.addOnSuccessListener(location -> {
                Toast.makeText(AttendeeHomepageActivity.this, "Doomed but before", Toast.LENGTH_LONG).show();
                if (location != null) {
                    curLocation = location;
                    Toast.makeText(this, "Latitude: " + curLocation.getLatitude() + ", Longitude: " + curLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "bruh location is null", Toast.LENGTH_SHORT).show();

                }
            });
            task.addOnFailureListener(e -> {
                Toast.makeText(AttendeeHomepageActivity.this, "Doomed", Toast.LENGTH_LONG).show();

            });
        } else {
            Toast.makeText(AttendeeHomepageActivity.this, "Doomed coz null", Toast.LENGTH_LONG).show();

        }
    }

    // String attFirstName = findViewById(R.id.att_first_name)
    // String attLastName = findViewById(R.id.att_last_name)
    FloatingActionButton QRCodeScannerBtn;


    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                // If nothing was scanned
                if(result.getContents() == null) {
                    Toast.makeText(AttendeeHomepageActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    // function calls for when an id has been scanned go here
                    //Toast.makeText(AttendeeHomepageActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                    String read = result.getContents();
                    //Log.d("Scanned QR Code", "QR CODE ID: " + read.substring(1));
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                    checkIn = read.charAt(0) == 'c';
                    getLastLocation();
                    if (curLocation != null) {
                        Toast.makeText(this, "Latitude: " + curLocation.getLatitude() + ", Longitude: " + curLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                    }
                    String qrID = read.substring(1);
                    fc.getEventByQRID(qrID, this);
                }
            });

    /**
     * Initializes the attendee homepage activity when it is first launched
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_homepage);
        String userID = ls.getUserID(this);

        fc.getUserByID(userID, this);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        getLastLocation();

        // Text views
        firstNameView = findViewById(R.id.att_first_name);
        lastNameView = findViewById(R.id.att_last_name);
        usernameView = findViewById(R.id.att_homepage_user);

        // Image view for Profile Picture
        attProfilePicture = findViewById(R.id.att_profile_picture);
        attProfilePictureTextView = findViewById(R.id.att_homepage_profile_picture_textview);
        if (attProfilePicture.getDrawable() == null) {
            String firstLetter = userID.substring(0,1);
            String secondLetter = userID.substring(1,2);
            //TextDrawable textDrawable = new TextDrawable(getBaseContext(), firstLetter + secondLetter);
            //attProfilePicture.setImageDrawable(textDrawable);
            //attProfilePicture.setForeground(textDrawable);
            attProfilePictureTextView.setText(String.format(firstLetter + secondLetter));

        }

        // buttons
        attMyEventsBtn = findViewById(R.id.attendee_my_events_button);
        attBrowseEventsBtn = findViewById(R.id.attendee_browse_events_button);
        attHomepageBackBtn = findViewById(R.id.attendee_update_back_button);
        QRCodeScannerBtn = findViewById(R.id.QRScannerButton);
        editProfileBtn = findViewById(R.id.edit_profile_button);

        // send to my events
        attMyEventsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AttendeeMyEventsActivity.class);
            startActivity(intent);
        });

        // send to browse events
        attBrowseEventsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AttendeeBrowseEventsActivity.class);  // placeholder for attendee opener
            startActivity(intent);
        });

        // back button to return to previous page
        attHomepageBackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), splashScreen.class);  // placeholder for attendee opener
            startActivity(intent);
        });


        // scan a qr code
        QRCodeScannerBtn.setOnClickListener(v -> {
            // options for the scanner
            ScanOptions options = new ScanOptions();
            options.setOrientationLocked(false);
            options.setBeepEnabled(false);
            options.setCaptureActivity(ScannerActivity.class);
            barcodeLauncher.launch(options);
        });

        editProfileBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), AttendeeUpdateActivity.class);  // placeholder for attendee opener
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
            Toast.makeText(this, "Check-In Successful! Enjoy the event!", Toast.LENGTH_LONG).show();

        }
        else{
            Intent intent;
            intent = new Intent(AttendeeHomepageActivity.this, AttendeeEventDetails.class);
            intent.putExtra("key", scannedEvent);
            startActivity(intent);
        }
    }
}