package com.example.rallyup.uiReference.organizers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.health.connect.datatypes.HeartRateRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.User;
import com.example.rallyup.firestoreObjects.Attendance;
import com.example.rallyup.firestoreObjects.Registration;
import com.example.rallyup.uiReference.AttendeeCheckInAdapter;
import com.example.rallyup.uiReference.AttendeeCheckInAdapter;
import com.example.rallyup.uiReference.AttendeeRegisteredAdapter;
import com.example.rallyup.uiReference.testingClasses.AttListArrayAdapter;
import com.example.rallyup.uiReference.testingClasses.AttendeeStatsClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONException;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.List;

/**
 * This class contains the activity for the attendee's info in an event
 * @author Kaye Maranan
 */
public class EventAttendeesInfoActivity extends AppCompatActivity
        implements OnMapReadyCallback, FirestoreCallbackListener {

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

    // Maps
    private GoogleMap map;

    // Data management controllers.
    FirestoreController fc = FirestoreController.getInstance();
    //LocalStorageController lc = LocalStorageController.getInstance();

    // Colors to be used for the HeatMap gradient
    int[] colors = {
        Color.rgb(102, 225, 0), // green
        Color.rgb(255, 0, 0)    // red
    };

    // percentage of maximum intensity from the first element to the second
    float[] startingPoints = {
            0.2f,
            1f
    };

    // Gradient of the HeatMap
    Gradient gradient = new Gradient(colors, startingPoints);

//     @Override
//     public void onGetUsers(List<User> userList) {
//         // LatLngs list for the heatmap
//         List<LatLng> latLngs = new ArrayList<>();

//         //FirestoreCallbackListener.super.onGetUsers(userList);
//         // Iterate through the list and add the LatLng objects into an array
//         for(User user:userList){
//             if (user.getGeolocation()){
//                 latLngs.add(new LatLng(user.getLatlong().getLatitude(), user.getLatlong().getLongitude()));
//                 //Toast.makeText(getBaseContext(), "User LatLng Added!", Toast.LENGTH_SHORT).show();
//             }
//             //System.out.println("userID: " + user.getId());
//             //System.out.println("getGeolocation: " + user.getGeolocation());
//         }
//         // Add the HeatMap overlay after we received ALL the
//         //latLngs.add(new LatLng(-4.0383, 21.7587));
//         //latLngs.add(new LatLng(-4.0383, 21.7587));
//         addHeatMap(latLngs);
//     }

    @Override
    public void onGetEvent(Event event) {
        // If the Event's geolocation is true, then do the map.
        //fc.getCheckedInUserIDs("048ACC2B534046668F6BAA2EA43F170C", this);
        if (event.getGeolocation()){
            //fc.getCheckedInUserIDs(event.getEventID(), this);
            fc.getCheckedInUserIDs2(event.getEventID(), this);
        }
    }

    @Override
    public void onGetLatLngs(List<LatLng> latLngs) {
        // Set the latLngs here into our heatmap
        addHeatMap(latLngs);
        Log.d("EventAttendeesInfoActivity - onGetLatLngs", "latLngs size: " + latLngs.size());
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

        // Get the event ID only works IF it has been passed to this activity
        // WHICH should be from OrganizerEventDetailsActivity
        // And that activity receives its eventID from OrganizerEventListActivity
        // Then call the FirestoreController to do something
        // (probably to retrieve the lat longs of users)
        fc.getEventByID(eventID, this);
        
        // test event ID: 048ACC2B534046668F6BAA2EA43F170C
        //fc.getCheckedInUserIDs("048ACC2B534046668F6BAA2EA43F170C", this);
        //fc.getEventByID("048ACC2B534046668F6BAA2EA43F170C", this);
        //fc.getCheckedInUserIDs2("048ACC2B534046668F6BAA2EA43F170C", this);

        // SupportMapFragment that manages the GoogleMap object
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        checkInList = findViewById(R.id.attnCheckInList);        // the view that displays all the books
        registeredList = findViewById(R.id.registeredAttnList);
        eventAttBackButton = findViewById(R.id.event_attendees_back_button);
        controller.getCheckedInAttendees(eventID, this);
        controller.getRegisteredAttendees(eventID, this);
        TextView pageTitle = findViewById(R.id.org_attendee_details_title);

        eventAttBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), OrganizerEventDetailsActivity.class);
                intent.putExtra("key", eventID);
                startActivity(intent);
            }
        });

        pageTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EventAttendeesInfoActivity.class);
                intent.putExtra("key", eventID);
                startActivity(intent);
            }
        });


        // array of strings?
//        String[] users = {
//                "Edmonton", "Vancouver", "Toronto"
//        };
//        Integer[] countedCheckIns = {
//                2, 5, 8
//        };
//
//        dataList = new ArrayList<>();
//        // creating a new array list with objects of City
//        for (int i = 0; i < users.length; i++) {
//            dataList.add(new AttendeeStatsClass(users[i], countedCheckIns[i]));
//        }
    }

    // Great reference from StackOverflow:
    // https://stackoverflow.com/questions/49465240/weighted-heat-maps-in-android

    /**
     * Method that adds a Heat Map overlay to a GoogleMaps object
     * Will show a Toast if there are NO latLngs
     * @param latLngs A list of LatLng objects for the HeatMap to mark
     */
    private void addHeatMap(List<LatLng> latLngs){
        if (!latLngs.isEmpty()){
            HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                    .data(latLngs)
                    .gradient(gradient)
                    .build();
            TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
        } else {
         Toast.makeText(getBaseContext(), "No data points available", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method that adds a Weighted Heat Map overlay to a GoogleMaps object
     * Will show a Toast if there are NO latLngs
     * @param latLngs A list of WeightedLatLng objects for the HeatMap to mark
     */
    private void addHeatMapWeighted(List<WeightedLatLng> latLngs){
        if (!latLngs.isEmpty()){
            HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                    .weightedData(latLngs)
                    .gradient(gradient)
                    .build();
            TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
        } else {
            Toast.makeText(getBaseContext(), "No data points available", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overrided method that activates the map
     * @param googleMap The googleMap object that we instantiate
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // edmonton actual Lat Long (according to Google): 53.5461, -113.4937
        LatLng edmontonLatLng = new LatLng(53.5461, -113.4937);
        if (map != null){
            map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(edmontonLatLng, 1, 0, 0)));
            return;
        }
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(edmontonLatLng, 1, 0, 0)));
    }
}