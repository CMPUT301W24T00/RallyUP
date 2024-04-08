package com.example.rallyup.firestoreObjects;

import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Represents an attendance record for an event.
 */
public class Attendance {

    /**
     * The ID of the event.
     */
    private String eventID;

    /**
     * The number of times the attendee has checked in.
     */
    private int timesCheckedIn;

    /**
     * The ID of the user attending the event.
     */
    private String userID;

    /**
     * Constructs a new empty Attendance object.
     */
    public Attendance() {};

    /**
     * Constructs a new attendance object from a document snapshot
     * @param documentSnapshot a snapshot for the document
     */
    public Attendance(DocumentSnapshot documentSnapshot) {
        setEventID(documentSnapshot.getString("eventID"));
        setUserID(documentSnapshot.getString("userID"));
        setTimesCheckedIn(Math.toIntExact(documentSnapshot.getLong("timesCheckedIn")));
    }

    /**
     * Attendance object constructer mainly used for testing
     * @param eventID string Id of event
     * @param timesCheckedIn integer for number of times checked in
     * @param userID string Id for user
     */
    public Attendance(String eventID, int timesCheckedIn, String userID) {
        this.eventID = eventID;
        this.timesCheckedIn = timesCheckedIn;
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
     * Gets the number of times the attendee has checked in.
     * @return The number of times the attendee has checked in.
     */
    public int getTimesCheckedIn() {
        return timesCheckedIn;
    }

    /**
     * Sets the number of times the attendee has checked in.
     * @param timesCheckedIn The number of times the attendee has checked in.
     */
    public void setTimesCheckedIn(int timesCheckedIn) {
        if (timesCheckedIn < 0) {
            throw new IllegalArgumentException("Number of times checked in cannot be negative.");
        }
        this.timesCheckedIn = timesCheckedIn;
    }

    /**
     * Gets the ID of the user attending the event.
     * @return The ID of the user attending the event.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the ID of the user attending the event.
     * @param userID The ID of the user attending the event.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }
}

