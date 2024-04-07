package com.example.rallyup.uiReference.organizers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.LocalStorageController;
import com.example.rallyup.firestoreObjects.Event;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a dialog fragment that enables the resure of events
 */
public class ChooseReUseEventFragment extends DialogFragment {
    // creating a list of the users previous events, @Marcus would you be able to query the firebase
    // for events with that user ID where the date has already passed
    // and populate this list with those events?
    private List<Event> usersPreviousEvents = new ArrayList<>();
    private String eventChosen;
    private Context context;

    private static final String TAG = "DialogFragment";

    /**
     * Constructor for the re use event fragment
     * @param events a list of event objects
     */
    public ChooseReUseEventFragment(List<Event> events) {
        this.usersPreviousEvents = events;
    }


    // Code sourced from:
    // Reference: https://www.geeksforgeeks.org/how-to-pass-data-from-dialog-fragment-to-activity-in-android/
    /**
     * Sends the String input that represents the Event ID that the user selected to reUse back to the Activities
     * that are listening to this Dialogue
     */
    public interface OnInputListener {
        /**
         * This method send an input
         * @param input a string for input
         */
        void sendInput(String input);
    }

    /**
     * an on input listener
     */
    public OnInputListener mOnInputListener;

    /**
     * Upon attaching
     * @param context the context for this method
     */
    @Override public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            mOnInputListener = (OnInputListener) getActivity();
        }
        catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }

    /**
     * Queries the firebase to populate the array of items displayed in the dialogue for the user to select with
     * the event names (and event IDs?) of past events that they can choose to reuse the QR Codes from\
     *
     */
//    private void populateArray() {
//        // @ Marcus
//        // query the firebase to find all the events where the date has passed and use that to
//        // populate this array with the event name strings converted to CharSequence?
////        usersPreviousEvents = new CharSequence[]{"hello", "goodbye", "test"};
//        FirestoreController fc = FirestoreController.getInstance();
//        LocalStorageController ls = LocalStorageController.getInstance();
//        fc.getEventsByOwnerID(ls.getUserID(context), this);
//    }

    /**
     * Uses the name of the chosen Event to query the firebase to find the Event ID of that chosen event and pass it
     * into the add event activity
     * @ MARCUS we might not need this function depending on how you get the information from the firebase in populateArray()
     * @param eventChosen a String variable that holds the name of the chosen Event
     *
     */
    private String getEventID(String eventChosen) {
        // query the firebase again to find all the past events from this user and pick the event ID
        // that corresponds to that eventName
        String test = "test";
        return test;
    }

    /**
     * Initializes the choose re use event fragment
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     *
     * @return the dialog to be created
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction.
        // populateArray();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Build char sequence
        CharSequence[] seq = new CharSequence[usersPreviousEvents.size()];
        for (int i=0; i < usersPreviousEvents.size(); i++) {
            seq[i] = usersPreviousEvents.get(i).getEventName();
        }

        builder.setTitle("Please Select the Event You Would Like to Reuse QR Codes From:")
                .setItems(seq, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected event.
                        eventChosen = seq[which].toString();
                        String input = getEventID("a");
                        mOnInputListener.sendInput(input);

                    }
                });
        return builder.create();
    }
}
