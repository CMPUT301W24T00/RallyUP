package com.example.rallyup.firestoreObjects;

/**
 * Notification class represents a notification for an event.
 */
public class Notification {
    private String eventID;
    private String title;
    private String description;
    private String notificationID;

    /**
     * Constructs a new Notification with the specified event ID, title, and description.
     *
     * @param eventID     The ID of the event associated with the notification.
     * @param title       The title of the notification.
     * @param description The description of the notification.
     */
    public Notification(String eventID, String title, String description) {
        this.eventID = eventID;
        this.title = title;
        this.description = description;
    }

    /**
     * Gets the event ID associated with the notification.
     *
     * @return The event ID.
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the event ID associated with the notification.
     *
     * @param eventID The event ID to set.
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Gets the title of the notification.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the notification.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the notification.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the notification.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
