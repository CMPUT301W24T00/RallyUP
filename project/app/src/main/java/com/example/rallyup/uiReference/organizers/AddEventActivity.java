package com.example.rallyup.uiReference.organizers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.QrCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * This class contains the activity that allows an organizer to add events
 */
public class AddEventActivity extends AppCompatActivity implements ChooseReUseEventFragment.OnInputListener, FirestoreCallbackListener {
    private EditText eventLocationInput, eventNameInput, eventDescriptionInput;

    private TextView eventDateInput, eventTimeInput, uploadPosterText, shareDisplayText, checkInDisplayText;

    private Button createButton;

    private FloatingActionButton eventImageInput;

    private ImageButton backButton;

    private CheckBox geoInput, newQRSelect, attendeeSignUpLimitInput, reUseQRSelect;

    private NumberPicker attendeeLimitPicker;

    private String eventName, eventLocation, eventDescription, eventID;
    private StorageReference posterRef, shareQRRef, checkInQRRef;

    // @ MARCUS in the function for if the user selects to reuse a QR Code you
    // would save the path to the QR images to these variables
    private String posterPath, shareQRPath, checkInQRPath;

    // Date in the format year, month, day concatenated together
    // time in the format hour, minute concatenated together in 24 hour time
    private String eventDate, eventTime, userID;
    private Integer signupLimit = -1;
    private Boolean geolocation, signupLimitInput, reUseQR, newQR;
    private Boolean posterUploaded = false;
    private List<Event> usersPreviousEvents = new ArrayList<>();

    private ImageView shareImageView, checkInImageView, posterImage;

    private FirebaseStorage storage;
    // Create a storage reference from our app
    private StorageReference storageRef;

    FirebaseFirestore database = FirebaseFirestore.getInstance();


    // Create a child reference
    // imagesRef now points to "images"
    private FirestoreController controller = new FirestoreController();

    private Uri image = null;

    private String reUseQrID;

    @Override
    public void onGetEvents(List<Event> events) {
        usersPreviousEvents.addAll(events);
    }

