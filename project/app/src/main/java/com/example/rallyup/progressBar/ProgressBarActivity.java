package com.example.rallyup.progressBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

// To access methods from different packages, need to import it like so
import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.MainActivity;
import com.example.rallyup.firestoreObjects.Attendance;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.Registration;
import com.example.rallyup.notification.NotificationObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.rallyup.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * Class that is an Activity to show your ProgressBar
 * @author Chih-Hung Wu
 * @version 1.0.0
 * */
public class ProgressBarActivity extends AppCompatActivity implements FirestoreCallbackListener {

    NotificationObject notificationObject = new NotificationObject(this);

    /**
     * Upon getting an event object it will initialize the necessary views with event details
     * @param event an event object that has a time, name, and other important details
     */
    @Override
    public void onGetEvent(Event event) {
        TextView eventView = findViewById(R.id.ProgressBarEventNameTextView);
        TextView eventTime = findViewById(R.id.ProgressBarEventDateView);
        TextView eventLocation = findViewById(R.id.ProgressBarEventLocationView);
        TextView eventDescription = findViewById(R.id.ProgressBarEventDescriptionView);

        eventView.setText(event.getEventName());
        eventTime.setText(event.getEventTime());
        eventLocation.setText(event.getEventLocation());
        eventDescription.setText(event.getEventDescription());
        ImageView eventPoster = findViewById(R.id.ProgressBarEventPosterView);


        // Load in poster for this event
        // Load in poster for this event
        FirestoreController fc = FirestoreController.getInstance();
        fc.getPosterByEventID(event.getPosterRef(), this, eventPoster);
    }

    /**
     * Upon getting an attendee lists it will initialize the necessary attendee views
     * @param attendantList an array of the attendees in attendance
     */
    @Override
    public void onGetAttendants(List<Attendance> attendantList) {
        //TextView eventVerifiedAttendeesView = findViewById(R.id.ProgressBarEventAttendeesNumberView);
        TextView eventTotalAttendees = findViewById(R.id.ProgressBarEventTotalAttendeesView);

        eventTotalAttendees.setText(attendantList.size() + " total attendees");

//        int count = 0;
//        for (Attendance attendance : attendantList) {
//            if (attendance.isAttendeeVerified()) count++;
//        }
//        eventVerifiedAttendeesView.setText(count + " verified attendees");
    }

    @Override
    public void onGetRegisteredAttendants(List<Registration> registrationList){
        TextView eventVerifiedAttendeesView = findViewById(R.id.verifed_attendees);
        eventVerifiedAttendeesView.setText(registrationList.size() + " registered attendees");
    }

    /*
    @Override
    public void onGetImage(Bitmap bm) {
        ImageView eventPoster = findViewById(R.id.ProgressBarEventPosterView);
        eventPoster.setImageBitmap(bm);
    }*/

    /**
     * Initializes the progress bar activity when it is created for the first time
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set your CONTENT VIEWS
        setContentView(R.layout.activity_progressbar);

        // create the notification channel
        notificationObject.createNotificationChannel(
                getString(R.string.notification_channel_ID_milestone),
                getString(R.string.notification_channel_name_milestone),
                getString(R.string.notification_channel_description_milestone),
                NotificationCompat.PRIORITY_DEFAULT);

        // Initialize your XML items here
        // To stay for final product
        // Top of ProgressBarActivity
        ImageView backToMain = findViewById(R.id.backToMainButtonXML);
        TextView eventView = findViewById(R.id.ProgressBarEventNameTextView);
        ImageView eventPoster = findViewById(R.id.ProgressBarEventPosterView);
        FloatingActionButton editPosterButton = findViewById(R.id.ProgressBarEditEventPosterButton);
        // Event Details
        TextView eventTime = findViewById(R.id.ProgressBarEventDateView);
        TextView eventLocation = findViewById(R.id.ProgressBarEventLocationView);
        TextView eventVerifiedAttendeesView = findViewById(R.id.ProgressBarEventAttendeesNumberView);
        TextView eventTotalAttendees = findViewById(R.id.ProgressBarEventTotalAttendeesView);
        TextView eventDescription = findViewById(R.id.ProgressBarEventDescriptionView);
        TextView eventViewAttendees = findViewById(R.id.ProgressBarEventViewAttendeesView);
        ImageView shareEventImageView = findViewById(R.id.ProgressBarEventShareEventImage);
        Button checkInQRCodeButton = findViewById(R.id.ProgressBarEventCheckInQRCodeButton);
        // Milestones
        ImageView editMilestonesDialogButton = findViewById(R.id.ProgressBarMilestonesEditButton);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        // Announcements
        EditText editAnnouncementTitle = findViewById(R.id.ProgressBarAnnouncementEditTitle);
        EditText editAnnouncementBody = findViewById(R.id.ProgressBarAnnouncementBody);
        Button sendAnnouncementButton = findViewById(R.id.ProgressBarAnnouncementSendButton);

        FirestoreController fc = FirestoreController.getInstance();
        fc.getEventByID("Actual last test before pushing lol", this);
        fc.getEventAttendantsByEventID("Actual last test before pushing lol", this);
        fc.getRegisteredAttendees("Actual last test before pushing lol", this);


        eventViewAttendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the Organizer to U.S. 01.02.1/01.09.1/01.08.01

            }
        });

        checkInQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Takes us to the Event Check-In QR Code Dialog/Fragment
            }
        });


        // Here we would get the Firebase controller to do the following:
        // I want to receive the current number of attendees of this event
        // I want to get the Max Number Of Attendees or the Goal Of Attendees set by the
        // Organizers.
        // I'm not too sure how we're going to keep track or update the progressBar

        //setProgressOfEvent(progressBar, //int of current number of attendees, int of MaxAttendeesOrGoal);

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMainIntent =
                        new Intent(ProgressBarActivity.this, MainActivity.class);
                startActivity(backToMainIntent);
            }
        });

        editPosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Something Something
            }
        });

        shareEventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the share event QR code dialog U.S. - 01.06.01
            }
        });



        editMilestonesDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageMilestoneDialog manageMilestoneDialog = new ManageMilestoneDialog();
                manageMilestoneDialog.show(getSupportFragmentManager(), "ManageMilestonesDialog");
            }
        });

        sendAnnouncementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editAnnouncementBody.getText().toString().equals("") && !editAnnouncementTitle.getText().toString().equals("")){
                    // Create a new notification/announcement in the Firebase
                    // Which then if we go to Attendees side of the activities, they should be able
                    // to detect a new notification create for their specific event
                } else {
                    Toast toasty = Toast.makeText(ProgressBarActivity.this, "Missing title and/or body text.", Toast.LENGTH_SHORT);
                    toasty.show();
                }
            }
        });


    }

    /**
     * Takes in the progress bar, number of attendees, and the goal of the event to set the progess of the event
     * @param progressBar
     * @param currentAttendees
     * @param goalOrMax
     */
    private void setProgressOfEvent(ProgressBar progressBar, int currentAttendees, int goalOrMax){
        int maximum = progressBar.getMax();

        // (number of participants / Max or Goal) * maximum
        int percentageOfProgress = (currentAttendees / goalOrMax) * maximum;

        progressBar.setProgress(percentageOfProgress);
    }
}
