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
import android.util.Log;
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

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.MainActivity;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Objects;

/**
 * This class is an activity that enables an attendee to update their profile or user details
 */
public class AttendeeUpdateActivity extends AppCompatActivity implements FirestoreCallbackListener {
    private final String USER_FIRST_NAME_TAG = "firstName";
    private final String USER_LAST_NAME_TAG = "lastName";
    private final String USER_EMAIL_TAG = "email";
    private final String USER_PHONE_NUMBER_TAG = "phoneNumber";
    private final String USER_GEOLOCATION_TAG = "geolocation";
    private final String USER_GEOPOINT_TAG = "latlong";

    // Get the instances of the different storage controllers
    FirestoreController fc = FirestoreController.getInstance();
    LocalStorageController lc = LocalStorageController.getInstance();
    // TextView of Username
    TextView userName;

    // Edit personal info section
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editPhoneNumber;
    CheckBox geolocationCheck;

    @Override
    public void onGetUser(User user) {
        Log.d("Attendee Update Activity", user.getId());
        // Set the user's details
        userName.setText(user.getId());
        if (user.getFirstName() == null) {
            editFirstName.setText("");
        } else {
            editFirstName.setText(user.getFirstName());
        }
        if (user.getLastName() == null) {
            editLastName.setText("");
        } else {
            editLastName.setText(user.getLastName());
        }
        if (user.getEmail() == null) {
            editEmail.setText("");
        } else {
            editEmail.setText(user.getEmail());
        }
        if (user.getPhoneNumber() == null) {
            editPhoneNumber.setText("");
        } else {
            editPhoneNumber.setText(user.getPhoneNumber());
        }
        if (user.getGeolocation() == null) {
            geolocationCheck.setChecked(false);
        } else {
            geolocationCheck.setChecked(user.getGeolocation());
        }
//        if (!user.getFirstName().isEmpty() || user.getFirstName() != null){
//            editFirstName.setText(user.getFirstName());
//        } else {
//            editFirstName.setText("");
//        }
//        if (!user.getLastName().isEmpty() || user.getLastName() != null){
//            editLastName.setText(user.getLastName());
//        } else {
//            editLastName.setText("");
//        }
//        if (!user.getEmail().isEmpty() || user.getEmail() != null){
//            editEmail.setText(user.getEmail());
//        } else {
//            editEmail.setText("");
//        }
//        if (!user.getPhoneNumber().isEmpty() || user.getPhoneNumber() != null){
//            editPhoneNumber.setText(user.getPhoneNumber());
//        } else {
//            editPhoneNumber.setText("");
//        }
        //geolocationCheck.setChecked(user.getGeolocation());
    }

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
        // Will we save the image into the Firestore?

        // TextView of Username
        userName = findViewById(R.id.AttendeeUpdateGeneratedUsernameView);

        // Edit personal info section
        editFirstName = findViewById(R.id.editFirstNameXML);
        editLastName = findViewById(R.id.editLastNameXML);
        editEmail = findViewById(R.id.editEmailAddressXML);
        editPhoneNumber = findViewById(R.id.editPhoneNumberXML);
        geolocationCheck = findViewById(R.id.checkBoxGeolocXML);
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

        // Use the Local Storage Controller (lc) to get the userID
        // then from Firestore Controller (fc) to get the details from the Firebase database
        String userID = lc.getUserID(this);
        fc.getUserByID(userID,this);


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
                //String firstLetter = "T"; //This is where we will get either the first name or username
                // username[0], or firstName[0]; assuming that they're Strings
                //TextDrawable textDrawable = new TextDrawable(getBaseContext(), firstLetter);
                String firstLetter = userID.substring(0,1);
                String secondLetter = userID.substring(1,2);
                TextDrawable textDrawable = new TextDrawable(getBaseContext(), firstLetter + secondLetter);

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

                // Where we upload the data to the Firebase
                updateUserInformation(userID);

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

    /**
     * Method that calls the firestore controller to update all editable user fields.
     * @param userID String of the userID to be updated
     */
    private void updateUserInformation(String userID){
        fc.updateUserStringFields(userID, USER_FIRST_NAME_TAG, editFirstName.getText().toString(), this);
        fc.updateUserStringFields(userID, USER_LAST_NAME_TAG, editLastName.getText().toString(), this);
        fc.updateUserStringFields(userID, USER_EMAIL_TAG, editEmail.getText().toString(), this);
        fc.updateUserStringFields(userID, USER_PHONE_NUMBER_TAG, editPhoneNumber.getText().toString(), this);
        fc.updateUserBooleanFields(userID, USER_GEOLOCATION_TAG, geolocationCheck.isChecked(), this);
    }
}
