package com.example.rallyup.uiReference.testingClasses;

/**
 * This class contains the stats of an attendee
 */
public class AttendeeStatsClass {
    private String attName;
    private Integer checkInCount;

    /**
     * constructs an attendee based on a given name and their check in count
     * @param attName a string for the name of the attendee
     * @param checkInCount an integer count of their check ins to an event
     */
    public AttendeeStatsClass(String attName, Integer checkInCount) {
        this.attName = attName;
        this.checkInCount = checkInCount;
    }

    /**
     * This method retrieves a name of an attendee
     * @return a string of the attendee's name
     */
    public String getAttName() {
        return attName;
    }

    /**
     * This method sets the attende's name
     * @param attName a string that will be used to set the name
     */
    public void setAttName(String attName) {
        this.attName = attName;
    }

    /**
     * This method gets the count of check in's a user has to an event
     * @return the integer count of check in's
     */
    public Integer getCheckInCount() {
        return checkInCount;
    }

    /**
     * This method sets the number of check in's
     * @param checkInCount the integer used to set the number of check in's
     */
    public void setCheckInCount(Integer checkInCount) {
        this.checkInCount = checkInCount;
    }
}