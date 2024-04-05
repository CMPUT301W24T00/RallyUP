package com.example.rallyup.firestoreObjects;

import com.google.firebase.firestore.GeoPoint;

/**
 * Represents a user with email, first name, and last name.
 */
public class User {
    private String email = "Sample email";
    private String firstName = "";
    private String lastName = "";
    private String id = "";
    private String phoneNumber = "";
    private Boolean geolocation = false;
    private GeoPoint latlong;

    /**
     * Default constructor for User.
     */
    public User() {
    }

    /**
     * Constructor for User with email parameter.
     *
     * @param email The email of the user.
     */
    public User(String email) {
        this.email = email;
    }

    /**
     * Constructor mainly used for unit testing
     * @param email the email of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the usr
     * @param id the id of the user
     */
    public User(String email, String firstName, String lastName, String id, String phoneNumber, Boolean geolocation, GeoPoint geoPoint) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.geolocation = geolocation;
        this.latlong = geoPoint;
    }

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
}
