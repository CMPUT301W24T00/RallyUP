package com.example.rallyup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.rallyup.firestoreObjects.User;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

/**
 * This class contains all the unit tests for the user object.
 * It tests various getters and setters of the User class.
 *
 * @author Reimark Ronabio
 */
public class userClassUnitTest {

    /**
     * Creates a mock User object for testing.
     *
     * @return a User object with mock data
     */
    private User mockUser() {
        GeoPoint newPoint = new GeoPoint(31.2323, -39.1232);
        User newMock = new User("test@gmail.com",
                "bob",
                "david",
                "ka19Vl8P4QH9QQ90kWvm",
                "7801234567",
                true,
                newPoint,
                true,
                "dJmfbLxfSzS40YRf-IcJpf:APA91bGUyM-F_A7fClNvo9ocr88DQ2Q63UksJVfEQr1PPyrqOKKm0aan2sgio32zd8lyYFTvc-M1MKPequNEVoNXZ3a9_86EyOaprEsr3JKiLMJxsQ0GY11GWDBmPxEXZliYNLT8xOB3");
        return newMock;
    }

    /**
     * Creates a default User object.
     *
     * @return a default User object
     */
    private User defaultUser() {
        return new User();
    }

    /**
     * This method tests if the default constructor initializes the user object correctly.
     */
    @Test
    public void testDefaultUser() {
        User defUser = defaultUser();

        assertEquals("", defUser.getEmail());
        assertEquals("", defUser.getFirstName());
        assertEquals("", defUser.getLastName());
        assertEquals("", defUser.getId());
        assertEquals("", defUser.getPhoneNumber());
        assertNull(defUser.getLatlong());
        assertFalse(defUser.getGeolocation());
        assertFalse(defUser.getWantNotifications());
        assertEquals("", defUser.getFcmToken());

    }

    /**
     * Tests the getEmail method.
     */
    @Test
    public void testGetEmail () {
        User testUser = mockUser();
        assertEquals("test@gmail.com", testUser.getEmail());
    }

    /**
     * Tests the setEmail method.
     */
    @Test
    public void testSetEmail () {
        User testUser = mockUser();
        testUser.setEmail("notest@gmail.com");
        assertEquals("notest@gmail.com", testUser.getEmail());
    }

    /**
     * Tests the getFirstName method.
     */
    @Test
    public void testGetFirstName () {
        User testUser = mockUser();
        assertEquals("bob", testUser.getFirstName());
    }

    /**
     * Tests the setFirstName method.
     */
    @Test
    public void testSetFirstName () {
        User testUser = mockUser();
        testUser.setFirstName("joe");
        assertEquals("joe", testUser.getFirstName());
    }

    /**
     * Tests the getLastName method.
     */
    @Test
    public void testGetLastName () {
        User testUser = mockUser();
        assertEquals("david", testUser.getLastName());
    }

    /**
     * Tests the setLastName method.
     */
    @Test
    public void testSetLastName () {
        User testUser = mockUser();
        testUser.setLastName("matt");
        assertEquals("matt", testUser.getLastName());
    }

    /**
     * Tests the getId method.
     */
    @Test
    public void testGetID () {
        User testUser = mockUser();
        assertEquals("ka19Vl8P4QH9QQ90kWvm", testUser.getId());
    }

    /**
     * Tests the setId method.
     */
    @Test
    public void testSetID () {
        User testUser = mockUser();
        testUser.setId("ju31Vl8P4QH9QQ90kWvm");
        assertEquals("ju31Vl8P4QH9QQ90kWvm", testUser.getId());
    }

    /**
     * Tests the getPhoneNumber method.
     */
    @Test
    public void testGetPhoneNum() {
        User testUser = mockUser();
        assertEquals("7801234567", testUser.getPhoneNumber());
    }

    /**
     * Tests the setPhoneNumber method.
     */
    @Test
    public void testSetPhoneNum() {
        User testUser = mockUser();
        testUser.setPhoneNumber("7802224567");
        assertEquals("7802224567", testUser.getPhoneNumber());
    }

    /**
     * Tests the getGeolocation method.
     */
    @Test
    public void testGetGeolocation() {
        User testUser = mockUser();
        assertTrue(testUser.getGeolocation());
    }

    /**
     * Tests the setGeolocation method.
     */
    @Test
    public void testSetGeolocation() {
        User testUser = mockUser();
        testUser.setGeolocation(false);
        assertFalse(testUser.getGeolocation());
    }

    /**
     * Tests the getLatLong method.
     */
    @Test
    public void testGetLatLong() {
        GeoPoint expectedLatLong = new GeoPoint(31.2323, -39.1232);

        User testUser = mockUser();

        double delta = 0.000001;

        assertEquals(expectedLatLong.getLatitude(), testUser.getLatlong().getLatitude(), delta);
        assertEquals(expectedLatLong.getLongitude(), testUser.getLatlong().getLongitude(), delta);
    }

    /**
     * Tests the setLatLong method.
     */
    @Test
    public void testSetLatLong() {
        GeoPoint newLatLong = new GeoPoint(70.2392, -102.1555);

        User testUser = mockUser();

        testUser.setLatlong(newLatLong);

        double delta = 0.000001;
        assertEquals(newLatLong.getLatitude(), testUser.getLatlong().getLatitude(), delta);
        assertEquals(newLatLong.getLongitude(), testUser.getLatlong().getLongitude(), delta);
    }

    /**
     * Tests the get want notifications boolean
     */
    @Test
    public void testGetNotifications() {
        User testUser = mockUser();
        assertTrue(testUser.getWantNotifications());
    }
    /**
     * Tests the set want notifications boolean
     */
    @Test
    public void testSetNotifications() {
        User testUser = mockUser();
        testUser.setWantNotifications(false);
        assertFalse(testUser.getWantNotifications());
    }

    /**
     * Tests the get FCM token method
     */
    @Test
    public void testGetFCM() {
        User testUser = mockUser();
        assertEquals("dJmfbLxfSzS40YRf-IcJpf:APA91bGUyM-F_A7fClNvo9ocr88DQ2Q63UksJVfEQr1PPyrqOKKm0aan2sgio32zd8lyYFTvc-M1MKPequNEVoNXZ3a9_86EyOaprEsr3JKiLMJxsQ0GY11GWDBmPxEXZliYNLT8xOB3", testUser.getFcmToken());
    }

    /**
     * Tests the set FCM token method
     */
    @Test
    public void testSetFCM() {
        User testUser = mockUser();
        testUser.setFcmToken("eEWefne39-IcJpf:APA91bGUyM-F_A7fClNvo9ocr88DQ2Q63UksJVfEQr1PPyrqOKKm0aan2sgio32zd8lyYFTvc-M1MKPequNEVoNXZ3a9_86EyOaprEsr3JKiLMJxsQ0GY11GWDBmPxEXZliYNLT8xOB3");
        assertEquals("eEWefne39-IcJpf:APA91bGUyM-F_A7fClNvo9ocr88DQ2Q63UksJVfEQr1PPyrqOKKm0aan2sgio32zd8lyYFTvc-M1MKPequNEVoNXZ3a9_86EyOaprEsr3JKiLMJxsQ0GY11GWDBmPxEXZliYNLT8xOB3", testUser.getFcmToken());
    }

}
