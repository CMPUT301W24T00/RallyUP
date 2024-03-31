package com.example.rallyup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNull;
import com.example.rallyup.firestoreObjects.Registration;
public class registrationClassUnitTest {
    private Registration mockRegistration() {
        Registration newMock = new Registration(
                "1222513B4E6D44B19D6A7DFC89CB1F7E",
                "ka19Vl8P4QH9QQ90kWvm");
        return newMock;
    }

    private Registration nullRegistration() {
        Registration nullMock = new Registration();
        return nullMock;
    }


}
