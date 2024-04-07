package com.example.rallyup.uiReference.organizers;

import androidx.annotation.NonNull;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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

        FirestoreController fc = FirestoreController.getInstance();

        TextView eventView = findViewById(R.id.org_event_details_name);
        TextView eventVerifiedAttendeesView = findViewById(R.id.verifed_attendees);
        TextView eventTotalAttendees = findViewById(R.id.total_attendees);
        progressBar = findViewById(R.id.progressBar3);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("key");

        editNotificationTitle = findViewById(R.id.notification_title);
        editNotificationBody = findViewById(R.id.notification_details);

        eventTotalAttendees.setText(String.format(Locale.getDefault(),attendantList.size() + " total attendees"));

        int count = 0;
        for (Attendance attendance : attendantList) {
            if (attendance.isAttendeeVerified()) count++;
        }
        eventVerifiedAttendeesView.setText(String.format(Locale.getDefault(), count + " verified attendees"));
        progressBar.setProgress(count);

        String notification_channel_ID =
                getString(R.string.notification_channel_ID_milestone);

        // TODO: Add milestone criteria
        notificationObject.createNotification(
                    MainActivity.class,
                    notification_channel_ID,
                    "A Milestone Achieved!",
                    String.format(Locale.getDefault(), count + " verified attendees for " + eventView.getText().toString()),
                    (R.drawable.rally_up_title_screen),
                    0,
                    NotificationCompat.VISIBILITY_PUBLIC,
                    NotificationCompat.PRIORITY_DEFAULT,
                    true,
                    false,
                    null);
    }

    @Override
    public void onGetFCMTokens(List<String> fcmTokens) {
        editNotificationTitle = findViewById(R.id.notification_title);
        editNotificationBody = findViewById(R.id.notification_details);

        String notificationTitle = editNotificationTitle.getText().toString();
        String notificationBody = editNotificationBody.getText().toString();

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("key");
        Log.d("onGetUsers", "fcmTokens: " + fcmTokens.size());

        for (String fcmToken: fcmTokens){
            // Send a push notification to the user with the current fcmToken in our fcmTokens list
            sendAnnouncement(fcmToken, notificationTitle, notificationBody, eventID);
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
                getString(R.string.notification_channel_ID_milestone);
        String notification_channel_name =
                getString(R.string.notification_channel_name_milestone);
        String notification_channel_description=
                getString(R.string.notification_channel_description_milestone);

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

        viewCheckInQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sendNotificationButton.setOnClickListener(v -> {
            if (!editNotificationBody.getText().toString().isEmpty() &&
                    !editNotificationTitle.getText().toString().isEmpty()){

                // Where we get the data for our Attendees and sending the notification
                fc.getAttendeeIDsForFCMTokens(eventID, OrganizerEventDetailsActivity.this);
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

    /**
     * Makes a connection to FCM's back end through OkHTTP,
     * and send a request for messaging our JSONObject jsonObject
     * @param jsonObject JSONObject that should follow the Legacy FCM Message format
     */
    private void callAPI(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        // String url follows the legacy/deprecated HTTP format (which works for now 07-APR-2024)
        //String url2 = "https://fcm.googleapis.com/v1/projects/cmput301-rallyup/messages:send";
        String authorization = "Bearer AAAA40qEz7M:APA91bEctcvtfKy3BZ8nW-xISEtyx0EKKEyjsXLlhgH4v_6x2Qu8dgYA2BuHGenmRCrtzV4lPc_AawyGrKcRS5ovXZYHF2dCWGHKVY2jeRpQMICg6DFRf3NCiXXMjkF6x-R9ovq0kU-T";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", authorization)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("OrganizerEventDetailsActivity", "callAPI IO Exception: ",e);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d("OrganizerEventDetailsActivity", "Successfully called Notification API");
                response.close();
            }
        });
    }

    /**
     * Method that will send a Push Notification to user of specified userFCMToken
     * @param userFCMToken String of the auto-generated FCM Token of the user
     * @param notificationTitle String of a the notifications title
     * @param notificationBody String of the notification body text
     * @param eventID String of the eventID that you're currently looking at,
     *                should be part of the .putExtras from the previous activity.
     */
    private void sendAnnouncement(String userFCMToken, String notificationTitle, String notificationBody, String eventID){
        try {
            // Create the JSON Object that follows the deprecated FCM message format
            JSONObject jsonObject = new JSONObject();

            JSONObject notification = new JSONObject();
            notification.put("title", notificationTitle);
            notification.put("body", notificationBody);

            JSONObject data = new JSONObject();
            data.put("eventID", eventID);

            jsonObject.put("notification", notification);
            jsonObject.put("data", data);
            jsonObject.put("to", userFCMToken);

            // Call the API for OkHTTP to connect to FCM's backend
            // which will ask for a request to send our JSONObject message
            callAPI(jsonObject);
        } catch (JSONException e){
            Log.e("OrganizerEventDetailsActivity", "notification JSON object error: ", e);
        }
    }
}