package com.example.rallyup;

import static org.junit.Assert.assertEquals;

import com.example.rallyup.firestoreObjects.User;

import org.junit.Test;

public class userTest {
    private User mockUser() {
        User newMock = new User("test@gmail.com", "bob", "david", "123");
        return newMock;
    }

    @Test
    public void testGetEmail () {
        User testUser = mockUser();
        assertEquals("test@gmail.com", testUser.getEmail());
    }

    @Test
    public void testSetEmail () {
        User testUser = mockUser();
        testUser.setEmail("notest@gmail.com");
        assertEquals("notest@gmail.com", testUser.getEmail());
    }

    @Test
    public void testGetFirstName () {
        User testUser = mockUser();
        assertEquals("bob", testUser.getFirstName());
    }

    @Test
    public void testSetFirstName () {
        User testUser = mockUser();
        testUser.setFirstName("joe");
        assertEquals("joe", testUser.getFirstName());
    }

    @Test
    public void testGetLastName () {
        User testUser = mockUser();
        assertEquals("david", testUser.getLastName());
    }

    @Test
    public void testSetLastName () {
        User testUser = mockUser();
        testUser.setLastName("matt");
        assertEquals("matt", testUser.getLastName());
    }

    @Test
    public void testGetID () {
        User testUser = mockUser();
        assertEquals("123", testUser.getId());
    }

    @Test
    public void testSetID () {
        User testUser = mockUser();
        testUser.setId("456");
        assertEquals("456", testUser.getId());
    }
}
