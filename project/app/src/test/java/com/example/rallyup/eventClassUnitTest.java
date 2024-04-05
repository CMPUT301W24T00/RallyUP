package com.example.rallyup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import com.example.rallyup.firestoreObjects.Event;

import org.junit.Test;

/**
 * This class contains all the unit tests for the event object.
 * It tests various getters and setters of the Event class.
 * @author Reimark Ronabio
 */
public class eventClassUnitTest {

    /**
     * Creates a mock event object for testing.
     * @return newMock an event object
     */
    private Event mockEvent() {
        Event newMock = new Event(
                "event name",
                "event location",
                "event description",
                "20240321",
                "1230",
                20,
                true,
                false,
                false,
                true,
                "/images/Posters/048ACC2B534046668F6BAA2EA43F170C",
                "/images/ShareQR/048ACC2B534046668F6BAA2EA43F170C",
                "/images/CheckInQR/048ACC2B534046668F6BAA2EA43F170C",
                "ka19Vl8P4QH9QQ90kWvm",
                "1222513B4E6D44B19D6A7DFC89CB1F7E");
        return newMock;
    }

    /**
     * Tests the getEventName method.
     */
    @Test
    public void testGetEventName () {
        Event testEvent = mockEvent();
        assertEquals("event name", testEvent.getEventName());
    }

    /**
     * Tests the getEventLocation method.
     */
    @Test
    public void testGetEventLocation () {
        Event testEvent = mockEvent();
        assertEquals("event location", testEvent.getEventLocation());
    }

    /**
     * Tests the getEventDescription method.
     */
    @Test
    public void testGetEventDesc () {
        Event testEvent = mockEvent();
        assertEquals("event description", testEvent.getEventDescription());
    }

    /**
     * Tests the getEventDate method.
     */
    @Test
    public void testGetEventDate () {
        Event testEvent = mockEvent();
        assertEquals("20240321", testEvent.getEventDate());
    }

    /**
     * Tests the getEventTime method.
     */
    @Test
    public void testGetEventTime () {
        Event testEvent = mockEvent();
        assertEquals("1230", testEvent.getEventTime());
    }

    /**
     * Tests the getSignUpLimit method.
     */
    @Test
    public void testGetEventSignUpLimit () {
        Event testEvent = mockEvent();
        assertEquals(20, testEvent.getSignUpLimit());
    }

    /**
     * Tests the getSignUpLimitBool method.
     */
    @Test
    public void testGetEventSignUpLimitBool() {
        Event testEvent = mockEvent();
        assertTrue(testEvent.getSignUpLimitBool());
    }

    /**
     * Tests the getGeolocation method.
     */
    @Test
    public void testGetEventGeolocation() {
        Event testEvent = mockEvent();
        assertFalse(testEvent.getGeolocation());
    }

    /**
     * Tests the getReUseQR method.
     */
    @Test
    public void testGetReUseQR() {
        Event testEvent = mockEvent();
        assertFalse(testEvent.getReUseQR());
    }

    /**
     * Tests the getNewQR method.
     */
    @Test
    public void testNewQR() {
        Event testEvent = mockEvent();
        assertTrue(testEvent.getNewQR());
    }

    /**
     * Tests the getPosterRef method.
     */
    @Test
    public void testGetEventPosterRef () {
        Event testEvent = mockEvent();
        assertEquals("/images/Posters/048ACC2B534046668F6BAA2EA43F170C", testEvent.getPosterRef());
    }

    /**
     * Tests the getShareQRRef method.
     */
    @Test
    public void testGetEventShareQRRef () {
        Event testEvent = mockEvent();
        assertEquals("/images/ShareQR/048ACC2B534046668F6BAA2EA43F170C", testEvent.getShareQRId());
    }

    /**
     * Tests the getCheckInQRRef method.
     */
    @Test
    public void testGetEventCheckInQRRef() {
        Event testEvent = mockEvent();
        assertEquals("/images/CheckInQR/048ACC2B534046668F6BAA2EA43F170C", testEvent.getCheckInQRId());
    }

    /**
     * Tests the getOwnerID method.
     */
    @Test
    public void testGetEventOwner () {
        Event testEvent = mockEvent();
        assertEquals("ka19Vl8P4QH9QQ90kWvm", testEvent.getOwnerID());
    }

