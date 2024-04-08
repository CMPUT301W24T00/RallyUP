package com.example.rallyup.uiReference.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.User;

import java.util.List;

/**
 * This class contains the activity tha allows an admin to browse profiles to be deleted
 */
public class AdminBrowseProfiles extends AppCompatActivity implements FirestoreCallbackListener {
    ListView listView;
    ImageButton backBtn;

    FirestoreController controller;
    List<User> users;

    AdminProfilesAdapter adminProfilesAdapter;

    /**
     * Upon getting the list of events, it will set the necessary adapters
     * @param users a list of event objects
     */
    public void onGetUsers(List<User> users){
        adminProfilesAdapter = new AdminProfilesAdapter(AdminBrowseProfiles.this, users);
        listView.setAdapter(adminProfilesAdapter);
        this.users = users;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_browse_profiles);
        listView = findViewById(R.id.user_profiles_list);

        // real list
        controller = FirestoreController.getInstance();
        controller.getAdminProfiles(this);

        // back button
        backBtn = findViewById(R.id.admin_profiles_back_button);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), AdminHomepageActivity.class);  // placeholder for attendee opener
            startActivity(intent);
        });
    }

}