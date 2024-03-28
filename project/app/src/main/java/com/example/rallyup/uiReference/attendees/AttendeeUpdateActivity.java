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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * This class is an activity that enables an attendee to update their profile or user details
 */
public class AttendeeUpdateActivity extends AppCompatActivity implements FirestoreCallbackListener {
    private String userID;
    private final String USER_FIRST_NAME_TAG = "firstName";
    private final String USER_LAST_NAME_TAG = "lastName";
    private final String USER_EMAIL_TAG = "email";
    private final String USER_PHONE_NUMBER_TAG = "phoneNumber";
    private final String USER_GEOLOCATION_TAG = "geolocation";
    private final String USER_GEOPOINT_TAG = "latlong";
    private Uri selectedImageUri;
    private TextDrawable textDrawable;
    private String firstLetter;
    private String secondLetter;


    // To get last known location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location location;
    // Get the instances of the different storage controllers
    FirestoreController fc = FirestoreController.getInstance();
    LocalStorageController lc = LocalStorageController.getInstance();

    ImageView profilePicture;
    // TextView of user ID
    TextView userIDView;

    // Edit personal info section
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editPhoneNumber;
    CheckBox geolocationCheck;

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
        userIDView.setText(String.format("User ID: " + user.getId()));
        if (user.getFirstName() == null && user.getLastName() != null) {
            editFirstName.setText("");
            firstLetter = user.getLastName().substring(0,1);
        } else {
            editFirstName.setText(user.getFirstName());
            firstLetter = user.getFirstName().substring(0,1);
        }
        if (user.getLastName() == null && user.getFirstName() != null) {
            editLastName.setText("");
            secondLetter = user.getFirstName().substring(1,2);
        } else {
            editLastName.setText(user.getLastName());
            secondLetter = user.getLastName().substring(1,2);
        }
        if (user.getFirstName() == null && user.getLastName() == null){
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
                        }
                    }
                }
        );

        // Use the Local Storage Controller (lc) to get the userID
        // then from Firestore Controller (fc) to get the details from the Firebase database
        userID = lc.getUserID(this);
        fc.getUserByID(userID, this);
        //Toast.makeText(getBaseContext(), userID, Toast.LENGTH_SHORT).show();

        // editText.getText() keeps returning null for some reason
        // Need to double check on why
        // Getting the first two characters of the user name
        //setAutoGeneratedName(userID);
//        firstLetter = userID.substring(0, 1);
//        secondLetter = userID.substring(1, 2);
//
        textDrawable = new TextDrawable(getBaseContext(), firstLetter + secondLetter);

        // Setting up the auto-generated pfp if you didn't have any set up previously
        if (selectedImageUri == null && profilePicture.getDrawable() == null){
            profilePicture.setImageDrawable(textDrawable);
        }

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

                // This is assuming we have a user object that I have access to, and has
                // proper setters and getters for its data

                // Where we upload the data to the Firebase
                updateUserInformation(userID);

                // Since we clicked on confirm, it brings us back to the screen that was there before
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
//    private void setAutoGeneratedName (String userID){
//        firstLetter = userID.substring(0,1);
//        secondLetter = userID.substring(1,2);
//        if (editFirstName.getText() != null &&
//                editLastName.getText() == null){
//            firstLetter = editFirstName.getText().toString().substring(0,1);
//            secondLetter = editFirstName.getText().toString().substring(1,2);
//        } else if (editFirstName.getText() == null &&
//                editLastName.getText() != null){
//            firstLetter = editLastName.getText().toString().substring(0,1);
//            secondLetter = editLastName.getText().toString().substring(1,2);
//        }
//        Toast.makeText(getBaseContext(), editFirstName.getText().toString(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getBaseContext(), editLastName.getText().toString(), Toast.LENGTH_SHORT).show();
//        textDrawable = new TextDrawable(getBaseContext(), firstLetter + secondLetter);
//    }

    /**
     * Method that calls the firestore controller to update all editable user fields.
     * @param userID String of the userID to be updated
     */
    private void updateUserInformation(String userID) {
        // Update the firebase
        fc.updateUserStringFields(userID, USER_FIRST_NAME_TAG, editFirstName.getText().toString(), this);
        fc.updateUserStringFields(userID, USER_LAST_NAME_TAG, editLastName.getText().toString(), this);
        fc.updateUserStringFields(userID, USER_EMAIL_TAG, editEmail.getText().toString(), this);
        fc.updateUserStringFields(userID, USER_PHONE_NUMBER_TAG, editPhoneNumber.getText().toString(), this);
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
            fc.uploadImage(selectedImageUri, imageRef);
        } else {
            Toast.makeText(getBaseContext(),
                    "No media selected for profile picture",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

//    private void setProfilePicture(){
//        if (data != null && data.getData() != null) {
//            selectedImageUri = data.getData();
//            Bitmap selectedImageBitmap;
//            try {
//                selectedImageBitmap =
//                        MediaStore.Images.Media.getBitmap(
//                                AttendeeUpdateActivity.this.getContentResolver(),
//                                selectedImageUri);
//            } catch (IOException error) {
//                error.printStackTrace();
//                // Setting to null for now, just to remove the error in
//                // profileImageView.setImageBitMap(selectedImageBitmap);
//                selectedImageBitmap = null; // Or have this as the temporary picture place holder
//                // in case that it returns an error
//            }
//            //profileImageView = requireActivity().findViewById(R.id.attendeeUpdateInfoImageViewXML);
//            profilePicture.setImageBitmap(selectedImageBitmap);
//        }
//    }

//    private void getLocation(String userID){
//        // Get the location of the user
//        GeoPoint geoPoint = null;
//        // geoPoint = new GeoPoint(latitude, longitude);
//
//        ActivityResultLauncher<String[]> locationPermissionRequest =
//                registerForActivityResult(new ActivityResultContracts
//                                .RequestMultiplePermissions(), result -> {
//                            Boolean fineLocationGranted = result.getOrDefault(
//                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
//                            Boolean coarseLocationGranted = result.getOrDefault(
//                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
//                            if (fineLocationGranted != null && fineLocationGranted) {
//                                // Precise location access granted.
//                                // This is IF we do not have permission - then what do we do?
//                                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                                    // TODO: Consider calling
//                                    //    ActivityCompat#requestPermissions
//                                    // here to request the missing permissions, and then overriding
//                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                    //                                          int[] grantResults)
//                                    // to handle the case where the user grants the permission. See the documentation
//                                    // for ActivityCompat#requestPermissions for more details.
//                                    ActivityCompat.requestPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, 1);
//                                    return;
//                                }
//                                fusedLocationProviderClient.getLastLocation()
//                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
//                                // Only approximate location access granted.
//                            } else {
//                                // No location access granted.
//                                Toast toasty = Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT);
//                                toasty.show();
//                            }
//                        }
//                );
//
//        // If we do have the location
//        if (geolocationCheck.isChecked()) {
//            locationPermissionRequest.launch(new String[] {
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//            });
//            fc.updateUserGeoPointFields(userID, USER_GEOPOINT_TAG, geoPoint, this);
//        } else {
//            fc.updateUserGeoPointFields(userID, USER_GEOPOINT_TAG, geoPoint, this);
//        }
//    }
}
