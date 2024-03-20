package com.example.rallyup.firestoreObjects;

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
    public User(String email, String firstName, String lastName, String id) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    /**
     * Constructor mainly used for unit testing
     * @param id the id of the user
     * @param email the email of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the usr
     * @param phoneNumber the phone number of the user
     * @param geolocation whether geolocation is enabled for the user
     */
    public User(String id, String email, String firstName, String lastName, String phoneNumber, Boolean geolocation) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.geolocation = geolocation;
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
     * Gets the phone number of the user.
     *
     * @return The phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber The phone number for the user.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets whether geolocation is enabled for the user.
     *
     * @return Whether geolocation is enabled for the user
     */
    public Boolean getGeolocation() {
        return geolocation;
    }

    /**
     * Sets whether geolocation is enabled for the user.
     *
     * @param geolocation The geolocation setting for the user.
     */
    public void setGeolocation(Boolean geolocation) {
        this.geolocation = geolocation;
    }
}
