package com.example.rallyup.firestoreObjects;

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * This class represents the registration record of a user for an event
 */
public class Registration {
    /**
     * The ID of the event.
     */
    private String eventID;

    /**
     * The ID of the user registering for the event.
     */
    private String userID;


    /**
     * Constructs a new empty Registration object.
     */
    public Registration() {};

    /**
     * Constructs a new Registration object from a document snapshot
     * @param documentSnapshot a snapshot for the document
     */
    public Registration(DocumentSnapshot documentSnapshot) {
        setEventID(documentSnapshot.getString("eventID"));
        setUserID(documentSnapshot.getString("userID"));
    }

    /**
     * Registration object constructor
     * @param eventID string Id of event
     * @param userID string Id for user
     */
    public Registration(String eventID, String userID) {
        this.eventID = eventID;
        this.userID = userID;
    }

    /**
     * Gets the ID of the event.
     * @return The ID of the event.
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the ID of the event.
     * @param eventID The ID of the event.
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }


    /**
     * Gets the ID of the user registering for the event.
     * @return The ID of the user registering for the event.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the ID of the user registering for the event.
     * @param userID The ID of the user registering for the event.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }



}
