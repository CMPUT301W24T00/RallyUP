package com.example.rallyup.uiReference.attendees;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rallyup.MainActivity;
import com.example.rallyup.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Objects;

/**
 * This class is an activity that enables an attendee to update their profile or user details
 */
public class AttendeeUpdateActivity extends AppCompatActivity {

    /**
     * Initializes the attendee updating/editing activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendeeupdateinfo);

        // We need to access our database and get the information as needed

        // Edit image section
        ImageView profilePicture = findViewById(R.id.attendeeUpdateInfoImageViewXML);
        FloatingActionButton editImageButton = findViewById(R.id.attendeeUpdateInfoPictureFABXML);

        // TextView of Username
        TextView userName = findViewById(R.id.AttendeeUpdateGeneratedUsernameView);

        // Edit personal info section
        EditText editFirstName = findViewById(R.id.editFirstNameXML);
        EditText editLastName = findViewById(R.id.editLastNameXML);
        EditText editEmail = findViewById(R.id.editEmailAddressXML);
        EditText editPhoneNumber = findViewById(R.id.editPhoneNumberXML);
        CheckBox geolocationCheck = findViewById(R.id.checkBoxGeolocXML);
        Button confirmEditButton = findViewById(R.id.attendeeUpdateInfoConfirmXML);
        ImageButton attHomepageBackBtn = findViewById(R.id.attendee_update_back_button);
        // All of the following editTexts and checkBox values need to be reflected and update
        // the values from Firebase, once the confirmButton is clicked, it should send the values
        // for Firebase to update.

        // This whole slob of launchSomeActivity HAS TO BE before the call for it
        // Which is in editPhotoButton.setOnClickListener();
        ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // Continue with our Ops here

                        if (data != null && data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            Bitmap selectedImageBitmap;
                            try {
                                selectedImageBitmap =
                                        MediaStore.Images.Media.getBitmap(
                                                AttendeeUpdateActivity.this.getContentResolver(),
                                                selectedImageUri);
                            } catch (IOException error) {
                                error.printStackTrace();
                                // Setting to null for now, just to remove the error in
                                // profileImageView.setImageBitMap(selectedImageBitmap);
                                selectedImageBitmap = null; // Or have this as the temporary picture place holder
                                // in case that it returns an error
                            }
                            //profileImageView = requireActivity().findViewById(R.id.attendeeUpdateInfoImageViewXML);
                            profilePicture.setImageBitmap(selectedImageBitmap);
                        }
                    }
                }
        );

        // FIREBASE needed here as well? Or is it the local generated username?
        userName.setText("@ " + "FIREBASE USERNAME");
        editFirstName.setText("Firebase data");
        editLastName.setText("Firebase data here");
        editEmail.setText("Firebase data again");
        editPhoneNumber.setText("Firebase data once more");
        geolocationCheck.setChecked(false); // False for now but should retrieve true/false from Firebase

        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder pfpBuilder = new AlertDialog.Builder(AttendeeUpdateActivity.this);
                View editPhotoView = getLayoutInflater().inflate(R.layout.dialog_attendeeupdatepicture, null);
                pfpBuilder.setView(editPhotoView);

                Button editPhotoButton = editPhotoView.findViewById(R.id.AttendeeUpdatePhotoEditButton);
                Button deletePhotoButton = editPhotoView.findViewById(R.id.AttendeeUpdatePhotoDeleteButton);
                Button closeButton = editPhotoView.findViewById(R.id.AttendeeUpdatePhotoCloseButton);

                // Comment out once we have access to user's username or firstName
                String firstLetter = "T"; //This is where we will get either the first name or username
                // username[0], or firstName[0]; assuming that they're Strings
                TextDrawable textDrawable = new TextDrawable(getBaseContext(), firstLetter);

                editPhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);

                        launchSomeActivity.launch(intent);
                    }
                });


                // Create and Show the dialog
                AlertDialog editPhotoDialog = pfpBuilder.create();
                Objects.requireNonNull(editPhotoDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                editPhotoDialog.show();

                deletePhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profilePicture.setImageDrawable(textDrawable);
                        editPhotoDialog.dismiss();
                    }
                });
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editPhotoDialog.dismiss();
                    }
                });

            }
        });

        confirmEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is where we change the data into the input inside all the editable views

                // Need to check if they had anything in their data
                // If there was nothing, then accept the new changes
                // If the changes are different than the pre-existing data,
                // then accept the new changes.
                // IF the data is still the same as before, then DO NOT CHANGE THE DATA

                // This is assuming we have a user object that I have access to, and has
                // proper setters and getters for its data

                // "user.firstName" = editFirstName.getText().toString();
                // "user.lastName" = editLastName.getText().toString();
                // "user.email" = editEmail.getText().toString();
                // "user.phoneNumber" = editPhoneNumber.getText().toString();
                // "user.geolocationOn" = geolocationCheck.isChecked();


                // Since we clicked on confirm, it brings us back to the screen that was there before
                // In this case, we'll put MainActivity.class as the placeholder
                Intent intent = new Intent(AttendeeUpdateActivity.this, AttendeeHomepageActivity.class);
                startActivity(intent);
            }
        });

        attHomepageBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AttendeeHomepageActivity.class);  // placeholder for attendee opener
                startActivity(intent);
            }
        });

    }

}
