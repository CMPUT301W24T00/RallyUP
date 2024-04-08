package com.example.rallyup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.rallyup.firestoreObjects.Notification;

import org.junit.Test;

/**
 * This class contains all the unit tests for the Notification object.
 * It tests various getters and setters of the Notification class.
 */
public class notificationClassUnitTest {

    /**
     * Creates a mock Notification object for testing.
     * @return newNotif a Notification object
     */
    private Notification mockNotif() {
        Notification newNotif = new Notification(
                "048ACC2B534046668F6BAA2EA43F170C",
                "Notification Title",
                "Notification Test Description"
        );
        return newNotif;
    }

    /**
     * Creates a null mock Notification object for null testing.
     * @return nullNotif a null Notification object
     */
    private Notification nullNotif() {
        return new Notification();
    }

    /**
     * Tests if the null constructor initializes the Notification object correctly.
     */
    @Test
    public void testNullNotif() {
        Notification nullNotif = nullNotif();
        assertNull(nullNotif.getEventID());
        assertNull(nullNotif.getDescription());
        assertNull(nullNotif.getTitle());
    }

    /**
     * Tests the getEventID method.
     */
    @Test
    public void testGetEventID() {
        Notification testNotif = mockNotif();
        assertEquals("048ACC2B534046668F6BAA2EA43F170C", testNotif.getEventID());
    }

    /**
     * Tests the setEventID method.
     */
    @Test
    public void testSetEventID() {
        Notification testNotif = mockNotif();
        testNotif.setEventID("123DCC2B534046668F6BAA2EA43F170C");
        assertEquals("123DCC2B534046668F6BAA2EA43F170C", testNotif.getEventID());
    }

    /**
     * Tests the getTitle method.
     */
    @Test
    public void testGetTitle() {
        Notification testNotif = mockNotif();
        assertEquals("Notification Title", testNotif.getTitle());
    }

    /**
     * Tests the setTitle method.
     */
    @Test
    public void testSetTitle() {
        Notification testNotif = mockNotif();
        testNotif.setTitle("New Title");
        assertEquals("New Title", testNotif.getTitle());
    }

    /**
     * Tests the getDescription method.
     */
    @Test
    public void testGetDescription() {
        Notification testNotif = mockNotif();
        assertEquals("Notification Test Description", testNotif.getDescription());
    }

    /**
     * Tests the setDescription method.
     */
    @Test
    public void testSetDescription() {
        Notification testNotif = mockNotif();
        testNotif.setDescription("New Description");
        assertEquals("New Description", testNotif.getDescription());
    }
}
