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
import android.widget.Toast;

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
import java.util.Locale;
import java.util.Objects;

/**
 * This class is an activity that enables an attendee to update their profile or user details
 */
public class AttendeeUpdateActivity extends AppCompatActivity implements FirestoreCallbackListener {

    EditText editFirstName;
    EditText editLastName;
    String firstName;
    String lastName;
    String userEmail;
    String userPhoneNumber;
    String userID;

    // Display user profile picture
    ImageView profilePicture;
    FloatingActionButton editImageButton;

    // Display user ID
    TextView userIDView;

    // Edit user info items
    EditText editEmail;
    EditText editPhoneNumber;
    CheckBox geolocationCheck;
    Button confirmEditButton;
    ImageButton attHomepageBackBtn;

    private static String firstNameField = "firstName";
    private static String lastNameField = "lastName";
    private static String emailField = "email";
    private static String phoneNumberField = "phoneNumber";
    private static String geolocationField = "geolocation";

    @Override
    public void onGetUser(User user) {
        //FirestoreCallbackListener.super.onGetUser(user);
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userEmail = user.getEmail();
        this.userID = user.getId();
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
        FirestoreController fc = FirestoreController.getInstance();
        //LocalStorageController ls = LocalStorageController.getInstance();
        // Gets the local userID and checks it to the Firestore
        //fc.getUserByID(ls.getUserID(this), this);
        fc.getUserByID("0LzfC31jw7FEgF0VXrXZ", this);

        // Edit image section
        profilePicture = findViewById(R.id.attendeeUpdateInfoImageViewXML);
        editImageButton = findViewById(R.id.attendeeUpdateInfoPictureFABXML);

        // TextView of userID
        userIDView = findViewById(R.id.AttendeeUpdateGeneratedUsernameView);

        // Edit personal info section
        editFirstName = findViewById(R.id.editFirstNameXML);
        editLastName = findViewById(R.id.editLastNameXML);
        editEmail = findViewById(R.id.editEmailAddressXML);
        editPhoneNumber = findViewById(R.id.editPhoneNumberXML);
        geolocationCheck = findViewById(R.id.checkBoxGeolocXML);
        confirmEditButton = findViewById(R.id.attendeeUpdateInfoConfirmXML);
        attHomepageBackBtn = findViewById(R.id.attendee_update_back_button);

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
        //userIDView.setText("@ " + "FIREBASE USERNAME");
        String userIDCombo = "@ " + userID;
        userIDView.setText(userIDCombo);
        editFirstName.setText(firstName);
        editLastName.setText(lastName);
        editEmail.setText(userEmail);
        editPhoneNumber.setText(userPhoneNumber);
        geolocationCheck.setChecked(false); // False for now but should retrieve true/false from Firebase

        // Changing the profile picture
        //resetProfilePicture(profilePicture, "TS");
//        if (editFirstName.getText().toString().isEmpty() && editLastName.getText().toString().isEmpty()){
//            // If both name fields are empty AND no pfp, change profile picture to username
//            // Might need to modify the substring range in order to match the username string spacing
//            String firstLetter = userIDView.getText().toString().substring(0,1).toUpperCase(Locale.getDefault());
//            String secondLetter = userIDView.getText().toString().substring(1,2).toUpperCase(Locale.getDefault());
//            resetProfilePicture(profilePicture, firstLetter + secondLetter);
//
//        } else if (editFirstName.getText().toString().isEmpty()) {
//            // IF they do NOT have their first name but do have their last name
//            String firstLetter = editLastName.getText().toString().substring(0,1).toUpperCase(Locale.getDefault());
//            String secondLetter = editLastName.getText().toString().substring(1,2).toUpperCase(Locale.getDefault());
//            resetProfilePicture(profilePicture, firstLetter + secondLetter);
//
//        } else if (editLastName.getText().toString().isEmpty()) {
//            // IF they do NOT have their last name but have their first name
//            String firstLetter = editFirstName.getText().toString().substring(0,1).toUpperCase(Locale.getDefault());
//            String secondLetter = editFirstName.getText().toString().substring(1,2).toUpperCase(Locale.getDefault());
//            resetProfilePicture(profilePicture, firstLetter + secondLetter);
//        } else {
//            // Here is if they do have both first name and last name
//            String firstLetter = editFirstName.getText().toString().substring(0,1).toUpperCase(Locale.getDefault());
//            String secondLetter = editLastName.getText().toString().substring(0,1).toUpperCase(Locale.getDefault());
//            resetProfilePicture(profilePicture, firstLetter + secondLetter);
//        }
        setDefaultProfilePicture(profilePicture);

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
                // TODO: get the first or the first two letters of a user's username IF there is no
                //  set user first name/last name
                //String firstLetter = "T"; //This is where we will get either the first name or username
                // username[0], or firstName[0]; assuming that they're Strings
                //TextDrawable textDrawable = new TextDrawable(getBaseContext(), firstLetter);

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
                // Just setting the background to be transparent, so it doesn't have
                // sharp white corners
                Objects.requireNonNull(editPhotoDialog.getWindow())
                        .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                editPhotoDialog.show();

                deletePhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //profilePicture.setImageDrawable(textDrawable);
                        resetProfilePicture(profilePicture, "TS");
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

                // Call the FirestoreController to update the database for us
                fc.setUserStringData(userID, firstNameField, editFirstName.getText().toString());
                fc.setUserStringData(userID, lastNameField, editLastName.getText().toString());
                fc.setUserStringData(userID, emailField, editEmail.getText().toString());
                fc.setUserStringData(userID, phoneNumberField, editPhoneNumber.getText().toString());

                fc.setUserBooleanData(userID, geolocationField, geolocationCheck.isChecked());
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
     * Method that changes the profile picture in Attendee Update activity to
     * a set of string characters
     * @param imageView The imageView of the profile to be modified
     * @param nameToDisplay A set of string (expecting 2 characters) to display for the imageView
     *                      as a TextDrawable object.
     * @see TextDrawable for more information on how TextDrawables work.
     */
    private void resetProfilePicture(ImageView imageView, String nameToDisplay){
        // firstCharacter = nameToDisplay.substring(0,1);
        // secondCharacter = nameToDisplay.substring(0,1);
        if (imageView.getDrawable() == null){
            TextDrawable textDrawable = new TextDrawable(getBaseContext(), nameToDisplay);
            imageView.setImageDrawable(textDrawable);
        }
    }

    private void setDefaultProfilePicture(ImageView profilePicture){
        String firstLetter;
        String secondLetter;
        // Changing the profile picture
        if (userID == null) {
            Toast toasty = Toast.makeText(getBaseContext(),"NO USER ID", Toast.LENGTH_SHORT);
            toasty.show();
            resetProfilePicture(profilePicture, "TN");
        } else {
            if (firstName == null && lastName == null) {
                // If both name fields are empty AND no pfp, change profile picture to username
                // Might need to modify the substring range in order to match the username string spacing
                firstLetter = userID.substring(0, 1);
                secondLetter = userID.substring(1, 2);

            } else if (firstName == null) {
                // IF they do NOT have their first name but do have their last name
                firstLetter = lastName.substring(0, 1);
                secondLetter = lastName.substring(1, 2);

            } else if (lastName == null) {
                // IF they do NOT have their last name but have their first name
                firstLetter = firstName.substring(0, 1);
                secondLetter = firstName.substring(1, 2);
            } else {
                // Here is if they do have both first name and last name
                firstLetter = firstName.substring(0, 1);
                secondLetter = lastName.substring(0, 1);
            }

            resetProfilePicture(profilePicture,
                    firstLetter.toUpperCase(Locale.getDefault())
                            + secondLetter.toUpperCase(Locale.getDefault()));
        }
    }

}
