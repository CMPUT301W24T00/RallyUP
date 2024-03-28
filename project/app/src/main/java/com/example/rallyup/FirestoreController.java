package com.example.rallyup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.rallyup.firestoreObjects.Attendance;
import com.example.rallyup.firestoreObjects.Event;

import com.example.rallyup.firestoreObjects.QrCode;
import com.example.rallyup.firestoreObjects.Registration;
import com.google.android.gms.tasks.OnCompleteListener;

import com.example.rallyup.firestoreObjects.User;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * This class represents the firestore controller that contains references to the firebase and important collections
 */
public class FirestoreController {
    private static final FirestoreController instance = new FirestoreController();

    private final FirebaseFirestore dbRef;
    private final CollectionReference usersRef;
    private final CollectionReference eventsRef;
    private final CollectionReference eventAttendanceRef;

    private final CollectionReference eventRegistrationRef;

    private final CollectionReference qrRef;

    /**
     * This method constructs a firestore controller using references to the firecase and important collections
     */
    public FirestoreController() {
        dbRef = FirebaseFirestore.getInstance();
        usersRef = dbRef.collection("users");
        eventsRef = dbRef.collection("events");
        eventAttendanceRef = dbRef.collection("eventAttendance");
        eventRegistrationRef = dbRef.collection("eventRegistration");
        qrRef = dbRef.collection("qrCodes");
    }

    /**
     * This method returns an instance of the firestore controller
     * @return an instance of the firestore controller
     */
    public static FirestoreController getInstance() {
        return instance;
    }


