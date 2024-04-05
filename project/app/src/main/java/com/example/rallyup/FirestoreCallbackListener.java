package com.example.rallyup;

import android.graphics.Bitmap;

import com.example.rallyup.firestoreObjects.Attendance;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.QrCode;
import com.example.rallyup.firestoreObjects.User;

import java.util.List;

/**
 * This interface contains all methods that occur whenever an attribute is required from the firestore
 */
public interface FirestoreCallbackListener {

    /**
     * Upon getting an event
     * @param event an object containing the events details
     */
    default void onGetEvent(Event event) {
    }

    /**
     * Upon getting an event's ID
     * @param eventID the identifying string for an event object
     */
    default void onGetEventID(String eventID) {
    }

    /**
     * Upon getting the registration information of an event
     * @param objects a list of objects containing the registration details.
     *                The first element in the list is a boolean that represents whether or not there is a registration limit set for the event
     *                The second element in the list is an integer for the registration limit. It is set to -1 if a registration limit is not set
     *                The third element in the list is an integer representing how many people are currently registered for the event
     */
    default void onGetRegistrationInfo(Object[] objects){
    }

    /**
     * Upon getting the registration status of a user for an event
     * @param verified a boolean value that represents whether or not the user is registered for the event
     */
    default void onGetVerified (boolean verified){
    }

    /**
     * Upon getting a list of event objects
     * @param eventList a list of event objects
     */
    default void onGetEvents(List<Event> eventList) {
    }


    /**
     * Upon getting a list of User objects
     * @param userList a list of user objects
     */
    default void onGetUsers(List<User> userList){
    }

    /**
     * Upon getting an image
     * @param bm a bitmap of the image
     */
    default void onGetImage(Bitmap bm) {
    }

    /**
     * Upon getting an image but with job id
     * @param bm a bit map of the image
     * @param jobId the string identification for a job
     */
    default void onGetImage(Bitmap bm, String jobId) {
    }

    /**
     * Upon getting a list of attendants
     * @param attendantList a list of attendance objects
     */
    default void onGetAttendants(List<Attendance> attendantList) {
    }

    /**
     * Upon getting a user
     * @param user an object containing the details of a user
     */
    default void onGetUser(User user) {
    }

    /**
     * Upon getting a qr code with the job id
     * @param qrCode an object containing the details of a qr code
     * @param jobId the string identification of a job
     */
    default void onGetQrCode(QrCode qrCode, String jobId) {
    }

    /**
     * Upon the creation of an event object
     * @param event an object containing the details of an event
     */
    default void onCreateEvent(Event event) {

    }

    /**
     * Upon the amount of times a user checked in to a specific event
     * @param timesCheckedIn an Integer that represents how many times the user has checked in to that event
     */
    default void onGetCheckIns(Integer timesCheckedIn) {

    }
}
