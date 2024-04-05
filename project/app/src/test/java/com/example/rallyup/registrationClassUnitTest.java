package com.example.rallyup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.example.rallyup.firestoreObjects.Registration;

import org.junit.Test;

/**
 * This class contains all the unit tests for the registration object.
 * It tests various getters and setters of the registration class.
 *
 * @author Reimark Ronabio
 */
public class registrationClassUnitTest {

    /**
     * Creates a mock registration object for testing.
     *
     * @return a mock registration object
     */
    private Registration mockRegistration() {
        Registration newMock = new Registration(
                "1222513B4E6D44B19D6A7DFC89CB1F7E",
                "ka19Vl8P4QH9QQ90kWvm");
        return newMock;
    }

    /**
     * Creates a null mock registration object for null testing.
     *
     * @return a null registration object
     */
    private Registration nullRegistration() {
        Registration nullMock = new Registration();
        return nullMock;
    }

    /**
     * This method tests if the null constructor initializes the registration object correctly.
     */
    @Test
    public void testNullRegistration() {
        Registration mockReg = nullRegistration();
        assertNull(mockReg.getEventID());
        assertNull(mockReg.getUserID());
    }

    /**
     * This method tests the getEventID method.
     */
    @Test
    public void testGetEventID() {
        Registration mockReg = mockRegistration();
        assertEquals("1222513B4E6D44B19D6A7DFC89CB1F7E", mockReg.getEventID());
    }

    /**
     * This method tests the setEventID method.
     */
    @Test
    public void testSetEventID() {
        Registration mockReg = mockRegistration();
        mockReg.setEventID("3333513B4E6D44B19D6A7DFC89CB1F7E");
        assertEquals("3333513B4E6D44B19D6A7DFC89CB1F7E", mockReg.getEventID());
    }

    /**
     * This method tests the getUserID method.
     */
    @Test
    public void testGetUserID() {
        Registration mockReg = mockRegistration();
        assertEquals("ka19Vl8P4QH9QQ90kWvm", mockReg.getUserID());
    }

    /**
     * This method tests the setUserID method.
     */
    @Test
    public void testSetUserID() {
        Registration mockReg = mockRegistration();
        mockReg.setUserID("sb30Vl8P4QH9QQ90kWvm");
        assertEquals("sb30Vl8P4QH9QQ90kWvm", mockReg.getUserID());
    }
}
