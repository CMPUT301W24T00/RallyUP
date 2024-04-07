package com.example.rallyup.uiReference.attendees;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * This class is an activity that enables an attendee to update their profile or user details
 */
public class AttendeeUpdateActivity extends AppCompatActivity implements FirestoreCallbackListener {
    private String userID;
    private final String USER_GEOPOINT_TAG = "latlong";
    private Uri selectedImageUri;
    private TextDrawable textDrawable;

    // To get last known location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location location;
    // Get the instances of the different storage controllers
    FirestoreController fc = FirestoreController.getInstance();
    LocalStorageController lc = LocalStorageController.getInstance();

    TextView profilePictureTextView;
    ImageView profilePicture;
    // TextView of user ID
    TextView userIDView;

    // Edit personal info section
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editPhoneNumber;
    CheckBox geolocationCheck;
    GeoPoint geoPoint;

    String firstName = "";
    String lastName = "";
    String firstLetter = "";
    String secondLetter = "";

    // MODIFY ALL YOUR Images/EditTexts etc INSIDE YOUR fc.METHODS
    /**
     * onGetUser method that tells us what to do once we called onGetUser by the FirestoreController
     * This is where we set the information that is available on the database into our class
     * @param user an object containing the details of a user
     */
    @Override
    public void onGetUser(User user) {
        Log.d("Attendee Update Activity", user.getId());
        // Set the user's details
        //userIDView.setText(String.format(user.getId()));
        // User ID: + user.getId()
        // (Must be used with 18dp of space at the bottom to keep it nice)
        profilePictureTextView = findViewById(R.id.att_update_profile_picture_textview);
        userIDView.setText(String.format("User ID: " + user.getId()));

        // Setting the profile picture
        FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference imageRef = storageRef.getReference();

        // Big logic check for user autogenerated pfp
        if(!Objects.equals(user.getFirstName(), "")
                && !Objects.equals(user.getLastName(), "")){
            firstName = user.getFirstName();
            firstLetter = firstName.substring(0,1);

            lastName = user.getLastName();
            secondLetter = lastName.substring(0,1);

            editFirstName.setText(firstName);
            editLastName.setText(lastName);
        } else if (!Objects.equals(user.getFirstName(), "")
                && Objects.equals(user.getLastName(), "")){
            firstName = user.getFirstName();
            firstLetter = firstName.substring(0,1);
            secondLetter = firstName.substring(1,2);

        } else if (Objects.equals(user.getFirstName(), "")
                && !Objects.equals(user.getLastName(), "")) {

            lastName = user.getLastName();
            firstLetter = lastName.substring(0,1);
            secondLetter = lastName.substring(0,1);
        } else if (Objects.equals(user.getFirstName(), "")
                && Objects.equals(user.getLastName(), "")) {
            // If user.getFirstName() == null && user.getLastName() == null
            firstLetter = user.getId().substring(0,1);
            secondLetter = user.getId().substring(1,2);
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
        fc.getPosterByEventID("/images/ProfilePicture/" + user.getId(), this, profilePicture);
        imageRef.child("/images/ProfilePicture/" + user.getId())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profilePictureTextView.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        profilePictureTextView.setText(String.format(firstLetter + secondLetter));
                        profilePictureTextView.setVisibility(View.VISIBLE);
                    }
                });

        editFirstName.setText(firstName);
        editLastName.setText(lastName);
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

        // Getting the location services
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Edit image section
        profilePictureTextView = findViewById(R.id.att_update_profile_picture_textview);
        profilePicture = findViewById(R.id.attendeeUpdateInfoImageViewXML);
        FloatingActionButton editImageButton = findViewById(R.id.attendeeUpdateInfoPictureFABXML);
        // Will we save the image into the Firestore?

        // TextView of userID
        userIDView = findViewById(R.id.AttendeeUpdateGeneratedUsernameView);

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
                            selectedImageUri = data.getData();
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
                            profilePictureTextView.setText("");
                        }
                    }
                }
        );

        // Another reference
        // Request permission
        // https://www.geeksforgeeks.org/android-how-to-request-permissions-in-android-application/



        // TODO: MODIFY AND MOVE TO QR CHECK IN
        // Might need to look into this:
        // Definitely need to look into it
        // https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                // This is IF we do not have permission - then what do we do?
                                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(this,
                                            new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                                                    Manifest.permission.ACCESS_FINE_LOCATION},
                                            1);
                                    return;
                                }
                            } else {
                                // No location access granted.
                                geolocationCheck.setChecked(false);
                                Toast.makeText(AttendeeUpdateActivity.this, "Permission Denied, Check App Settings", Toast.LENGTH_LONG).show();
                            }
                        }
                );

        // Use the Local Storage Controller (lc) to get the userID
        // then from Firestore Controller (fc) to get the details from the Firebase database
        userID = lc.getUserID(this);
        fc.getUserByID(userID, this);

        //Toast.makeText(getBaseContext(), userID, Toast.LENGTH_SHORT).show();

        // Edit image dialog
        editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder pfpBuilder = new AlertDialog.Builder(AttendeeUpdateActivity.this);
                View editPhotoView = getLayoutInflater().inflate(R.layout.dialog_attendeeupdatepicture, null);
                pfpBuilder.setView(editPhotoView);

                Button editPhotoButton = editPhotoView.findViewById(R.id.AttendeeUpdatePhotoEditButton);
                Button deletePhotoButton = editPhotoView.findViewById(R.id.AttendeeUpdatePhotoDeleteButton);
                Button closeButton = editPhotoView.findViewById(R.id.AttendeeUpdatePhotoCloseButton);

                editPhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);

                        launchSomeActivity.launch(intent);
                        if (profilePicture.getDrawable() != null){
                            profilePictureTextView.setText("");
                        }
                    }
                });

                // Create and Show the dialog
                AlertDialog editPhotoDialog = pfpBuilder.create();
                Objects.requireNonNull(editPhotoDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                editPhotoDialog.show();

                deletePhotoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        profilePictureTextView.setText(String.format(firstLetter + secondLetter));
//                        profilePicture.setImageDrawable(null);
                        fc.deleteFile("/images/ProfilePicture/" + lc.getUserID(getBaseContext()));
                        profilePicture.setImageBitmap(null);
                        profilePicture.setImageDrawable(null);
                        selectedImageUri = null;
                        profilePictureTextView.setVisibility(View.VISIBLE);
                        profilePictureTextView.setText(String.format(firstLetter + secondLetter));
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

        geolocationCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (geolocationCheck.isChecked()) {
                    locationPermissionRequest
                            .launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION});

