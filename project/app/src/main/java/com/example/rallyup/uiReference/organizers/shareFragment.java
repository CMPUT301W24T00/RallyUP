package com.example.rallyup.uiReference.organizers;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link shareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class shareFragment extends DialogFragment implements FirestoreCallbackListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String eventID;

    private Event event;
    private FirestoreController controller;

    private ImageButton shareGmail, shareText, shareDownload, checkInGmail, checkInText, checkInDownload;
    private ImageView shareQR, checkInQR;


    @Override
    public void onGetEvent(Event event) {
        this.event = event;
        controller.getPosterByEventID(event.getShareQRRef(), getContext(), shareQR);
        controller.getPosterByEventID(event.getCheckInQRRef(), getContext(), checkInQR);
    }
    public shareFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eventID Parameter 1.
     * @return A new instance of fragment shareFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static shareFragment newInstance(String eventID) {
        shareFragment fragment = new shareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, eventID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventID = getArguments().getString(ARG_PARAM1);
        }
        controller = FirestoreController.getInstance();
        controller.getEventByID(eventID, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share2, container, false);
    }


}