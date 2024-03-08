package com.example.rallyup;

import android.graphics.Bitmap;

import com.example.rallyup.firestoreObjects.Attendance;
import com.example.rallyup.firestoreObjects.Event;

import java.io.IOException;
import java.util.List;

public interface FirestoreCallbackListener {
    default void onGetEvent(Event event) throws IOException {
    }

    default void onGetImage(Bitmap bm) {
    }

    default void onGetImage(Bitmap bm, String jobId) {
    }

    default void onGetAttendants(List<Attendance> attendantList) {
    }
}
