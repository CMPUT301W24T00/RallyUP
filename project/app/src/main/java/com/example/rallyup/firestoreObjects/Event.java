package com.example.rallyup.firestoreObjects;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Represents an event with a name, location, and description.
 */
public class Event {
    private String eventName;
    private String eventLocation;
    private String eventDescription;
    private String eventDate;
    private String eventTime;
    private int signUpLimit;
    private Boolean signUpLimitBool;
    private Boolean geolocation;
    private Boolean reUseQR;
    private Boolean newQR;
    private String posterRef;
    private String shareQRRef;
    private String checkInQRRef;
    private String ownerID;
    private String eventID;


    /**
     * Constructs a new Event object with the given name, location, description, date, time,
     * sign up limit, permissions for sign up limit, permissions for geolocation, permissions for reusing QR Codes,
     * permissions for generating new QR codes, the String path to the Poster in Firebase Storage,
     * the String path to the share QR Code in Firebase Storage, and the String path to the check-in QR code in Firebase Storage.
     *
     * @param eventName        The name of the event.
     * @param eventLocation    The location of the event.
     * @param eventDescription The description of the event.
     * @param eventDate        The date of the event.
     * @param eventTime        The start time of the event
     * @param signUpLimit      The signup limit of the event
     * @param signUpLimitBool  The signup limit permissions of the event.
     * @param geolocation      The geolocation permissions of the event.
     * @param reUseQR          The permissions for reusing QR Codes for the event.
     * @param newQR            The permissions for generating new QR Codes for the event.
     * @param posterRef        The String path to where the Event poster is stored in firebase icloud storage.
     * @param shareQRRef       The String path to where the share QR code image is stored in firebase icloud storage.
     * @param checkInQRRef     The String path to where the check in QR code image is stored in firebase icloud storage.
     * @param ownerID          A String that represents the unique userID of the event owner.
     * @param eventID          A String that represents the unique event ID.
     */
    public Event(String eventName, String eventLocation, String eventDescription, String eventDate, String eventTime,
                 int signUpLimit, Boolean signUpLimitBool, Boolean geolocation, Boolean reUseQR, Boolean newQR,
                 String posterRef, String shareQRRef, String checkInQRRef, String ownerID, String eventID) {
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.signUpLimit = signUpLimit;
        this.signUpLimitBool = signUpLimitBool;
        this.geolocation = geolocation;
        this.reUseQR = reUseQR;
        this.newQR = newQR;
        this.posterRef = posterRef;
        this.shareQRRef = shareQRRef;
        this.checkInQRRef = checkInQRRef;
        this.ownerID = ownerID;
        this.eventID = eventID;
    }

//    public Event() {}

    /**
     * Constructs a new event object based on a give document snapshop from the firebase
     * @param documentSnapshot
     */
    public Event(DocumentSnapshot documentSnapshot) {
        setEventID(documentSnapshot.getString("eventID"));
        setEventName(documentSnapshot.getString("eventName"));
        setEventDate(documentSnapshot.getString("eventDate"));
        setEventLocation(documentSnapshot.getString("eventLocation"));
        setEventDescription(documentSnapshot.getString("eventDescription"));
    }

    /**
     * Gets the name of the event.
     *
     * @return The name of the event.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the name of the event.
     *
     * @param eventName The new name of the event.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Gets the location of the event.
     *
     * @return The location of the event.
     */
    public String getEventLocation() {
        return eventLocation;
    }

    /**
     * Sets the location of the event.
     *
     * @param eventLocation The new location of the event.
     */
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    /**
     * Gets the description of the event.
     *
     * @return The description of the event.
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets the description of the event.
     *
     * @param eventDescription The new description of the event.
     */
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    /**
     * Gets the date of the event.
     *
     * @return The date of the event.
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * Sets the date of the event.
     *
     * @param eventDate The new date of the event.
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Gets the time of the event.
     *
     * @return The name of the event.
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * Sets the time of the event.
     *
     * @param eventTime The new time of the event.
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * Gets the sign-up limit of the event.
     *
     * @return The sign-up limit of the event.
     */
    public int getSignUpLimit() {
        return signUpLimit;
    }

