package com.example.rallyup.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Class that creates notifications and notification channels
 * @author Chih-Hung Wu
 * */
public class NotificationObject{

    private Context context;

    /**
     * Constructor of NotificationObject
     * @param context This is the context which should be your current activity.
     *                For example: Putting MainActivity.this as the argument
     *                (while inside MainActivity) when you call this constructor
     * */
    public NotificationObject(Context context){
        this.context = context;
    }
    /**
     * Creates a notification channel for your app, with a unique channelID.
     * Should be called as soon as the app starts.
     * Because you must create the notification channel before posting any notifications
     * on Android 8.0 and later, execute this code as soon as your app starts.
     * It's safe to call this repeatedly,
     * because creating an existing notification channel performs no operation.
     * @param channelID A String for the ID of the channel you're going to create,
     *                  not displayed on the app.
     *                  Cannot be "miscellaneous" since that's reserved already.
     * @param channelName The CharSequence of name of the
     *                  notification that will show on the app settings
     * @param channelDescription  The String of the description of the notification
     *                          that will show on the app settings
     * @param importance  An int for how important the notification is,
     *                    which will vary how it displays on the lock screen
     *                    use: NotificationManager.IMPORTANCE_*
     * */
    public void createNotificationChannel(String channelID,
                                          CharSequence channelName,
                                          String channelDescription,
                                          int importance){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
            channel.setDescription(channelDescription);

            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Creates a notification and shows shows it on the app
     * @param classObject A class object (usually ActivityOfInterest.class) of where you want to go when
     *                    clicking the notification
     * @param channelID A String for the ID of the channel the notifications are linked to,
     *      *                  not displayed on the app.
     * @param contentTitle A string of what the title of the notification would show as in the notification popup
     * @param contentText A string of what to show as the body of text of the notification
     * @param notifID An int for what the notification ID should be
     * @param smallIcon An int of what the small icon of the notification should be
     * @param visibility use NotificationCompat.VISIBILITY_* to determine how initially visible you
     *                   want the notifications
     * @param priority use NotificationCompat.PRIORITY_* to determine the initial priority of notifications,
     *                 whether it makes sounds and shows or not, etc.
     * @param autoCancel Boolean to set whether you want the notification to autocancel when clicked
     * @param isBigNotification A boolean to determine whether the notification can be enlarged or not
     * @param bigMessage The message to display if notification is enlarged. Can be null, in case that you
     *                   have isBigNotification as false
     * */
    public void createNotification(Class classObject,
                                   String channelID,
                                   String contentTitle,
                                   String contentText,
                                   int smallIcon,
                                   int notifID,
                                   int visibility,
                                   int priority,
                                   boolean autoCancel,
                                   boolean isBigNotification,
                                   @Nullable CharSequence bigMessage) {
        // Setting up the intent on where we want to go for the notification tap
        Intent intent = new Intent(context, classObject);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setContentIntent(pendingIntent) // Set the intent that fires when the user taps the notification.
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setVisibility(visibility)
                .setPriority(priority)
                .setAutoCancel(autoCancel);

        // If you decide that the notification should be able to be enlarged, and
        // provide information of a larger message
        if (isBigNotification){
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(bigMessage));
        }

        // This is to show the notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notifID, builder.build());
    }

    /**
     * A similar function as createNotification, except this is focused on pictures
     * and allowing expansion of notification to show picture
     * @param classObject A class object (usually ActivityOfInterest.class) of where you want to go when
     *                    clicking the notification
     * @param channelID A String for the ID of the channel the notifications are linked to,
     *                  not displayed on the app.
     * @param contentTitle A string of what the title of the notification would show as in the notification popup
     * @param contentText A string of what to show as the body of text of the notification
     * @param notifID An int for what the notification ID should be
     * @param smallIcon An int of what the small icon of the notification should be
     * @param visibility use NotificationCompat.VISIBILITY_* to determine how initially visible you
     *                   want the notifications
     * @param priority use NotificationCompat.PRIORITY_* to determine the initial priority of notifications,
     *                 whether it makes sounds and shows or not, etc.
     * @param autoCancel Boolean to set whether you want the notification to autocancel when clicked
     * @param bitmap Bitmap of media that you want to show in the notification
     */
    public void createNotificationPictures(Class classObject,
                                           String channelID,
                                           String contentTitle,
                                           String contentText,
                                           int smallIcon,
                                           int notifID,
                                           int visibility,
                                           int priority,
                                           boolean autoCancel,
                                           Bitmap bitmap){

        // Setting up the intent on where we want to go for the notification tap
        Intent intent = new Intent(context, classObject);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context,
                        0,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                .setContentIntent(pendingIntent) // Set the intent that fires when the user taps the notification.
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon((Bitmap) null))
                .setVisibility(visibility)
                .setPriority(priority)
                .setAutoCancel(autoCancel);

        // This is to show the notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notifID, builder.build());

    }

    // Resources/References:
    // https://stackoverflow.com/questions/20117148/how-to-create-json-object-using-string
    // https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages?authuser=1#androidconfig
    // https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages?authuser=1#notification
    // https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages?authuser=1#androidnotification

    public void androidConfigNotifications(JSONObject androidNotificationJSON) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        //JSONObject jsonItem = new JSONObject();

        /*
        * Follow this structure
        * AndroidConfig JSON ==
        * {
        *   "priority": enum (AndroidMessagePriority),
        *   "restricted_package_name": string,
        *   "notification": {object (AndroidNotification)},
        *   "direct_boot_ok": boolean
        * }
        *
        * AndroidNotification JSON ==
        * {
        *   "title": string,
        *   "body": string,
        *   "icon":string (hopefully a RallyUp Icon),
        *   Just Follow the reference
        * }
        * */

        // jsonObject = {"key":value, "key1":value1, {jsonItem}}
        // jsonItem = {"key":value, "key1":value1, ...}

        jsonObject.put("priority", "PRIORITY_UNSPECIFIED");
        jsonObject.put("restriced_package_name", "com.example.rallyup");
        // Assuming that we have created a JSONObject for androidNotificationJSON
        jsonObject.put("notification", androidNotificationJSON);
    }

    public JSONObject androidNotificationJSON(String notifTitle, String notifBody, String channelID) throws JSONException {
        JSONObject notificationJSON = new JSONObject();

        notificationJSON.put("title", notifTitle);
        notificationJSON.put("body", notifBody);
        notificationJSON.put("icon", "res/drawable/rally_up_title_screen.png");


        return notificationJSON;
    }

}