//                    if (ActivityCompat.checkSelfPermission(AttendeeUpdateActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                    }
                }
            }
        });



        confirmEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is where we change the data into the input inside all the editable views
                // This is assuming we have a user object that I have access to, and has
                // proper setters and getters for its data

                // TODO: remove geopoint setter
                // If the geolocation is true then ask for permission
                if (geolocationCheck.isChecked()) {
                    // If the User allows us to geolocate them, then update the geopoint appropriately
                    fc.updateUserGeoPointFields(userID, USER_GEOPOINT_TAG, geoPoint, AttendeeUpdateActivity.this);
//                    Toast.makeText(AttendeeUpdateActivity.this, "GeoPoint updated", Toast.LENGTH_SHORT).show();
                } else {
                    // If User DOES NOT allow us to geolocate them, set the GeoPoint to NULL
                    fc.updateUserGeoPointFields(userID, USER_GEOPOINT_TAG, null, AttendeeUpdateActivity.this);
//                    Toast.makeText(AttendeeUpdateActivity.this, "GeoPoint NULL", Toast.LENGTH_SHORT).show();
                }


                // Where we upload the data to the Firebase
                updateUserInformation(userID);
                // Since we clicked on confirm, it brings us back to the screen that was there before
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
    private void updateUserInformation(String userID) {
        // Update the firebase
        String USER_FIRST_NAME_TAG = "firstName";
        fc.updateUserStringFields(userID, USER_FIRST_NAME_TAG, editFirstName.getText().toString(), this);

        String USER_LAST_NAME_TAG = "lastName";
        fc.updateUserStringFields(userID, USER_LAST_NAME_TAG, editLastName.getText().toString(), this);

        String USER_EMAIL_TAG = "email";
        fc.updateUserStringFields(userID, USER_EMAIL_TAG, editEmail.getText().toString(), this);

        String USER_PHONE_NUMBER_TAG = "phoneNumber";
        fc.updateUserStringFields(userID, USER_PHONE_NUMBER_TAG, editPhoneNumber.getText().toString(), this);

        String USER_GEOLOCATION_TAG = "geolocation";
        fc.updateUserBooleanFields(userID, USER_GEOLOCATION_TAG, geolocationCheck.isChecked(), this);
        // Following AddEventActivity
        // Might need to change things to upload the image properly
        // Things to reference:
        //      https://firebase.google.com/docs/storage/android/upload-files#upload_from_a_local_file
        //      https://firebase.google.com/docs/storage/android/upload-files#get_a_download_url
        FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference imageRef = storageRef.getReference().child("/images/ProfilePicture/" + userID);
        if (selectedImageUri != null) {
            // If your profile picture DOES have a selectedImageUri
            // (meaning you were able to select a media from your storage)
            // then we upload that into your storage.
            // Otherwise the drawable would be the defaulted auto-generated picture
            // Might need a check that if the image is already in it, what do we do?
            // To avoid multiple copies of the same image if edit then delete then edit photo again.

            //fc.uploadImage(selectedImageUri, imageRef);
            fc.uploadImageBitmap(profilePicture, imageRef);
        } else {
            Toast.makeText(getBaseContext(),
                    "No media selected for profile picture",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
