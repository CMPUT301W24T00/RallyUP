package com.example.rallyup.uiReference.organizers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.rallyup.R;
import com.example.rallyup.uiReference.testingClasses.AttListArrayAdapter;
import com.example.rallyup.uiReference.testingClasses.AttendeeStatsClass;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.util.ArrayList;
import java.util.List;

import firebase.com.protolitewrapper.BuildConfig;

/**
 * This class contains the activity for the attendee's info in an event
 * @author Kaye Maranan
 * @see org.osmdroid for more information on osmdroid map capabilities
 */
public class EventAttendeesInfoActivity extends AppCompatActivity {

    ImageButton eventAttBackButton;
    ArrayList<AttendeeStatsClass> dataList;
    private ListView attlist;      // the view that everything will be shown on
    private AttListArrayAdapter attListAdapter;

    // For osmdroid map view
    String TAG = "heatmap";
    DisplayMetrics dm = null;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    // async loading stuff
    boolean renderJobActive = false;
    boolean running = true;
    long lastMovement = 0;
    boolean needsDataRefresh = true;
    // end async loading stuff

    /**
     * the size of the cell in density independent pixels
     * a higher value = smoother image but higher processing and rendering times
     */
    int cellSizeInDp = 20;

    //colors and alpha settings
    String alpha = "#55";
    String red = "FF0000";
    String orange = "FFA500";
    String yellow = "FFFF00";

    //a pointer to the last render overlay, so that we can remove/replace it with the new one
    FolderOverlay heatmapOverlay = null;


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

        // OSMDroid-android 5.6 and newer
        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string

        map = (MapView) findViewById(R.id.osmMap);
        map.setTileSource(TileSourceFactory.MAPNIK);

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

    // @Override
    // addOverlay
    // https://github.com/osmdroid/osmdroid/wiki/How-to-use-the-osmdroid-library-(Java)
    @Override
    protected void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    protected void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void generateMap() {
        renderJobActive = true;
        dm = getResources().getDisplayMetrics();
        // Set the center of the map, preferably the location of the organizer
        // Might need to ask the permission as well when doing this
        // UNLESS already asked
        //map.getController().setCenter(new GeoPoint());


        int densityDpi = (int) (dm.density * cellSizeInDp);

        IGeoPoint iGeoPoint = map.getProjection().fromPixels(0, 0);
        IGeoPoint iGeoPoint2 = map.getProjection().fromPixels(densityDpi, densityDpi);

        //delta is the size of our cell in lat,lon
        //since this is zoom dependent, rerun the calculations on zoom changes
        double xCellSizeLongitude = Math.abs(iGeoPoint.getLongitude() - iGeoPoint2.getLongitude());
        double yCellSizeLatitude = Math.abs(iGeoPoint.getLatitude() - iGeoPoint2.getLatitude());

        BoundingBox view = map.getBoundingBox();

        // a set of a GeoPoints representing what we want a heat map of.
        List<IGeoPoint> pts = loadPoints(view);

    }

    private List<IGeoPoint> loadPoints(BoundingBox view) {
        List<IGeoPoint> pts = new ArrayList<IGeoPoint>();
        // Return a list of geopoints that we get from the user's database
        // Meaning we have to make a FirestoreController
        // that gets us the user's location with a query



        return pts;
    }
}