    @Override
    public void onGetQrCode(QrCode qrCode, String jobId) {
        String encodedText;
        if (jobId.equals("share")) {
            // Share QR code
            encodedText = "s" + qrCode.getQrId();
        } else {
            // Check-in QR code
            encodedText = "c" + qrCode.getQrId();
        }

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix matrix;
        try {
            matrix = writer.encode(encodedText, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        BarcodeEncoder encoder = new BarcodeEncoder();
        Bitmap bitmap = encoder.createBitmap(matrix);

        if (jobId.equals("share")) {
            // Share QR code
            shareImageView.setImageBitmap(bitmap);
            shareImageView.setVisibility(View.VISIBLE);
            shareDisplayText.setVisibility(View.VISIBLE);
        } else {
            // Check-in QR code
            checkInImageView.setImageBitmap(bitmap);
            checkInImageView.setVisibility(View.VISIBLE);
            checkInDisplayText.setVisibility(View.VISIBLE);
        }

        // Create eventId
        qrCode.setEventID(eventID);
        qrCode.setCheckIn(!jobId.equals("share"));
        FirestoreController fc = FirestoreController.getInstance();
        fc.updateQrCode(qrCode, bitmap);
    }

    /**
     * Upon creating an event, retrieved it's stored ID
     * @param event an object containing the details of an event
     */
    @Override
    public void onCreateEvent(Event event) {
        eventID = event.getEventID();
    }

    /**
     * Initializes the add event activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        FirestoreController fc = FirestoreController.getInstance();
        fc.createEvent(this);
        getUserID();
        fc.getEventsByOwnerID(userID, this);

        // initializing all the views from our .xml file
        initializeViews();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), OrganizerEventListActivity.class);
                startActivity(intent);
            }
        });

        // prompting the user to upload an image when they click on the upload box
        eventImageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });


        //Setting the onClick Listener for picking the date, then setting the text of the pick date button
        //(Clickable textView) To be that selected date
        eventDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEventDate();
            }
        });

        // prompting the user to set event start time when the "Event Time" box is clicked
        eventTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEventTime();
            }
        });

        // prompting the user to set an attendee signup limit when the checkbox is checked
        attendeeSignUpLimitInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAttendeeLimit();
                } else {
                    resetAttendeeLimit();
                }
            }
        });

        // setting the onScrollListner for the number picker that sets the attendee signup limit to update the limit when
        // the numberpicker is scrolled
        attendeeLimitPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    signupLimit = numberPicker.getValue();
                }
            }
        });

        // setting the onScrollListner for the number picker that sets the attendee signup limit to update the limit when
        // the numberpicker is changed (So that when the user types the value in instead of scrolling it still updates)
        attendeeLimitPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                signupLimit = attendeeLimitPicker.getValue();
            }
        });

        // opening up the fragment that displays all the available old events that the user can choose to reuse a
        // QR Code from
        reUseQRSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    new ChooseReUseEventFragment(usersPreviousEvents).show(getSupportFragmentManager(), "Add/Edit City");
                }
            }
        });



        // Generating the new QR Code that is encoded with the event name when the user checks the
        // "Generate new QR Code" box
        newQRSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
//                    generateShareQR();
//                    generateCheckInQR();
                    generateQRCode("share");
                    generateQRCode("checkIn");
                }
                else {
                    resetQR();
                }
            }
        });


        // sends all the information to firebase when the User clicks the "ADD" button
        // (Does input validation first)
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateEventClick();
            }
        });



    }

    /**
     * Initializes all the views that need to be accessed in this activity
     */
    public void initializeViews() {
        backButton = findViewById(R.id.backButton);
        eventNameInput = findViewById(R.id.eventNameInput);
        uploadPosterText = findViewById(R.id.uploadPosterText);
        eventLocationInput = findViewById(R.id.eventLocationInput);
        eventDateInput = findViewById(R.id.eventDateInput);
        eventTimeInput = findViewById(R.id.eventTimeInput);
        eventDescriptionInput = findViewById(R.id.eventDetailsInput);
        attendeeSignUpLimitInput = findViewById(R.id.attendeeSignUpLimitInput);
        attendeeLimitPicker = findViewById(R.id.attendeeLimitPicker);
        geoInput = findViewById(R.id.geolocationInput);
        createButton = findViewById(R.id.createEventButton);
        eventImageInput = findViewById(R.id.posterUploadButton);
        newQRSelect = findViewById(R.id.newQrCodeSelect);
        reUseQRSelect = findViewById(R.id.reuseQRCodeSelect);
        shareImageView = findViewById(R.id.shareQRView);
        checkInImageView = findViewById(R.id.checkInQRView);
        shareDisplayText = findViewById(R.id.shareQRText);
        checkInDisplayText = findViewById(R.id.checkInQRText);
        posterImage = findViewById(R.id.uploadPosterView);

    }

    /**
     * Retrieving the user input from the ChooseReUseEventFragment
     * @param input a String variable that represents the list option the user selected in the fragment dialogue
     */
    // Code sourced from:
    // Reference: https://www.geeksforgeeks.org/how-to-pass-data-from-dialog-fragment-to-activity-in-android/
    @Override
    public void sendInput(String input)
    {
        reUseQrID = input;
    }


    /**
     * Allows the user to upload an image from their device
     * to represent the poster for this event.
     * This method does not take in any parameters, or return any variables
     */
    private void imageChooser()
    // Code for the image chooser sourced and adapted from:
    // Source: https://www.geeksforgeeks.org/how-to-select-an-image-from-gallery-in-android/
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }


    ActivityResultLauncher<Intent> launchSomeActivity
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        image = selectedImageUri;
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                            posterImage.setImageBitmap(
                                    selectedImageBitmap);
                            posterImage.setVisibility(View.VISIBLE);
                            uploadPosterText.setVisibility(View.GONE);
                            posterUploaded = true;
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    /**
     * Prompts the user to set the start date of this event by
     * utilizing the Calender widget. Saves the inputted date as a String
     * that represents Year, Month, and Day concatenated together in that order for ease of sorting.
     * This method does not take in any parameters, or return any variables
     */
    public void getEventDate(){
        // Code sourced and adapted from:
        // Reference: https://www.geeksforgeeks.org/datepicker-in-android/
        // on below line we are getting
        // the instance of our calendar.
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                AddEventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        monthOfYear = monthOfYear + 1;
                        eventDateInput.setText(toStringCheckZero(dayOfMonth) + "-" + toStringCheckZero((monthOfYear)) + "-" + toStringCheckZero(year));
                        eventDate = (toStringCheckZero(year) + toStringCheckZero((monthOfYear)) + toStringCheckZero(dayOfMonth));
                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();

    }

    /**
     * Prompts the user to set the start time of this event by
     * utilizing the TimePicker widget. Saves the inputted time as a String
     * that represents hour and minute concatenated together in that order for ease of sorting.
     * Set to the 24 Hour clock
     * This method does not take in any parameters, or return any variables
     */
    public void getEventTime(){
        // Code sourced and adapted from:
        // Reference: https://abhiandroid.com/ui/timepicker#gsc.tab=0
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                eventTimeInput.setText("" + toStringCheckZero(selectedHour) + ":" + toStringCheckZero(selectedMinute));
                eventTime = toStringCheckZero(selectedHour) + toStringCheckZero(selectedMinute);
            }
        }, hour, minute, true); //Setting it to 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    /**
     * Sets the numberPicker that is responsible for setting the attendee sign-up limit to visible
     * This function is called if the user checks the "Set Sign Up Limit" checkbox
     */
    public void setAttendeeLimit() {
        attendeeLimitPicker.setVisibility(attendeeLimitPicker.VISIBLE);
        attendeeLimitPicker.setMaxValue(1000);
        attendeeLimitPicker.setMinValue(1);
    }

    /**
     * Resets the numberPicker that is responsible for setting the attendee sign-up limit to 0, and removes it from the page
     * This function is called if the user unchecks the "Set Sign Up Limit" checkbox
     */
    public void resetAttendeeLimit() {
        attendeeLimitPicker.setValue(1);
        attendeeLimitPicker.setVisibility(attendeeLimitPicker.GONE);
    }

    /**
     * Adds a leading 0 to the inputted int number if it is less than 10 and converts the number to a String
     * @param number: the number to be converted
     * @return the String representation of the inputted number with a leading 0 added to it if the number is less than 10
     */
    public String toStringCheckZero(int number){
        if(number<=9) {
            return "0" + String.valueOf(number);
        }
        return String.valueOf(number);
    }

    /**
     * Uses the firebase to pull up the QR Codes from the event the user selected to reuse QR Codes from,
     * // and sets those QR Codes to be associated with this event
     */
    public void generateReUseQRCode() {
        // ** @ MARCUS here is the function where we should be switching the QR code to
        // the QR code from the event the user selected to reuse. The event ID should be stored in the variable
        // reUseQrID
        String qrID = reUseQrID;
    }

    private void generateQRCode(String jobId) {
        FirestoreController fc = FirestoreController.getInstance();
        fc.createQRCode(jobId, this);
    }

    /**
     * Generates a QR Code that will be used to share event details
     * This method does not take in any parameters, or return any variables
     */