    /**
     * This method takes an event object and allows it to be modified/updated
     * @param event an object containing the details of an event
     * @param callbackListener a listener for the firestore
     */
    public void updateEvent(Event event, FirestoreCallbackListener callbackListener) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("ownerID", event.getOwnerID());
        qrRef.document(event.getEventID()).set(data);
    }

    /**
     * This method creates a new event and sets a unique ID for it
     * @param callbackListener a listener for the firestore
     */
    public void createEvent(FirestoreCallbackListener callbackListener) {

        Event event = new Event();
        eventsRef.add(event).addOnSuccessListener(documentReference -> {
            event.setEventID(documentReference.getId());
            callbackListener.onCreateEvent(event);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * This method takes in a qr code objecet and a bitmap, and allows the user to modify/update a qr code
     * @param qrCode an object containing the details of a qr code
     * @param bm a bitmap
     */
    public void updateQrCode(QrCode qrCode, Bitmap bm) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("eventID", qrCode.getEventID());
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReference();
        StorageReference qrImgRef = storageReference.child("images/QR/"+ qrCode.getQrId());
        uploadImageBitmap(bm, qrImgRef);

        data.put("image", qrImgRef.getPath());
        data.put("checkIn", qrCode.isCheckIn());
        data.put("qrID", qrCode.getQrId());
        qrRef.document(qrCode.getQrId()).set(data);
    }

    /**
     * This method allows the user to create a new QR code
     * @param jobId a string for the identification of a job
     * @param callbackListener a listener for the firestore
     */
    public void createQRCode(String jobId, FirestoreCallbackListener callbackListener) {

        QrCode newQr = new QrCode();
        qrRef.add(newQr).addOnSuccessListener(documentReference -> {
            newQr.setQrId(documentReference.getId());
            callbackListener.onGetQrCode(newQr, jobId);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * This method retrieves the events of a given user
     * @param userID the string identification of a user
     * @param callbackListener a listener for the firestore
     */
    public void getEventsByOwnerID(String userID, FirestoreCallbackListener callbackListener) {
        Query query = eventsRef.whereEqualTo("userID", userID);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {

            List<Event> eventList = new ArrayList<>();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Event thisEvent;
                thisEvent = documentSnapshot.toObject(Event.class);
                eventList.add(thisEvent);
            }
            callbackListener.onGetEvents(eventList);

        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * This method retrieves a user by the user id
     * @param userID a string for the identification of a user
     * @param callbackListener a listener for the firestore
     */
    public void getUserByID(String userID, FirestoreCallbackListener callbackListener) {
        DocumentReference docRef = usersRef.document(userID);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            User user = new User();
            user.setId(documentSnapshot.getId());
            user.setEmail(documentSnapshot.getString("email"));
            user.setFirstName(documentSnapshot.getString("firstName"));
            user.setLastName(documentSnapshot.getString("lastName"));
            user.setPhoneNumber(documentSnapshot.getString("phoneNumber"));
            user.setGeolocation(documentSnapshot.getBoolean("geolocation"));
            // If the user wants to be geolocated, set the GeoPoint
            // Else, set the GeoPoint var as null
            if (Boolean.TRUE.equals(documentSnapshot.getBoolean("geolocation"))){
                user.setLatlong(documentSnapshot.getGeoPoint("latlong"));
            } else {
                user.setLatlong(null);
            }

            callbackListener.onGetUser(user);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting document: " + e));
    }

    /**
     * This method retrieves the poster of an event
     * @param event an object that contains the details of an event
     * @param callbackListener a listener for the firestore
     */
    public void getPosterByEvent(Event event, FirestoreCallbackListener callbackListener) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(event.getPosterRef());

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch(Exception e) {
            Log.e("FirestoreController", "Error getting picture:" + e);
        }
        File finalLocalFile = localFile;
        storageReference.getFile(finalLocalFile).addOnSuccessListener(taskSnapshot -> {
            String path = finalLocalFile.getAbsolutePath();
            callbackListener.onGetImage(BitmapFactory.decodeFile(path));
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting picture: " + e));
    }

    /**
     * This method retireves an event based on the event's QR id
     * @param eventQRID a string for the identification of an event's qr id
     * @param callbackListener a listener for the firestore
     */
    public void getEventByQRID(String eventQRID, FirestoreCallbackListener callbackListener) {
        Log.d("Controller", "getEventByQRID: " +eventQRID);
        Query query = qrRef.whereEqualTo("qrID", eventQRID);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                QrCode thisQR;
                thisQR = documentSnapshot.toObject(QrCode.class);
                Log.d("Controller", "getEventByQRID: " + thisQR.getEventID());

                String eventID = thisQR.getEventID();
                callbackListener.onGetEventID(eventID);
            }
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }


    /**
     * This method gets the event of an attendant based on the id of the event
     * @param eventID a string for the identification of an event
     * @param callbackListener a listener for the firestore
     */
    public void getEventAttendantsByEventID(String eventID, FirestoreCallbackListener callbackListener) {
        Query query = eventAttendanceRef.whereEqualTo("eventID", eventID);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Attendance> attendanceList = new ArrayList<>();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                attendanceList.add(new Attendance(documentSnapshot));
            }
            callbackListener.onGetAttendants(attendanceList);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * This method retrieves all events that have not yet started based on the current date
     * @param year an integer for the year
     * @param month an integer for the month
     * @param day an integer for the day
     * @param callbackListener a listener for the firestore
     */
    public void getEventsByDate(int year, int month, int day, FirestoreCallbackListener callbackListener) {
        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Event> EventList = new ArrayList<>();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Event thisEvent;
                thisEvent = documentSnapshot.toObject(Event.class);
                String eDate = thisEvent.getEventDate();
                if(eDate != null){
                    if(Integer.parseInt(eDate.substring(0, 4)) >= year){
                        if(Integer.parseInt(eDate.substring(4, 6)) >= month){
                            EventList.add(thisEvent);
                        }
                    }
                }
            }
            callbackListener.onGetEvents(EventList);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * This method retrieves all events to be displayed on Admin screens
     * @param callbackListener a listener for the firestore
     */
    public void getAdminEvents(FirestoreCallbackListener callbackListener) {
        eventsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Event> eventList = new ArrayList<>();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Event thisEvent;
                thisEvent = documentSnapshot.toObject(Event.class);
                if (thisEvent.getEventName() != null) {
                    eventList.add(thisEvent);
                }
            }
            callbackListener.onGetEvents(eventList);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));

    }

    /**
     * This method retrieves all user profiles to be displayed on Admin screens
     * @param callbackListener a listener for the firestore
     */
    public void getAdminProfiles(FirestoreCallbackListener callbackListener) {
        usersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<User> userList = new ArrayList<>();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                User thisUser;
                thisUser = documentSnapshot.toObject(User.class);
                if(thisUser.getFirstName() != null){
                    userList.add(thisUser);
                }
            }
            callbackListener.onGetUsers(userList);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));

    }

    /**
     * This method retrieves all eventID's of the events a specified user has registered for
     * @param userID the unique ID of the user
     * @param callbackListener a listener for the firestore
     */
    public void getEventsByUserID(String userID, FirestoreCallbackListener callbackListener){
        List<String> eventIDs = new ArrayList<>();
        Query query = eventRegistrationRef.whereEqualTo("userID", userID);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Registration thisRegistration;
                thisRegistration = documentSnapshot.toObject(Registration.class);
                String eventID = thisRegistration.getEventID();
                if(eventID != null){
                    eventIDs.add(eventID);
                }
            }
            getEventListFromEventIDs(eventIDs, callbackListener);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * This method uses a list of event IDs to create a list of event objects
     * @param eventIDS a list of all the eventIDs of the events the user has registered for
     * @param callbackListener a listener for the firestore
     */
    public void getEventListFromEventIDs(List<String> eventIDS, FirestoreCallbackListener callbackListener){
        List<Event> events = new ArrayList<>();
        for(String eventID: eventIDS) {
            Query query = eventsRef.whereEqualTo("eventID", eventID);
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Event thisEvent;
                    thisEvent = documentSnapshot.toObject(Event.class);
                    if(thisEvent.getEventName() != null){
                        events.add(thisEvent);
                    }
                }
                callbackListener.onGetEvents(events);
            }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
        }
    }


    /**
     * This method queries the firestore to find the eventAttendance instance associated
     * with the inputted user and the inputted eventID, and update the number of times checked in for that instance,
     * if an instance is not found one is created
     * @param eventID the event being checked in to
     * @param userID the user checking in
     * @param verified an Boolean representing whether or not this user signed up for this event
     * @param callbackListener a listener for the firestore
     */
    public  void updateAttendance(String eventID, String userID, Boolean verified, FirestoreCallbackListener callbackListener){
        Query query = eventAttendanceRef.whereEqualTo("userID", userID);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isEmpty()){ // if the user doesn't have any rows in the eventAttendanceRef table right now we make a new instance for this event
                Log.d("GETCHECKINS", "getCheckIns: couldn't find anything");
                int checkIns = 1;
                Attendance checkIn = new Attendance(verified, eventID, checkIns, userID);
                addAttendance(checkIn);
            }
            else {
                // making a list of all the instances of Attendance found for this userID
                boolean found = false; // creating a boolean that represents whether this specific eventAttendance instance we're looking for is in the list
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Attendance thisAttendance;
                    thisAttendance = documentSnapshot.toObject(Attendance.class);
                    if (thisAttendance.getEventID().equals(eventID) && thisAttendance.isAttendeeVerified() == verified) { // if the specific eventID and verified mode is found
                        documentSnapshot.getReference().update("timesCheckedIn", thisAttendance.getTimesCheckedIn()+1);
                        found = true;
                    }
                }

                if(!found){ // if we were unable to find it we create a new instance for this event attendance
                    int checkIns = 1;
                    Attendance checkIn = new Attendance(verified, eventID, checkIns, userID);
                    addAttendance(checkIn);
                }
            }
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * Method that updates the specified field with a String value for a specific user.
     * @param userID String of the specified userID
     * @param userField String of the name of the field to be updated
     * @param userValue String of the value to be updated into the field
     * @param callbackListener a listener for firestore
     */
    public void updateUserStringFields(String userID, String userField, String userValue, FirestoreCallbackListener callbackListener){
        // .update needs to be on ACTUAL EXISTING DOCUMENT
        // otherwise it will throw an error
        // Another way to do this is to use .set() to recreate ALL existing FIELDS
        usersRef.document(userID)
                .update(userField, userValue)
                .addOnSuccessListener(unused ->
                        Log.d("FirestoreController",
                                "User document successfully updated!"))
                .addOnFailureListener(e ->
                        Log.w("FirestoreController",
                                "Error updating User document", e));
    }

    /**
     * Method that updates the specified field with a Boolean value for a specific user
     * @param userID String of the specified userID
     * @param userField String of the name of the field to be updated
     * @param userValue Boolean of the value to be updated into the field
     * @param callbackListener a listener for firestore
     */
    public void updateUserBooleanFields(String userID, String userField, Boolean userValue, FirestoreCallbackListener callbackListener){
        usersRef.document(userID)
                .update(userField, userValue)
                .addOnSuccessListener(unused ->
                        Log.d("FirestoreController",
                                "User document successfully updated!"))
                .addOnFailureListener(e ->
                        Log.w("FirestoreController",
                                "Error updating User document", e));
    }

    /**
     * Method that updates the specified field with a GeoPoint value for a specific user
     * @param userID String of the specified userID
     * @param userField String of the name of the field to be updated
     * @param userGeoPoint GeoPoint of the value to be updated into the field
     * @param callbackListener a listener for firestore
     */
    public void updateUserGeoPointFields(String userID, String userField, GeoPoint userGeoPoint, FirestoreCallbackListener callbackListener){
        usersRef.document(userID)
                .update(userField, userGeoPoint)
                .addOnSuccessListener(unused ->
                        Log.d("FirestoreController",
                                "User GeoPoint updated successfully"))
                .addOnFailureListener(e ->
                        Log.w("FirestoreController",
                                "Error updating User GeoPoint", e));
    }

    /**
     * This method gets an event based on the id of the event
     * @param eventID a string for the identification of the event
     * @param callbackListener a listener for the firestore
     */
    public void getEventByID(String eventID, FirestoreCallbackListener callbackListener) {
        DocumentReference docRef = eventsRef.document(eventID);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Event thisEvent;
            thisEvent = documentSnapshot.toObject(Event.class);
            callbackListener.onGetEvent(thisEvent);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting document: " + e));
    }

    /**
     * This method gets the registration information of an an event based on the id of the event
     * @param eventID a string for the identification of the event
     * @param callbackListener a listener for the firestore
     */
    public void getRegistrationInfo(String eventID, FirestoreCallbackListener callbackListener) {
        DocumentReference docRef = eventsRef.document(eventID);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Object[] objects = new Object[3];
            Event thisEvent;
            thisEvent = documentSnapshot.toObject(Event.class);
            assert thisEvent != null;
            if(thisEvent.getSignUpLimitBool() != null) {
                objects[0] = thisEvent.getSignUpLimitBool();
                objects[1] = thisEvent.getSignUpLimit();
                objects[2] = thisEvent.getCurrentlySignedUp();
                callbackListener.onGetRegistrationInfo(objects);
            }
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting document: " + e));
    }

    /**
     * This method creates an eventRegistration instance associated
     * with the inputted user and the inputted eventID, if one is already made a message is displayed
     * @param eventID the event being registered for
     * @param userID the user checking in
     * @param callbackListener a listener for the firestore
     */
    public void newRegistration(String eventID, String userID, Context context, FirestoreCallbackListener callbackListener) {
        Query query = eventRegistrationRef.whereEqualTo("userID", userID);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isEmpty()){ // if the user doesn't have any rows in the eventRegistration table right now we make a new instance for this event
                Log.d("NEWREGISTRATION", "getRegistration: couldn't find anything");
                Registration register = new Registration(eventID, userID);
                //upload to firebase
                addRegistration(register);
                incrementRegistered(eventID);
                Toast.makeText(context, "Registration Successful!", Toast.LENGTH_LONG).show();
            }
            else {
                boolean found = false; // creating a boolean that represents whether this specific eventRegistration instance we're looking for is in the list
                // making a list of all the instances of Registration found for this userID
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Registration aRegistration;
                    aRegistration = documentSnapshot.toObject(Registration.class);
                    if (aRegistration.getEventID().equals(eventID)) { // if the specific eventID is found
                        found = true;
                        Toast.makeText(context, "You have already registered for this event!", Toast.LENGTH_LONG).show();
                    }
                }
                if(!found){ // if we were unable to find it we create a new instance for this event registration
                    Registration register = new Registration(eventID, userID);
                    //upload to firebase
                    addRegistration(register);
                    incrementRegistered(eventID);
                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * This method increments the count of registered users for a specified event by 1
     * @param eventID the event being incremented
     */
    public void incrementRegistered(String eventID) {
        DocumentReference docRef = eventsRef.document(eventID);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Event thisEvent;
            thisEvent = documentSnapshot.toObject(Event.class);
            assert thisEvent != null;
            int newRegistered = thisEvent.getCurrentlySignedUp() + 1;
            documentSnapshot.getReference().update("currentlySignedUp", newRegistered);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * This method is for determining whether or not a user has registered for the event they are attempting to check-in to
     * @param eventID the event being checked-in to
     * @param userID the user checking-in
     * @param callbackListener a listener for the firestore
     *
     */
    public void getVerified(String eventID, String userID, FirestoreCallbackListener callbackListener) {
        Query query = eventRegistrationRef.whereEqualTo("userID", userID);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            boolean verified = false;
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Registration aRegistration;
                aRegistration = documentSnapshot.toObject(Registration.class);
                if (aRegistration.getEventID().equals(eventID)) { // if the specific eventID is found
                    verified = true;
                }
            }
            callbackListener.onGetVerified(verified);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * This method retrieves all userID's of the users checked-in to a specific event
     * @param eventID the unique ID of the user
     * @param callbackListener a listener for the firestore
     */
    public void getCheckedInUserIDs(String eventID, FirestoreCallbackListener callbackListener) {
        Query query = eventRegistrationRef.whereEqualTo("eventID", eventID);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> userList = new ArrayList<>();
            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Registration aRegistration;
                aRegistration = documentSnapshot.toObject(Registration.class);
                String aUserID = aRegistration.getUserID();
                if(aUserID != null){
                    userList.add(aUserID);
                }
            }
            getCheckedInUsers(userList, callbackListener);
        }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
    }

    /**
     * This method uses a list of user IDs to create a list of user objects
     * @param userList a list of all the eventIDs of the events the user has registered for
     * @param callbackListener a listener for the firestore
     */
    public void getCheckedInUsers(List<String> userList, FirestoreCallbackListener callbackListener) {
        List<User> users = new ArrayList<>();
        for(String userID : userList){
            Query query = usersRef.whereEqualTo("userID", userID);
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User aUser;
                    aUser = documentSnapshot.toObject(User.class);
                    if(aUser.getFirstName() != null){
                        users.add(aUser);
                    }
                }
                callbackListener.onGetUsers(users);
            }).addOnFailureListener(e -> Log.e("FirestoreController", "Error getting documents: " + e));
        }
    }

    /**
     * Adds a new Event to the event collection in firebase
     *
     * @param event The new event to be added.
     */
    public void addEvent(Event event) {
        // Add the event to the Firestore collection
        HashMap<String, Object> data = new HashMap<>();
        data.put("eventName", event.getEventName());
        data.put("eventLocation", event.getEventLocation());
        data.put("eventDescription", event.getEventDescription());
        data.put("eventDate", event.getEventDate());
        data.put("eventTime", event.getEventTime());
        data.put("signUpLimit", event.getSignUpLimit());
        data.put("currentlySignedUp", event.getCurrentlySignedUp());
        data.put("signUpLimitBool", event.getSignUpLimitBool());
        data.put("geolocation", event.getGeolocation());
        data.put("reUseQR", event.getReUseQR());
        data.put("newQR", event.getNewQR());
        data.put("posterRef", event.getPosterRef());
        data.put("shareQRRef", event.getShareQRRef());
        data.put("checkInQRRef", event.getCheckInQRRef());
        data.put("userID", event.getOwnerID());
        data.put("eventID", event.getEventID());

        eventsRef.document(event.getEventID()).set(data);
    }

    public void addAttendance(Attendance attendance) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("attendeeVerified", attendance.isAttendeeVerified());
        data.put("eventID", attendance.getEventID());
        data.put("timesCheckedIn", attendance.getTimesCheckedIn());
        data.put("userID", attendance.getUserID());
        // create a unique attendance ID

        String attendanceID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        eventAttendanceRef.document(attendanceID).set(data);
    }

    public void addRegistration(Registration registration) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("eventID", registration.getEventID());
        data.put("userID", registration.getUserID());

        String registrationID = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        eventRegistrationRef.document(registrationID).set(data);
    }

    /**
     * Generates a new User ID in the Firestore.
     * @param onCompleteListener a listener of type document reference
     */
    // Create a new user in the Firestore and return its userID
    public void createUserID(final OnCompleteListener<DocumentReference> onCompleteListener) {
        usersRef.add(new User())
                .addOnCompleteListener(onCompleteListener);
    }

    /**
     * Uploads a Uri image to the firebase icloud storage.
     *
     * @param image The Uri image to be uploaded.
     * @param reference The storage reference in which the image will be stored
     */
    public void uploadImage(Uri image, StorageReference reference) {
        //final StorageReference posters = reference.child("images/" + "eventPosters/" + reference);

        reference.putFile(image)
                .addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                })
                .addOnSuccessListener(taskSnapshot -> {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                });
    }

    /**
     * Uploads a bitmap image to the firebase icloud storage.
     *
     * @param image The bitmap image to be uploaded.
     * @param sReference The storage reference in which the image will be stored
     */
    public void uploadImageBitmap(ImageView image, StorageReference sReference) {
        // Get the data from an ImageView as bytes
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = sReference.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
        });
    }

    /**
     * This method uploads an image bitmap
     * @param bitmap an object containing the details of a bitmap
     * @param sReference an object containing the details of the storage reference
     */
    public void uploadImageBitmap(Bitmap bitmap, StorageReference sReference) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = sReference.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
        });
    }

    public void downloadFile(, StorageReference storageReference){

    }


    /**
     * This method retrieves the poster of an event
     * @param posterPath the string for the path of the poster
     * @param context the context for this method
     * @param poster an imageview object of a poster
     */
    public void getPosterByEventID(String posterPath, Context context, ImageView poster) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(posterPath);
        Glide.with(context)
                .load(storageReference)
                .into(poster);
    }
}



