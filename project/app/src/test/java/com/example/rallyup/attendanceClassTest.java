package com.example.rallyup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import com.example.rallyup.firestoreObjects.Attendance;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.QrCode;
import com.example.rallyup.firestoreObjects.User;
import com.google.zxing.qrcode.encoder.QRCode;

import org.junit.Test;

/**
 * This class contains all the unit tests for the attendance object
 * @author Reimark Ronabio
 */
public class attendanceClassTest {
    private Attendance mockAttendance() {
        Attendance newMock = new Attendance(
                true,
                "048ACC2B534046668F6BAA2EA43F170C",
                5,
                "ka19Vl8P4QH9QQ90kWvm");
        return newMock;
    }

    @Test
    public void testGetVerified() {
        Attendance testAtt = mockAttendance();
        assertTrue(testAtt.isAttendeeVerified());
    }

    @Test
    public void testSetVerified() {
        Attendance testAtt = mockAttendance();
        testAtt.setAttendeeVerified(false);
        assertFalse(testAtt.isAttendeeVerified());
    }
    @Test
    public void testGetEventID () {
        Attendance testAtt = mockAttendance();
        assertEquals("048ACC2B534046668F6BAA2EA43F170C", testAtt.getEventID());
    }

    @Test
    public void testSetEventID () {
        Attendance testAtt = mockAttendance();
        testAtt.setEventID("139ACC2B534046668F6BAA2EA43F170C");
        assertEquals("139ACC2B534046668F6BAA2EA43F170C", testAtt.getEventID());
    }

    @Test
    public void testGetCheckIn() {
        Attendance testAtt = mockAttendance();
        assertEquals(5, testAtt.getTimesCheckedIn());
    }

    @Test
    public void testSetCheckIn() {
        Attendance testAtt = mockAttendance();
        testAtt.setTimesCheckedIn(3);
        assertEquals(3, testAtt.getTimesCheckedIn());
    }

    @Test
    public void testGetUser() {
        Attendance testAtt = mockAttendance();
        assertEquals("ka19Vl8P4QH9QQ90kWvm", testAtt.getUserID());
    }

    @Test
    public void testSetUser() {
        Attendance testAtt = mockAttendance();
        testAtt.setUserID("wo39143Vl8P4QH9QQ90kWvm");
        assertEquals("wo39143Vl8P4QH9QQ90kWvm", testAtt.getUserID());
    }
}