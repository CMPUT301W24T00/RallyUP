package com.example.rallyup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.User;

import org.junit.Test;
public class eventTest {
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

    @Test
    public void testGetEventName () {
        Event testEvent = mockEvent();
        assertEquals("event name", testEvent.getEventName());
    }

    @Test
    public void testGetEventLocation () {
        Event testEvent = mockEvent();
        assertEquals("event location", testEvent.getEventLocation());
    }

    @Test
    public void testGetEventDesc () {
        Event testEvent = mockEvent();
        assertEquals("event description", testEvent.getEventDescription());
    }

    @Test
    public void testGetEventDate () {
        Event testEvent = mockEvent();
        assertEquals("20240321", testEvent.getEventDate());
    }

    @Test
    public void testGetEventTime () {
        Event testEvent = mockEvent();
        assertEquals("1230", testEvent.getEventTime());
    }

    @Test
    public void testGetEventSignUpLimit () {
        Event testEvent = mockEvent();
        assertEquals(20, testEvent.getSignUpLimit());
    }

    @Test
    public void testGetEventSignUpLimitBool() {
        Event testEvent = mockEvent();
        assertTrue(testEvent.getSignUpLimitBool());
    }

    @Test
    public void testGetEventGeolocation() {
        Event testEvent = mockEvent();
        assertFalse(testEvent.getGeolocation());
    }

    @Test
    public void testGetReUseQR() {
        Event testEvent = mockEvent();
        assertFalse(testEvent.getReUseQR());
    }

    @Test
    public void testNewQR() {
        Event testEvent = mockEvent();
        assertTrue(testEvent.getNewQR());
    }

    @Test
    public void testGetEventPosterRef () {
        Event testEvent = mockEvent();
        assertEquals("/images/Posters/048ACC2B534046668F6BAA2EA43F170C", testEvent.getPosterRef());
    }

    @Test
    public void testGetEventShareQRRef () {
        Event testEvent = mockEvent();
        assertEquals("/images/ShareQR/048ACC2B534046668F6BAA2EA43F170C", testEvent.getShareQRRef());
    }

    @Test
    public void testGetEventCheckInQRRef() {
        Event testEvent = mockEvent();
        assertEquals("/images/CheckInQR/048ACC2B534046668F6BAA2EA43F170C", testEvent.getCheckInQRRef());
    }

    @Test
    public void testGetEventOwner () {
        Event testEvent = mockEvent();
        assertEquals("ka19Vl8P4QH9QQ90kWvm", testEvent.getOwnerID());
    }

    @Test
    public void testGetEventID () {
        Event testEvent = mockEvent();
        assertEquals("1222513B4E6D44B19D6A7DFC89CB1F7E", testEvent.getEventID());
    }

    @Test
    public void testSetEmail () {
        User testUser = mockUser();
        testUser.setEmail("notest@gmail.com");
        assertEquals("notest@gmail.com", testUser.getEmail());
    }
}
