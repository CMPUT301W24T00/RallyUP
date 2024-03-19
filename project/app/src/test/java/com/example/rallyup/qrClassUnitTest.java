package com.example.rallyup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import com.example.rallyup.firestoreObjects.QrCode;
import com.example.rallyup.firestoreObjects.User;
import com.google.zxing.qrcode.encoder.QRCode;

import org.junit.Test;
/**
 * This class contains all the unit tests for the QR object
 * @author Reimark Ronabio
 */
public class qrClassUnitTest {
    private QrCode mockQR() {
        QrCode newMock = new QrCode(
                "1RD8qkMMqyq5mz5CqRQD",
                true,
                "048ACC2B534046668F6BAA2EA43F170C",
                "/images/QR/1RD8qkMMqyq5mz5CqRQD");
        return newMock;
    }

    @Test
    public void testGetQrId() {
        QrCode testQR = mockQR();
        assertEquals("1RD8qkMMqyq5mz5CqRQD", testQR.getQrId());
    }

    @Test
    public void testGetCheckIn() {
        QrCode testQR = mockQR();
        assertTrue(testQR.isCheckIn());
    }

    @Test
    public void testGetEventID() {
        QrCode testQR = mockQR();
        assertEquals("048ACC2B534046668F6BAA2EA43F170C", testQR.getEventID());
    }

    @Test
    public void testGetImage() {
        QrCode testQR = mockQR();
        assertEquals("/images/QR/1RD8qkMMqyq5mz5CqRQD", testQR.getImage());
    }
    @Test
    public void testSetQrId() {
        QrCode testQR = mockQR();
        testQR.setQrId("3AB8qkMMqyq5mz5CqRQD");
        assertEquals("3AB8qkMMqyq5mz5CqRQD", testQR.getQrId());
    }

    @Test
    public void testSetCheckIn() {
        QrCode testQR = mockQR();
        testQR.setCheckIn(false);
        assertFalse(testQR.isCheckIn());
    }
    @Test
    public void testSetEventID() {
        QrCode testQR = mockQR();
        testQR.setEventID("139ACC2B534046668F6BAA2EA43F170C");
        assertEquals("139ACC2B534046668F6BAA2EA43F170C", testQR.getEventID());
    }
    @Test
    public void testSetImage() {
        QrCode testQR = mockQR();
        testQR.setImage("/images/QR/3AB8qkMMqyq5mz5CqRQD");
        assertEquals("/images/QR/3AB8qkMMqyq5mz5CqRQD", testQR.getImage());
    }
}
