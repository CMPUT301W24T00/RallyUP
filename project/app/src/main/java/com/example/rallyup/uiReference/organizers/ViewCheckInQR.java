package com.example.rallyup.uiReference.organizers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.QrCode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewCheckInQR#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewCheckInQR extends DialogFragment implements FirestoreCallbackListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String eventID;

    FirestoreController controller = FirestoreController.getInstance();
    ImageView checkInQR;
    String checkInCode;



//    @Override
//    public void onGetQrCode(QrCode qrCode, String jobId){
//        controller.getBitmapByQRCode(qrCode, "checkIn", this);
//    }

    @Override
    public void onGetCheckInQRPath(String qrPath){
        controller.getPosterByEventID(qrPath, getContext(), checkInQR);
    }

    @Override
    public void onGetCheckInBitmap(Bitmap bitmap) {
    }

    @Override
    public void onGetQRID(String qrID, String jobId){
        Log.d("TAG", "onGetQRID: " + jobId + " " +qrID);
        if(jobId.equals("checkIn")){
            checkInCode = qrID;
            controller.getBitmapByQRID(checkInCode, jobId, this);
        }
    }
    public ViewCheckInQR() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eventID The ID of the event.
     * @return A new instance of fragment ViewCheckInQR.
     */
    public static ViewCheckInQR newInstance(String eventID) {
        ViewCheckInQR fragment = new ViewCheckInQR();
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
        controller.getQRIDByEventID("checkIn", eventID, true, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_check_in_q_r, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkInQR = (ImageView) view.findViewById(R.id.checkInQRImage);
        //Drawable mDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable._icon__email_, null);
        //assert mDrawable != null;
        //Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
        //checkInQR.setImageBitmap(mBitmap);
    }
}