//    private void generateShareQR() {
//        // Code sourced and adapted from:
//        // Reference: https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/
//        // Library: https://github.com/journeyapps/zxing-android-embedded
//
//        // @ Marcus this text should be replaced with "c" + the unique event ID
//        String text = "s" + eventNameInput.getText().toString();
//        MultiFormatWriter writer = new MultiFormatWriter();
//        BitMatrix matrix;
//        try {
//            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400);
//        } catch (WriterException e) {
//            throw new RuntimeException(e);
//        }
//        BarcodeEncoder encoder = new BarcodeEncoder();
//        Bitmap bitmap = encoder.createBitmap(matrix);
//        shareImageView.setImageBitmap(bitmap);
//        shareImageView.setVisibility(View.VISIBLE);
//        shareDisplayText.setVisibility(View.VISIBLE);
//    }

    /**
     * Generates a QR Code that will be used to check users in to the event
     * This method does not take in any parameters, or return any variables
     */
//    private void generateCheckInQR() {
//        // Code sourced and adapted from:
//        // Reference: https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/
//        // Library: https://github.com/journeyapps/zxing-android-embedded
//
//        // @ Marcus this text should be replaced with "c" + the unique event ID
//        String checkInText = "c" + eventNameInput.getText().toString();
//
//        MultiFormatWriter writer = new MultiFormatWriter();
//        BitMatrix matrix;
//        try {
//            matrix = writer.encode(checkInText, BarcodeFormat.QR_CODE, 400, 400);
//        } catch (WriterException e) {
//            throw new RuntimeException(e);
//        }
//        BarcodeEncoder encoder = new BarcodeEncoder();
//        Bitmap bitmap = encoder.createBitmap(matrix);
//        // make a new view for the checkInQR to be displayed?
//        checkInImageView.setImageBitmap(bitmap);
//        checkInImageView.setVisibility(View.VISIBLE);
//        checkInDisplayText.setVisibility(View.VISIBLE);
//    }

    /**
     * This method resets the QR
     */
    public void resetQR() {
        checkInImageView.setVisibility(View.GONE);
        checkInDisplayText.setVisibility(View.GONE);
        shareImageView.setVisibility(View.GONE);
        shareDisplayText.setVisibility(View.GONE);
    }


    /**
     * Returns a Boolean object that represents whether or not all required fields of the Add Event form have been filled in.
     * <p>
     * This method ensures that all the inputs required for successful event creation have been filled in.
     *
     * @return      the Boolean value representing whether or not all required fields have been filled in.
     */
    public Boolean validateInput() {
        // checking to see if any of the required fields were left blank
        if(eventName.isEmpty() || eventLocation.isEmpty() || (eventTimeInput.getText().toString().isEmpty())
        || eventDateInput.getText().toString().isEmpty() || (!newQRSelect.isChecked() && !reUseQRSelect.isChecked())
                || !posterUploaded) {
            Toast.makeText(
                            this,
                            "Please ensure all fields are filled out!",
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        // checking to see if the user checked both boxes for the QR Option
        else if(newQRSelect.isChecked() && reUseQRSelect.isChecked()) {
            Toast.makeText(
                            this,
                            "Please ensure only one of the QR Generation Options is filled out!",
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        // checking to see if the user didn't scroll to select a signup limit or if they selected a sign up limit of 1
        else if ((attendeeSignUpLimitInput.isChecked() && signupLimit.equals(-1)) || (attendeeSignUpLimitInput.isChecked() && signupLimit.equals(1))) {
            Toast.makeText(
                            this,
                            "Please select a valid sign up limit! It has to be greater than 1!",
                            Toast.LENGTH_SHORT)
                    .show();
            return false;

        } else {
            // all the required fields are populated
            return true;
        }
    }

    /**
     * This method uploads the image the user selected as the event poster to firebase iCloud storage
     */
    public void uploadPoster() {

        controller.uploadImage(image, posterRef);

    }

    /**
     * This method uploads the newly generated share QR Code image to firebase iCloud storage
     */
    public void uploadShareQR() {
        controller.uploadImageBitmap(shareImageView, shareQRRef);
    }

    /**
     * This method uploads the newly generated check-in QR Code image to firebase iCloud storage
     */
    public void uploadCheckInQR() {
        controller.uploadImageBitmap(checkInImageView, checkInQRRef);
    }


    /**
     * This method generates an event id
     */
    public void generateEventID() {
        eventID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    /**
     * This method gets the id of a user from the local storage
     */
    public void getUserID(){
        LocalStorageController lc = new LocalStorageController();
        lc.initialization(this);
        userID = lc.getUserID(this);
    }
    /**
     * This method saves all the input fields in new variables, resets the views of all the input fields on the screen,
     * and passes the saved variables to firebase as an instance of the Event Class.
     *
     */
    public void onCreateEventClick(){
        // saving the data the user inputted into variables
        eventName = String.valueOf(eventNameInput.getText());
        eventLocation = String.valueOf(eventLocationInput.getText());
        geolocation = geoInput.isChecked();
        eventDescription = String.valueOf(eventDescriptionInput.getText());
        signupLimitInput = attendeeSignUpLimitInput.isChecked();
        newQR = newQRSelect.isChecked();
        reUseQR = reUseQRSelect.isChecked();

        Boolean inputVal = validateInput();
        if(inputVal.equals(true)) {
            generateEventID();
            getUserID();
            if(newQR){
//                generateShareQR();
//                generateCheckInQR();
                generateQRCode("share");
                generateQRCode("checkIn");

                // if the user wants new QR Codes to be generated
                // Code for uploading these images to firebase icloud storage sourced from
                // Reference: https://firebase.google.com/docs/storage/android/upload-files
                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();
                posterRef = storageRef.child("images/Posters/"+ eventID);
                posterPath = posterRef.getPath();
                shareQRRef = storageRef.child("images/ShareQR/"+ eventID);
                shareQRPath = shareQRRef.getPath();
                checkInQRRef = storageRef.child("images/CheckInQR/"+ eventID);
                checkInQRPath = checkInQRRef.getPath();
                uploadCheckInQR();
                uploadShareQR();
                posterUploaded = false;
            }
            else {
                // if the user selected to reUse a past Event QR Code
                generateReUseQRCode();
            }
            // Uploading the Poster to Firebase Icloud Storage
            uploadPoster();
            // Clearing the views of the form
            eventNameInput.getText().clear();
            eventLocationInput.getText().clear();
            eventDescriptionInput.getText().clear();
            eventDateInput.setText("");
            eventTimeInput.setText("");
            geoInput.setChecked(false);
            newQRSelect.setChecked(false);
            reUseQRSelect.setChecked(false);
            attendeeSignUpLimitInput.setChecked(false);
            attendeeLimitPicker.setVisibility(View.GONE);
            posterImage.setImageDrawable(null);
            resetQR();
            uploadPosterText.setVisibility(View.VISIBLE);

            // send the event values to fb
            int currentlySignedUp = 0;
            Event newEvent = new Event(eventName, eventLocation, eventDescription,
                    eventDate, eventTime, signupLimit, currentlySignedUp, signupLimitInput,
                    geolocation, reUseQR, newQR,
                    posterPath, shareQRPath, checkInQRPath, userID, eventID);
            FirestoreController fc = FirestoreController.getInstance();
            fc.addEvent(newEvent);

            Intent intent = new Intent(getBaseContext(), OrganizerEventListActivity.class);
            startActivity(intent);
        }
    }
}
