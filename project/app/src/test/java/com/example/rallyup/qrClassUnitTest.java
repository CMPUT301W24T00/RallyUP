package com.example.rallyup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import com.example.rallyup.firestoreObjects.QrCode;

import org.junit.Test;

/**
 * This class contains all the unit tests for the QR object.
 * It tests various getters and setters of the QrCode class.
 * @author Reimark Ronabio
 */
public class qrClassUnitTest {

    /**
     * Creates a mock QR object for testing.
     * @return newMock a QR object
     */
    private QrCode mockQR() {
        QrCode newMock = new QrCode(
                true,
                "048ACC2B534046668F6BAA2EA43F170C",
                "/images/QR/1RD8qkMMqyq5mz5CqRQD");
        return newMock;
    }


    /**
     * Tests the isCheckIn method.
     */
    @Test
    public void testGetCheckIn() {
        QrCode testQR = mockQR();
        assertTrue(testQR.isCheckIn());
    }

    /**
     * Tests the getEventID method.
     */
    @Test
    public void testGetEventID() {
        QrCode testQR = mockQR();
        assertEquals("048ACC2B534046668F6BAA2EA43F170C", testQR.getEventID());
    }

    /**
     * Tests the getImage method.
     */
    @Test
    public void testGetImage() {
        QrCode testQR = mockQR();
        assertEquals("/images/QR/1RD8qkMMqyq5mz5CqRQD", testQR.getImage());
    }

    /**
     * Tests the setCheckIn method.
     */
    @Test
    public void testSetCheckIn() {
        QrCode testQR = mockQR();
        testQR.setCheckIn(false);
        assertFalse(testQR.isCheckIn());
    }

    /**
     * Tests the setEventID method.
     */
    @Test
    public void testSetEventID() {
        QrCode testQR = mockQR();
        testQR.setEventID("139ACC2B534046668F6BAA2EA43F170C");
        assertEquals("139ACC2B534046668F6BAA2EA43F170C", testQR.getEventID());
    }

    /**
     * Tests the setImage method.
     */
    @Test
    public void testSetImage() {
        QrCode testQR = mockQR();
        testQR.setImage("/images/QR/3AB8qkMMqyq5mz5CqRQD");
        assertEquals("/images/QR/3AB8qkMMqyq5mz5CqRQD", testQR.getImage());
    }
}
