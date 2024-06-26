package com.example.rallyup.firestoreObjects;

import com.google.firebase.firestore.GeoPoint;

/**
 * Represents a user with email, first name, and last name.
 */
public class User {
    private String email = "";
    private String firstName = "";
    private String lastName = "";
    private String id = "";
    private String phoneNumber = "";
    private Boolean geolocation = false;
    private GeoPoint latlong;
    private Boolean wantNotifications = false;
    private String fcmToken = "";
    private boolean admin = false;

    /**
     * Default constructor for User.
     */
    public User() {
    }

    /**
     * Constructor for User with email, firstName, lastName, id, phoneNumber, geolocation,
     * latlong, wantNotifications, fcmToken parameters.
     *
     * @param email The email of the user.
     * @param firstName String of the user's self inputted first name
     * @param lastName String of the user's self inputted last name
     * @param id String of the user's ID that's autogenerated when they download the app
     * @param phoneNumber String of the user's phone number without hyphens
     * @param geolocation Whether the user wants to be geolocated or not
     * @param latlong The GeoPoint of the user's last known location
     * @param wantNotifications Whether the user wants notifications or not
     * @param fcmToken The string of the user's FCM Token to be used for receiving notifications
     */

     // used to have only email constructor
     
    public User(String email, String firstName, String lastName,
                String id, String phoneNumber,
                Boolean geolocation, GeoPoint latlong,
                Boolean wantNotifications, String fcmToken) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.geolocation = geolocation;
        this.latlong = latlong;
        this.wantNotifications = wantNotifications;
        this.fcmToken = fcmToken;
    }

//    /**
//     * Constructor mainly used for unit testing
//     * @param email the email of the user
//     * @param firstName the first name of the user
//     * @param lastName the last name of the usr
//     * @param id the id of the user
//     */
//    public User(String email, String firstName, String lastName, String id, String phoneNumber, Boolean geolocation, GeoPoint geoPoint) {
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.id = id;
//        this.phoneNumber = phoneNumber;
//        this.geolocation = geolocation;
//        this.latlong = geoPoint;
//    }

    /**
     * Constructor of a user mainly used for unit testing
     * @param email the email of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param id the id of the user
     * @param phoneNumber the phone number of the user
     * @param geolocation the boolean for geolaction of the user
     * @param latlong the geo point of the user
     */
    public User(String email, String firstName, String lastName, String id, String phoneNumber, Boolean geolocation, GeoPoint latlong) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.geolocation = geolocation;
        this.latlong = latlong;
    }

    /**
     * Gets the email of the user.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email The new email for the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the first name of the user.
     *
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The new first name for the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The new last name for the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the id of the user.
     *
     * @return The id of the user.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id of the user.
     *
     * @param id The id for the user.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the user's phone number String
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's phone number as a string
     * @param phoneNumber String of the user's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the geolocation Boolean of the user
     * @return geolocation Boolean
     */
    public Boolean getGeolocation() {
        return geolocation;
    }

    /**
     * Sets the boolean if the user wants to be tracked or not
     * @param geolocation Boolean
     */
    public void setGeolocation(Boolean geolocation) {
        this.geolocation = geolocation;
    }

    /**
     * Gets the user's GeoPoint latitude longitude
     * @return latlong GeoPoint
     */
    public GeoPoint getLatlong() {
        return latlong;
    }

    /**
     * Sets the user's GeoPoint latitude and longitude
     * @param latlong GeoPoint to be set
     */
    public void setLatlong(GeoPoint latlong) {
        this.latlong = latlong;
    }

    /**
     * Gets the Boolean of the user for wantNotifications
     * @return wantNotifications Boolean
     */
    public Boolean getWantNotifications() {
        return wantNotifications;
    }

    /**
     * Sets the Boolean of the user for wantNotifications
     * @param wantNotifications A Boolean value of whether the user wants notifications or not
     */
    public void setWantNotifications(Boolean wantNotifications) {
        this.wantNotifications = wantNotifications;
    }

    /**
     * Gets the FCM token of the user for Notification purposes
     * @return fcmKey String of the user
     */
    public String getFcmToken() {
        return fcmToken;
    }

    /**
     * Sets the fcmToken variable of the User, which SHOULD be the user's actual FCM Token
     * @param fcmToken String of the User's FCM Token to be
     */
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    /**
     * Checks if the user is an admin
     * @return boolean if admin
     */
    public boolean getAdmin() {
        return admin;
    }

    /**
     * Sets the boolean condition if admin
     * @param admin admim boolean
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
