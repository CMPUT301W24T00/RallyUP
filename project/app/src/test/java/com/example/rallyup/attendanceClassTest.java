package com.example.rallyup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
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
 * It tests various getters and setters of the attendance class.
 * @author Reimark Ronabio
 */
public class attendanceClassTest {
    /**
     * Creates a mock attendance object for testing
     * @return newMock an attendance object
     */
    private Attendance mockAttendance() {
        Attendance newMock = new Attendance(
                true,
                "048ACC2B534046668F6BAA2EA43F170C",
                5,
                "ka19Vl8P4QH9QQ90kWvm");
        return newMock;
    }

    /**
     * Creates a null mock attendance object for null testing
     * @return null attendance object
     */
    private Attendance nullAttendance() {
        return new Attendance();
    }

    /**
     * This method tests if the null constructor initializes the attendance object correctly
     */
    @Test
    public void testNullAttendance() {
        Attendance nullAtt = nullAttendance();
        assertNull(nullAtt.getEventID());
        assertNull(nullAtt.getUserID());
        assertEquals(false, nullAtt.isAttendeeVerified());
        assertEquals(0, nullAtt.getTimesCheckedIn());
    }

    /**
     * This method tests the isAttendeeVerified method
     */
    @Test
    public void testGetVerified() {
        Attendance testAtt = mockAttendance();
        assertTrue(testAtt.isAttendeeVerified());
    }

    /**
     * This method tests the setAttendeeVerified method
     */
    @Test
    public void testSetVerified() {
        Attendance testAtt = mockAttendance();
        testAtt.setAttendeeVerified(false);
        assertFalse(testAtt.isAttendeeVerified());
    }

    /**
     * This method tests the getEventID method
     */
    @Test
    public void testGetEventID () {
        Attendance testAtt = mockAttendance();
        assertEquals("048ACC2B534046668F6BAA2EA43F170C", testAtt.getEventID());
    }
    /**
     * This method tests the setEventID method
     */
    @Test
    public void testSetEventID () {
        Attendance testAtt = mockAttendance();
        testAtt.setEventID("139ACC2B534046668F6BAA2EA43F170C");
        assertEquals("139ACC2B534046668F6BAA2EA43F170C", testAtt.getEventID());
    }
    /**
     * This method tests the getTimesCheckedIn method
     */
    @Test
    public void testGetCheckIn() {
        Attendance testAtt = mockAttendance();
        assertEquals(5, testAtt.getTimesCheckedIn());
    }
    /**
     * This method tests the setTimesCheckedIn method
     */
    @Test
    public void testSetCheckIn() {
        Attendance testAtt = mockAttendance();
        testAtt.setTimesCheckedIn(3);
        assertEquals(3, testAtt.getTimesCheckedIn());
    }
    /**
     * This method tests the getUserID method
     */
    @Test
    public void testGetUser() {
        Attendance testAtt = mockAttendance();
        assertEquals("ka19Vl8P4QH9QQ90kWvm", testAtt.getUserID());
    }
    /**
     * This method tests the setUserID method
     */
    @Test
    public void testSetUser() {
        Attendance testAtt = mockAttendance();
        testAtt.setUserID("wo39143Vl8P4QH9QQ90kWvm");
        assertEquals("wo39143Vl8P4QH9QQ90kWvm", testAtt.getUserID());
    }


    /**
     * This method tests invalid inputs when setting the times checked in
     */
    @Test
    public void testSetCheckedInWithInvalidValue(){
        Attendance testAtt = mockAttendance();
        IllegalArgumentException negException = assertThrows(IllegalArgumentException.class, () -> {
            testAtt.setTimesCheckedIn(-5);
        });
        assertEquals("Number of times checked in cannot be negative.", negException.getMessage());
    }
}