    /**
     * Tests the getEventID method.
     */
    @Test
    public void testGetEventID () {
        Event testEvent = mockEvent();
        assertEquals("1222513B4E6D44B19D6A7DFC89CB1F7E", testEvent.getEventID());
    }

    /**
     * Tests the setEventName method.
     */
    @Test
    public void testSetEventName () {
        Event testEvent = mockEvent();
        testEvent.setEventName("new name");
        assertEquals("new name", testEvent.getEventName());
    }

    /**
     * Tests the setEventLocation method.
     */
    @Test
    public void testSetEventLoc () {
        Event testEvent = mockEvent();
        testEvent.setEventLocation("new loc");
        assertEquals("new loc", testEvent.getEventLocation());
    }

    /**
     * Tests the setEventDescription method.
     */
    @Test
    public void testSetEEventDesc() {
        Event testEvent = mockEvent();
        testEvent.setEventDescription("new desc");
        assertEquals("new desc", testEvent.getEventDescription());
    }

    /**
     * Tests the setEventDate method.
     */
    @Test
    public void testSetEventDate () {
        Event testEvent = mockEvent();
        testEvent.setEventDate("20240423");
        assertEquals("20240423", testEvent.getEventDate());
    }

    /**
     * Tests the setSignUpLimit method.
     */
    @Test
    public void testSetEventSignUpLimit () {
        Event testEvent = mockEvent();
        testEvent.setSignUpLimit(30);
        assertEquals(30, testEvent.getSignUpLimit());
    }

    /**
     * Tests the setSignUpLimitBool method.
     */
    @Test
    public void testSetSignUpLimitBool () {
        Event testEvent = mockEvent();
        testEvent.setSignUpLimitBool(false);
        assertFalse(testEvent.getSignUpLimitBool());
    }

    /**
     * Tests the setGeolocation method.
     */
    @Test
    public void testSetGeoLoc () {
        Event testEvent = mockEvent();
        testEvent.setGeolocation(true);
        assertTrue(testEvent.getGeolocation());
    }

    /**
     * Tests the setReUseQR method.
     */
    @Test
    public void testSetReUseQR () {
        Event testEvent = mockEvent();
        testEvent.setReUseQR(true);
        assertTrue(testEvent.getReUseQR());
    }

    /**
     * Tests the setNewQR method.
     */
    @Test
    public void testSetNewQR () {
        Event testEvent = mockEvent();
        testEvent.setNewQR(false);
        assertFalse(testEvent.getNewQR());
    }

    /**
     * Tests the setPosterRef method.
     */
    @Test
    public void testSetPosterRef() {
        Event testEvent = mockEvent();
        testEvent.setPosterRef("/images/Posters/139ACC2B534046668F6BAA2EA43F170C");
        assertEquals("/images/Posters/139ACC2B534046668F6BAA2EA43F170C", testEvent.getPosterRef());
    }

    /**
     * Tests the setShareQRRef method.
     */
    @Test
    public void testSetShareQRRef() {
        Event testEvent = mockEvent();
        testEvent.setShareQRId("/images/ShareQR/139ACC2B534046668F6BAA2EA43F170C");
        assertEquals("/images/ShareQR/139ACC2B534046668F6BAA2EA43F170C", testEvent.getShareQRId());
    }

    /**
     * Tests the setCheckInQRRef method.
     */
    @Test
    public void testSetCheckInQRRef() {
        Event testEvent = mockEvent();
        testEvent.setCheckInQRId("/images/CheckInQR/139ACC2B534046668F6BAA2EA43F170C");
        assertEquals("/images/CheckInQR/139ACC2B534046668F6BAA2EA43F170C", testEvent.getCheckInQRId());
    }

    // This method is commented out since it's private and not accessible for testing.
    // If you intend to test it, consider making it package-private or protected.
    // @Test
    // public void testSetOwnerID() {
    //     Event testEvent = mockEvent();
    //     testEvent.setOwnerID("jv20Xl8P4QH9QQ90kWvm");
    //     assertEquals("jv20Xl8P4QH9QQ90kWvm", testEvent.getOwnerID());
    // }

    /**
     * Tests the setEventID method.
     */
    @Test
    public void testSetEventID(){
        Event testEvent = mockEvent();
        testEvent.setEventID("3245513B4E6D44B19D6A7DFC89CB1F7E");
        assertEquals("3245513B4E6D44B19D6A7DFC89CB1F7E", testEvent.getEventID());
    }
}
