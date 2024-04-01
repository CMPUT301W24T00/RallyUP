package com.example.rallyup.uiReference.organizers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.health.connect.datatypes.HeartRateRecord;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.User;
import com.example.rallyup.uiReference.testingClasses.AttListArrayAdapter;
import com.example.rallyup.uiReference.testingClasses.AttendeeStatsClass;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the activity for the attendee's info in an event
 * @author Kaye Maranan
 */
public class EventAttendeesInfoActivity extends AppCompatActivity
        implements OnMapReadyCallback, FirestoreCallbackListener {

    ImageButton eventAttBackButton;
    ArrayList<AttendeeStatsClass> dataList;
    private ListView attlist;      // the view that everything will be shown on
    private AttListArrayAdapter attListAdapter;

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

    // LatLngs list for the heatmap
    // Needs to be test with Firestore
    List<LatLng> latLngs = new ArrayList<>();

    // Gradient of the HeatMap
    Gradient gradient = new Gradient(colors, startingPoints);

    @Override
    public void onGetUsers(List<User> userList) {
        //FirestoreCallbackListener.super.onGetUsers(userList);
        // Iterate through the list and add the LatLng objects into an array
        for (int i = 0; i < userList.size(); i++){
            if (userList.get(i).getGeolocation() && userList.get(i).getLatlong() != null){
                double lat = userList.get(i).getLatlong().getLatitude();
                double lon = userList.get(i).getLatlong().getLongitude();
                LatLng latLng = new LatLng(lat, lon);
                latLngs.add(latLng);
            }
        }
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


        // Get the event ID only works IF it has been passed to this activity
        // WHICH should be from OrganizerEventDetailsActivity
        // And that activity receives its eventID from OrganizerEventListActivity
        String eventID = getIntent().getStringExtra("eventID");
        // Then call the FirestoreController to do something
        // (probably to retrieve the lat longs of users)
        fc.getCheckedInUserIDs(eventID, this);

//        For future, need to get the LatLngs from User GeoPoints then add it into a JSON?
//        latLngs.add(new com.google.android.gms.maps.model.LatLng(31.7917,-7.0926));
//        latLngs.add(new com.google.android.gms.maps.model.LatLng(31.7917,-7.0926));
//        latLngs.add(new com.google.android.gms.maps.model.LatLng(31.7917,-7.0926));
//        latLngs.add(new com.google.android.gms.maps.model.LatLng(31.7917,-7.0926));
//        latLngs.add(new com.google.android.gms.maps.model.LatLng(31.7917,-8.0926));
//        latLngs.add(new com.google.android.gms.maps.model.LatLng(31.7917,-8.0926));
//        latLngs.add(new com.google.android.gms.maps.model.LatLng(31.7917,-8.0926));
//        latLngs.add(new com.google.android.gms.maps.model.LatLng(31.7917,-8.0926));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        eventAttBackButton = findViewById(R.id.event_attendees_back_button);
        eventAttBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), OrganizerEventDetailsActivity.class);
                startActivity(intent);
            }
        });

        // array of strings?
        String[] users = {
                "Edmonton", "Vancouver", "Toronto"
        };
        Integer[] countedCheckIns = {
                2, 5, 8
        };

        dataList = new ArrayList<>();
        // creating a new array list with objects of City
        for (int i = 0; i < users.length; i++) {
            dataList.add(new AttendeeStatsClass(users[i], countedCheckIns[i]));
        }

        // add adapter for the attendees list
        attlist = findViewById(R.id.attnCheckInList);        // the view that displays all the books

        // connecting the view to the adapter that will be updating its appearance as changes occur in app
        attListAdapter = new AttListArrayAdapter(this, dataList);
        attlist.setAdapter(attListAdapter);
    }

    private void addHeatMap(List<LatLng> latLngs){
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .data(latLngs)
                .gradient(gradient)
                .build();
        TileOverlay overlay = map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));

    }

    // Your tiles MUST BE in the onMapReady, otherwise it will throw a NULL
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (map != null){
            addHeatMap(latLngs);
            return;
        }
        map = googleMap;
        addHeatMap(latLngs);
    }
}