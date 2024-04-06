package com.example.rallyup.uiReference.organizers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.MainActivity;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Attendance;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.User;
import com.example.rallyup.notification.NotificationObject;
import com.example.rallyup.progressBar.ManageMilestoneDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * OrganizerEventDetailsActivity displays detailed information about a specific event that an organizer is organizing, including event poster, event details, sending announcements, viewing milestones.
 * This activity allows the organizer to view the list of attendees for the event by clicking a button and to navigate back to the event list.
 */
public class OrganizerEventDetailsActivity extends AppCompatActivity
    implements FirestoreCallbackListener {
    NotificationObject notificationObject = new NotificationObject(this);

    Button viewEventAttendeesList; // Button to view the list of attendees for the event
    Button viewCheckInQRCode;
    ImageButton orgEventDetailsBackBtn; // ImageButton to navigate back to the event list

    ImageButton milestoneEditButton;
    Button sendNotificationButton;
    EditText editNotificationTitle;
    EditText editNotificationBody;
    ProgressBar progressBar;



    @Override
    public void onGetEvent(Event event) {
        TextView eventView = findViewById(R.id.org_event_details_name);
        TextView eventTime = findViewById(R.id.org_event_details_date);
        TextView eventLocation = findViewById(R.id.org_event_details_location);
        TextView eventDescription = findViewById(R.id.org_register_event_details);
        ImageView eventPoster = findViewById(R.id.org_event_details_image);

        eventView.setText(event.getEventName());
        String unformattedDate = event.getEventDate();
        String formattedDate = getProperDateFormatting(unformattedDate);
        String unformattedTime = event.getEventTime();
        String formattedTime = unformattedTime.substring(0,2) + ":" + unformattedTime.substring(2,4);
        String joinedDateTime = formattedDate + " At " + formattedTime;
        eventTime.setText(joinedDateTime);
        eventLocation.setText(event.getEventLocation());
        eventDescription.setText(event.getEventDescription());

        // Load in poster for this event
        FirestoreController fc = FirestoreController.getInstance();
        fc.getPosterByEventID(event.getPosterRef(), this, eventPoster);
    }

    @Override
    public void onGetAttendants(List<Attendance> attendantList) {
        TextView eventVerifiedAttendeesView = findViewById(R.id.verifed_attendees);
        TextView eventTotalAttendees = findViewById(R.id.total_attendees);

        editNotificationTitle = findViewById(R.id.notification_title);
        editNotificationBody = findViewById(R.id.notification_details);

        eventTotalAttendees.setText(attendantList.size() + " total attendees");

        int count = 0;
        for (Attendance attendance : attendantList) {
            if (attendance.isAttendeeVerified()) count++;
        }
        eventVerifiedAttendeesView.setText(count + " verified attendees");


//        FirestoreController fc = FirestoreController.getInstance();
//        if (!editNotificationTitle.getText().toString().isEmpty() && !editNotificationBody.getText().toString().isEmpty()){
//            fc.getCheckedInUserIDs(attendantList.get(0).getEventID(), this);
//        }
    }

    @Override
    public void onGetUsers(List<User> userList) {

        editNotificationTitle = findViewById(R.id.notification_title);
        editNotificationBody = findViewById(R.id.notification_details);

        String notificationTitle = editNotificationTitle.getText().toString();
        String notificationBody = editNotificationBody.getText().toString();

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("key");

        for (User user : userList){
            if (user.getWantNotifications()){
                sendAnnouncement(user.getFcmToken(), notificationTitle, notificationBody, eventID);
            }
        }
    }

    @Override
    public void onGetImage(Bitmap bm) {
        // The UI XML doesn't have an image thing yet, will need to get the proper ID for it once set
        //ImageView eventPoster = findViewById(R.id.ProgressBarEventPosterView);
        //eventPoster.setImageBitmap(bm);
    }

    /**
     * Called when the activity is starting. This is where most initialization should go. Shows the organizer event details xml and displays for the user
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_details);
        Intent intent = getIntent();
        String eventID = intent.getStringExtra("key");

        String notification_channel_ID =
                getString(R.string.notification_channel_ID_default);
        String notification_channel_name =
                getString(R.string.notification_channel_name_default);
        String notification_channel_description=
                getString(R.string.notification_channel_description_default);

//        notificationObject.createNotificationChannel(
//                notification_channel_ID_milestone,
//                notification_channel_name_milestone,
//                notification_channel_description_milestone,
//                NotificationCompat.PRIORITY_DEFAULT);
        notificationObject.createNotificationChannel(
                notification_channel_ID,
                notification_channel_name,
                notification_channel_description,
                NotificationCompat.PRIORITY_DEFAULT);

        orgEventDetailsBackBtn = findViewById(R.id.organizer_details_back_button); // Initializing back button
        viewEventAttendeesList = findViewById(R.id.event_attendees_button); // Initializing button to view attendees list
        viewCheckInQRCode = findViewById(R.id.view_qr_code_button);

        milestoneEditButton = findViewById(R.id.imageButton5);
        progressBar = findViewById(R.id.progressBar3);
        sendNotificationButton = findViewById(R.id.org_send_notification);

        editNotificationTitle = findViewById(R.id.notification_title);
        editNotificationBody = findViewById(R.id.notification_details);


        // FirestoreController here
        FirestoreController fc = FirestoreController.getInstance();
        fc.getEventByID(eventID, this);
        fc.getEventAttendantsByEventID(eventID, this);

        // TODO: PUT PROGRESS BAR PROGRESS IN THE onGetAttendants AND PUT
        //  progressBar.setProgress(attendantList.size());
        // Need to implement firebase to get the proper count of attendees here
        //setProgressOfEvent(progressBar,70, 100);
        progressBar.setProgress(70);

        // Setting onClickListener for the back button to navigate back to the event list
        orgEventDetailsBackBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(getBaseContext(), OrganizerEventListActivity.class);
            startActivity(intent1);
        });

        // Setting onClickListener for the button to view attendees list
        viewEventAttendeesList.setOnClickListener(view -> {
            Intent intent12 = new Intent(getBaseContext(), EventAttendeesInfoActivity.class);
            intent12.putExtra("eventID", eventID);
            startActivity(intent12);
        });

        // Might not need this anymore, delete when not needed anymore
        milestoneEditButton.setOnClickListener(v -> {
            ManageMilestoneDialog manageMilestoneDialog = new ManageMilestoneDialog();
            manageMilestoneDialog.show(getSupportFragmentManager(), "ManageMilestonesDialog");
        });

        sendNotificationButton.setOnClickListener(v -> {
            if (!editNotificationBody.getText().toString().isEmpty() &&
                    !editNotificationTitle.getText().toString().isEmpty()){

                // Create a new notification/announcement in the Firebase
                // Which then if we go to Attendees side of the activities, they should be able
                // to detect a new notification create for their specific event
                // Test notification
//                notificationObject.createNotification(
//                        MainActivity.class,
//                        notification_channel_ID_milestone,
//                        editNotificationTitle.getText().toString(),
//                        editNotificationBody.getText().toString(),
//                        (R.drawable.poster1),
//                        0,
//                        NotificationCompat.VISIBILITY_PUBLIC,
//                        NotificationCompat.PRIORITY_DEFAULT,
//                        true,
//                        true,
//                        null);
                fc.getCheckedInUserIDs(eventID, OrganizerEventDetailsActivity.this);
            } else {
                Toast toasty = Toast.makeText(OrganizerEventDetailsActivity.this,
                        "Missing title and/or body text.", Toast.LENGTH_SHORT);
                toasty.show();
            }
        });

        // Change the hardcoded int 30 into a variable of the user milestones
        // probably like progressBar.getProgress() >= (attendantList.size()/4*attendantList.size()) * 100
        // progressBar.getProgress() >= attendantList.size()/2
        // progressBar.getProgress() >= attendantList.size()3/4
        // progressBar.getProgress() >= attendantList.size()
        if (progressBar.getProgress() >= 30) {
//            notificationObject.createNotification(MainActivity.class,
//                    notification_channel_ID_milestone,
//                    "Milestone Achieved",
//                    String.format(Locale.getDefault(), "We have %d participants!", progressBar.getProgress()),
//                    (R.drawable.poster1),
//                    0,
//                    NotificationCompat.VISIBILITY_PUBLIC,
//                    NotificationCompat.PRIORITY_DEFAULT,
//                    true,
//                    true,
//                    null);
        }
    }

    private void setProgressOfEvent(ProgressBar progressBar, int currentAttendees, int goalOrMax){
        int maximum = progressBar.getMax();

        // (number of participants / Max or Goal) * maximum
        int percentageOfProgress = (currentAttendees / goalOrMax) * maximum;

        progressBar.setProgress(percentageOfProgress);
    }

    /**
     * For formatting the date stored in firebase into something more user friendly
     * @param date the string of the date
     * @return the string for proper date formatting
     */
    public String getProperDateFormatting(String date) {
        String year = date.substring(0,4);
        String month;
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        int monthNum=Integer.parseInt(date.substring(4,6));
        cal.set(Calendar.MONTH,monthNum);
        month = month_date.format(cal.getTime());
        String day = date.substring(6,8);
        return month + " " + day + ", " + year;
    }

    // TODO:
    //  1 - Make connection to FCM server that is HTTP V1 compliant
    //  2 - Make JSON object that follows the proper HTTP V1 format

    private void callAPI(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        // String url2 follows the legacy/deprecated HTTP format
        //String url2 = "https://fcm.googleapis.com/v1/projects/cmput301-rallyup/messages:send";
        String authorization = "Bearer AAAA40qEz7M:APA91bEctcvtfKy3BZ8nW-xISEtyx0EKKEyjsXLlhgH4v_6x2Qu8dgYA2BuHGenmRCrtzV4lPc_AawyGrKcRS5ovXZYHF2dCWGHKVY2jeRpQMICg6DFRf3NCiXXMjkF6x-R9ovq0kU-T";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", authorization)
                .build();
        client.newCall(request);
    }

    private void sendAnnouncement(String userFCMTokens, String notificationTitle, String notificationBody, String eventID){
        try {
            JSONObject notification =
                    notificationObject.androidNotificationJSONTest(
                            userFCMTokens,
                            notificationTitle,
                            notificationBody,
                            eventID,
                            NotificationObject.ANNOUNCEMENTS);
            callAPI(notification);
        } catch (JSONException e){
            Log.e("OrganizerEventDetailsActivity", "notification JSON object error: ", e);
        }

    }
}