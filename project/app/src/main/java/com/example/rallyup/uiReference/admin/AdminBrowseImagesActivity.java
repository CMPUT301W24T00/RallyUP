package com.example.rallyup.uiReference.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;

import java.util.List;

public class AdminBrowseImagesActivity extends AppCompatActivity implements FirestoreCallbackListener {

    GridView gridView;
    ImageButton backBtn;

    /**
     * Initializes the brwose images activity when it is launched
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    FirestoreController controller;
    AdminBrowseImagesAdapter adminBrowseImagesAdapter;
    List<Object> combinedEventsUsers;


    /**
     * Upon getting the list of events, it will set the necessary adapters
     //     * @param events a list of event objects
     */
    @Override
    public void onGetImages(List<Object> combinedEventsUsers){
        adminBrowseImagesAdapter = new AdminBrowseImagesAdapter(AdminBrowseImagesActivity.this, combinedEventsUsers);
        gridView.setAdapter(adminBrowseImagesAdapter);
        this.combinedEventsUsers = combinedEventsUsers;
    }


    //    needs two lists, events list and profiles list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_browse_images);
        gridView = findViewById(R.id.admin_images_list);

        controller = FirestoreController.getInstance();
        Log.d("Admin Browse Image Activity", "ran fc");
        controller.getImageReferences(this);

        // back button for the screen
        backBtn = findViewById(R.id.admin_images_back_button);
        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AdminHomepageActivity.class);  // placeholder for attendee opener
            startActivity(intent);
        });
    }
}