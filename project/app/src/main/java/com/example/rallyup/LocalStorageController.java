package com.example.rallyup;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.UUID;

/**
 * This class contains the local storage controller
 */
public class LocalStorageController {
    private static final LocalStorageController instance = new LocalStorageController();

    private static final String PREF_NAME = "RallyUpPreferences";
    private static final String KEY_USER_ID = "userID";
    private boolean idInitialized = false;

    /**
     * This method retrieves the instance of a local storage controller
     * @return the instance of a local storage controller
     */
    public static LocalStorageController getInstance() {
        return instance;
    }

    /**
     * This method initializes the local user's ID
     * @param context the context for this method
     */
    public void initialization(Context context) {
        if (existsUserID(context)) {
            idInitialized = true;
        } else {
            createNewUserID(context);
        }
    }

    /**
     * Method to retrieve userID from SharedPreferences
     * @param context the context for this method
     * @return the string for the user's id
     */
    public String getUserID(Context context) {
        assert idInitialized;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    /**
     * Method to create userID and save it to SharedPreferences
     * @param context the context for this method
     */
    public void createNewUserID(Context context) {
        FirestoreController fc = FirestoreController.getInstance();
        fc.createUserID(task -> {
            if (task.isSuccessful()) {
                // Document created successfully
                DocumentReference document = task.getResult();
                String userId = document.getId();
                // Save the userID to local storage
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_USER_ID, userId);
                editor.apply();
                idInitialized = true;
            } else {
                // Handle failure
                Exception e = task.getException();
                if (e != null) {
                    Log.e("LocalStorageController", "Error adding document: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Method to check if userID exists in SharedPreferences
     * @param context the context of this method
     * @return the boolean if the userID exists or not
     */
    public boolean existsUserID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(KEY_USER_ID);
    }
}