    /**
     * Sets the sign-up limit of the event.
     *
     * @param signUpLimit The new sign-up limit of the event.
     */
    public void setSignUpLimit(int signUpLimit) {
        this.signUpLimit = signUpLimit;
    }

    /**
     * Gets the permissions for the sign-up limit of the event.
     *
     * @return The permissions for the sign-up limit of the event.
     */
    public Boolean getSignUpLimitBool() {
        return signUpLimitBool;
    }

    /**
     * Sets the permissions for the sign-up limit of the event.
     *
     * @param signUpLimitBool The new permissions for the sign-up limit of the event.
     */
    public void setSignUpLimitBool(Boolean signUpLimitBool) {
        this.signUpLimitBool = signUpLimitBool;
    }

    /**
     * Gets the permissions for the geolocation of the event.
     *
     * @return The permissions for the geolocation of the event.
     */
    public Boolean getGeolocation() {
        return geolocation;
    }

    /**
     * Sets the permissions for the geolocation of the event.
     *
     * @param geolocation The new permissions for the geolocation of the event.
     */
    public void setGeolocation(Boolean geolocation) {
        this.geolocation = geolocation;
    }

    /**
     * Gets the permissions for reusing a QR Code for the event.
     *
     * @return The permissions for reusing a QR Code for the event.
     */
    public Boolean getReUseQR() {
        return reUseQR;
    }

    /**
     * Sets the permissions for reusing a QR Code for the event.
     *
     * @param reUseQR The new permissions for reusing a QR Code for the event.
     */
    public void setReUseQR(Boolean reUseQR) {
        this.reUseQR = reUseQR;
    }

    /**
     * Gets the permissions for generating a new QR Code for the event.
     *
     * @return The permissions for generating a new QR Code for the event.
     */
    public Boolean getNewQR() {
        return newQR;
    }

    /**
     * Sets the permissions for generating a new QR Code for the event.
     *
     * @param newQR The new permissions for generating a new QR Code for the event.
     */
    public void setNewQR(Boolean newQR) {
        this.newQR = newQR;
    }

    /**
     * Gets the path to the event poster image stored in firebase icloud storage.
     *
     * @return The path to the event poster image stored in firebase icloud storage.
     */
    public String getPosterRef() {
        return posterRef;
    }

    /**
     * Sets the path to the event poster image stored in firebase icloud storage.
     *
     * @param posterRef The new path to the event poster image stored in firebase icloud storage.
     */
    public void setPosterRef(String posterRef) {
        this.posterRef = posterRef;
    }

    /**
     * Gets the path to the event share QR code image stored in firebase icloud storage.
     *
     * @return The path to the event share QR code image stored in firebase icloud storage.
     */
    public String getShareQRRef() {
        return shareQRRef;
    }

    /**
     * Sets the path to the event share QR Code image stored in firebase icloud storage.
     *
     * @param shareQRRef The new path to the share QR code image stored in firebase icloud storage.
     */
    public void setShareQRRef(String shareQRRef) {
        this.shareQRRef = shareQRRef;
    }

    /**
     * Gets the path to the event check-in QR code image stored in firebase icloud storage.
     *
     * @return The path to the event check-in QR code image stored in firebase icloud storage.
     */
    public String getCheckInQRRef() {
        return checkInQRRef;
    }

    /**
     * Sets the path to the event check-in QR Code image stored in firebase icloud storage.
     *
     * @param checkInQRRef The new path to the check-in QR code image stored in firebase icloud storage.
     */
    public void setCheckInQRRef(String checkInQRRef) {
        this.checkInQRRef = checkInQRRef;
    }

    /**
     * Returns the user ID of the event organizer.
     *
     * @return The user ID of the event organizer.
     */
    public String getOwnerID() {
        return ownerID;
    }

    /**
     * Sets the owner ID of the event.
     *
     * @param ownerID The user ID associated with the event organizer.
     */
    private void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

      /**
     * Returns the unique event ID.
     *
     * @return The unique event ID associated with the event.
     */
      public String getEventID() {
        return eventID;
    }

      /**
     * Sets the event ID of the event.
     *
     * @param eventID The unique event ID associated with the event.
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
}

