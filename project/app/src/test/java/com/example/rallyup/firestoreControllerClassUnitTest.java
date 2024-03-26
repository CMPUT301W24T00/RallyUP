package com.example.rallyup;

import static org.mockito.Mockito.*;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.QrCode;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * This class contains all the unit tests for the firestore controller class object.
 * It tests various methods that would set, get, or update/modify values in the firebase
 * @author Reimark Ronabio
 */
@RunWith(MockitoJUnitRunner.class)
public class firestoreControllerClassUnitTest {
    @Mock
    private FirebaseFirestore firebaseFirestore;

    @Mock
    private CollectionReference eventsRef;

    @Mock
    private DocumentReference documentReference;

    @Mock
    private FirestoreCallbackListener callbackListener;

    @Mock
    private FirebaseFirestore firestoreInstance;

    private FirestoreController firestoreController;

    @Before
    public void setUp() {
        // Create an instance of the class under test
        firestoreController = new FirestoreController(firestoreInstance);

        // Setup mock behavior for dependencies
        when(firebaseFirestore.collection("events")).thenReturn(eventsRef);

        // Mock behavior for eventsRef
        when(eventsRef.add(any(Event.class))).thenReturn(mock(Task.class));
    }

    @Test
    public void createEvent_Success() {
        // Mock behavior for eventsRef and documentReference
        when(eventsRef.add(any(Event.class))).thenReturn(mock(Task.class));
        when(documentReference.getId()).thenReturn("eventId");

        // Call the method under test
        firestoreController.createEvent(callbackListener);

        // Verify that eventsRef.add() and callbackListener.onCreateEvent() were called with correct arguments
        verify(eventsRef).add(any(Event.class)); // Using any matcher instead of specific Event object
        verify(documentReference).getId();
        verify(callbackListener).onCreateEvent(any(Event.class)); // Similarly using any matcher
    }

//    @Test
//    public void createEvent_Failure() {
//        // Mock behavior for eventsRef.add() failure
//        Event event = new Event();
//        Task failureTask = mock(Task.class);
//        when(failureTask.addOnSuccessListener(any())).thenReturn(failureTask);
//        when(failureTask.addOnFailureListener(any())).thenReturn(failureTask);
//        when(eventsRef.add(event)).thenReturn(failureTask);
//
//        // Call the method under test
//        firestoreController.createEvent(callbackListener);
//
//        // Verify that eventsRef.add() was called
//        verify(eventsRef).add(event);
//        // Verify that callbackListener.onCreateEvent() was not called (due to failure)
//        verify(callbackListener, never()).onCreateEvent(any(Event.class));
//    }



}
