package com.example.rallyup.qrGeneration;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import android.widget.NumberPicker;


import java.io.IOException;
import java.util.Calendar;

public class AddEvent extends AppCompatActivity {
    EditText eventLocationInput, eventNameInput, eventDescriptionInput;

    TextView eventDateInput, eventTimeInput, uploadPosterText;

    Button createButton;

    // b2 is the back button
    FloatingActionButton eventImageInput, b2;

    CheckBox geoInput, newQRSelect, attendeeSignUpLimitInput;

    NumberPicker attendeeLimitPicker;

    String eventName, eventLocation, eventDescription, eventID;

    // Date in the format year, month, day concatenated together
    // time in the format hour, minute concatenated together in 24 hour time
    Integer eventDate, eventTime;
    Integer signupLimit = 0;
    Boolean geolocation, signupLimitInput;

    ImageView qrView, posterImage;

    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        initializeViews();

        // prompting the user to upload an image when they click on the upload box
        eventImageInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        /**
         * Setting the onClick Listener for picking the date, then setting the text of the pick date button
         * (Clickable textView) To be that selected date
         */
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

        attendeeLimitPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker numberPicker, int scrollState) {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    //We get the different between oldValue and the new value
                    signupLimit = numberPicker.getValue();
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
                    generateQR();
                } else {
                    resetQR();
                }
            }
        });


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateEventClick();
            }
        });


        b2.setOnClickListener(v -> {
            // for clicking the button to go back to the QR page
            switchPage();
        });


    }

    public void initializeViews() {
        b2 = findViewById(R.id.pageSwitcher2);

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
        qrView = findViewById(R.id.qrCodeView);
        newQRSelect = findViewById(R.id.newQrCodeSelect);
        posterImage = findViewById(R.id.uploadPosterView);
    }


    /**
     * Allows the user to upload an image from their device
     * to represent the poster for this event.
     * This method does not take in any parameters, or return any variables
     */
    private void imageChooser()
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
                    // do your operation from here....
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap = null;
                        try {
                            selectedImageBitmap
                                    = MediaStore.Images.Media.getBitmap(
                                    this.getContentResolver(),
                                    selectedImageUri);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        posterImage.setImageBitmap(
                                selectedImageBitmap);
                        uploadPosterText.setVisibility(uploadPosterText.GONE);
                    }
                }
            });

    /**
     * Prompts the user to set the start date of this event by
     * utilizing the Calender widget. Saves the inputted date as an integer
     * that represents Year, Month, and Day concatenated together in that order for ease of sorting.
     * This method does not take in any parameters, or return any variables
     */
    public void getEventDate(){
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
                AddEvent.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        eventDateInput.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        eventDate = dateConcat(year, monthOfYear, dayOfMonth);

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
     * utilizing the TimePicker widget. Saves the inputted time as an integer
     * that represents hour and minute concatenated together in that order for ease of sorting.
     * Set to the 24 Hour clock
     * This method does not take in any parameters, or return any variables
     */
    public void getEventTime(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddEvent.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                eventTimeInput.setText(selectedHour + ":" + selectedMinute);
                eventTime = timeConcat(selectedHour, selectedMinute);
            }
        }, hour, minute, true); //Setting it to 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void setAttendeeLimit() {
        attendeeLimitPicker.setVisibility(attendeeLimitPicker.VISIBLE);
        attendeeLimitPicker.setMaxValue(1000);
        attendeeLimitPicker.setMinValue(1);
    }

    public void resetAttendeeLimit() {
        attendeeLimitPicker.setVisibility(attendeeLimitPicker.GONE);
    }

    public void switchPage() {
        Intent a = new Intent(AddEvent.this, com.example.rallyup.qrGeneration.MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(a);
    }

    // could possibly combine these two functions by passing in a boolean value that would represent whether
    // or not the third value would be included in the concatenation, and passing in 0 or something for the third value for time
    /**
     * Takes 3 integer values and concatenates them all together
     * @param a the first integer value
     * @param b the second integer value
     * @param c the third integer value
     * @return an integer value that represents all 3 integers concatenated together
     */
    static int dateConcat(int a, int b, int c) {

        // Convert all the integers to string
        String s1 = Integer.toString(a);
        String s2 = Integer.toString(b);
        String s3 = Integer.toString(c);

        // Concatenate both strings
        String s = s1 + s2 +s3;

        // Convert the concatenated string
        // to integer
        int d = Integer.parseInt(s);

        // return the formed integer
        return d;
    }

    /**
     * Takes 2 integer values and concatenates them together
     * @param a the first integer value
     * @param b the second integer value
     * @return an integer value that represents both integers concatenated together
     */
    static int timeConcat(int a, int b) {

        // Convert all the integers to string
        String s1 = Integer.toString(a);
        String s2 = Integer.toString(b);

        // Concatenate both strings
        String s = s1 + s2;

        // Convert the concatenated string
        // to integer
        int c = Integer.parseInt(s);

        // return the formed integer
        return c;
    }

    /**
     * Generates a QR Code that contains the name of the event
     * This method does not take in any parameters, or return any variables
     */
    private void generateQR() {
        // throws an error for now if the "Generate New QR Code" Checkbox is
        // checked and there has not been an event name inputted yet. Isn't important for
        // the actual app part 3 as we are setting the data stored in the QR Code to be the unique event ID,
        // so Chidinma is not input validating to deal with that error right now
        String text = eventNameInput.getText().toString();
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix matrix;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 400, 400);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        BarcodeEncoder encoder = new BarcodeEncoder();
        Bitmap bitmap = encoder.createBitmap(matrix);
        qrView.setVisibility(qrView.VISIBLE);
        qrView.setImageBitmap(bitmap);
    }


    /**
     * Resets the QR Code View to be hidden
     * This method does not take in any parameters, or return any variables
     */
    public void resetQR(){
        qrView.setVisibility(qrView.GONE);
    }


    /**
     * Returns a Boolean object that represents whether or not all required fields have been filled in.
     * <p>
     * This method ensures that all the inputs required for successful event creation have been filled in.
     *
     * @return      the Boolean value representing whether or not all required fields have been filled in.
     */
    public Boolean validateInput() {
        if(eventName.isEmpty() || eventLocation.isEmpty() || (eventTimeInput.getText().toString().isEmpty())
        || eventDateInput.getText().toString().isEmpty() || !newQRSelect.isChecked()) {
            Toast.makeText(
                            this,
                            "Please ensure all fields are filled out!",
                            Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        else {
            return true;
        }
    }


    /**
     * This method saves all the input fields in new variables, resets the views of all the input fields on the screen,
     * and passes the saved variables to firebase as an instance of the Event Class.
     *
     */
    public void onCreateEventClick(){
        eventName = String.valueOf(eventNameInput.getText());
        eventLocation = String.valueOf(eventLocationInput.getText());
        geolocation = geoInput.isChecked();
        eventDescription = String.valueOf(eventDescriptionInput.getText());
        signupLimitInput = attendeeSignUpLimitInput.isChecked();

        Boolean inputVal = validateInput();
        if(inputVal.equals(true)) {
            eventNameInput.getText().clear();
            eventLocationInput.getText().clear();
            eventDescriptionInput.getText().clear();
            eventDateInput.setText("");
            eventTimeInput.setText("");
            geoInput.setChecked(false);
            newQRSelect.setChecked(false);
            attendeeSignUpLimitInput.setChecked(false);
            attendeeLimitPicker.setVisibility(attendeeLimitPicker.GONE);

            // send values to fb
            Event newEvent = new Event(eventName, eventLocation, eventDescription);
            FirestoreController fc = FirestoreController.getInstance();
            fc.addEvent(newEvent);
        }
    }
}
