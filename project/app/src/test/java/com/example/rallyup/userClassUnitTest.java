package com.example.rallyup;

import static org.junit.Assert.assertEquals;

import com.example.rallyup.firestoreObjects.User;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

/**
 * This class contains all the unit tests for the user object.
 * It tests various getters and setters of the User class.
 * @author Reimark Ronabio
 */
public class userClassUnitTest {

    /**
     * Creates a mock User object for testing.
     * @return newMock a User object
     */
    private User mockUser() {

        GeoPoint newPoint = new GeoPoint(31.2323, -39.1232);
        User newMock = new User("test@gmail.com",
                "bob",
                "david",
                "ka19Vl8P4QH9QQ90kWvm",
                "7801234567",
                false,
                newPoint);
        return newMock;
    }

    private User nullUser() {
        return new User();
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
}
