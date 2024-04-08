package com.example.rallyup.uiReference.organizers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
 * Use the {@link shareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class shareFragment extends DialogFragment implements FirestoreCallbackListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String eventID;

    private Event event;
    FirestoreController controller = FirestoreController.getInstance();
    ImageView shareQR, checkInQR;
    Bitmap shareBM, checkInBM;
    String checkInCode, shareCode;


    @Override
    public void onGetQRID(String qrID, String jobId){
        Log.d("TAG", "onGetQRID: " + jobId + " " +qrID);
        if(jobId.equals("checkIn")){
            checkInCode = qrID;
            controller.getBitmapByQRID(checkInCode, jobId, this);
        }
        else{
            shareCode = qrID;
            controller.getBitmapByQRID(shareCode, jobId, this);
        }
    }

    @Override
    public void onGetCheckInBitmap(Bitmap bitmap) {
        checkInBM = bitmap;
    }

    @Override
    public void onGetShareBitmap(Bitmap bitmap) {
        shareBM = bitmap;
    }

    @Override
    public void onGetShareQRPath(String qrPath){
        controller.getPosterByEventID(qrPath, getContext(), shareQR);
    }

    @Override
    public void onGetCheckInQRPath(String qrPath){
        controller.getPosterByEventID(qrPath, getContext(), checkInQR);
    }


    @Override
    public void onGetEvent(Event event) {
        this.event = event;
        controller.getQRIDByEventID("share", eventID, false, this);
        controller.getQRIDByEventID("checkIn", eventID, true, this);
    }

//    @Override
//    public void onGetQrCode(QrCode qrCode, String jobId){
//        if(jobId.equals("share")){
//            controller.getBitmapByQRCode(qrCode, "share", this);
//        }
//        else{
//            controller.getBitmapByQRCode(qrCode, "checkIn", this);
//        }
//    }
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

    //@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventID = getArguments().getString(ARG_PARAM1);
        }
        controller.getEventByID(eventID, this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_share, container,
                false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button shareCheckInQRButton = (Button) view.findViewById(R.id.checkInQRShareButton);
        Button shareEventDetailsQRButton = (Button) view.findViewById(R.id.eventDetailsQRShareButton);
        shareQR = (ImageView) view.findViewById(R.id.shareEventInfoQRCode);
        checkInQR = (ImageView) view.findViewById(R.id.checkInQRCode);
        //Drawable mDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable._icon__email_, null);
        //assert mDrawable != null;
        //Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
        //shareQR.setImageBitmap(mBitmap);
        //checkInQR.setImageBitmap(mBitmap);

        shareCheckInQRButton.setOnClickListener(v -> {
            share(checkInBM, true);
        });
        shareEventDetailsQRButton.setOnClickListener(v -> {
            share(shareBM, false);
        });
    }


    public void share(Bitmap mBitmap, Boolean checkIn){
        //Drawable mDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable._icon__email_, null);
        //assert mDrawable != null;
        //mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
        String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), mBitmap, "QR Code", null);
        Uri fileUri = Uri.parse(path);

        // on below line creating an intent to send sms
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        // on below line putting extra as sms body with the data from edit text
        if(checkIn){
            sendIntent.putExtra("sms_body", "Use this QR Code to check in to my event in the RallyUp app!");
            // on below line putting extra as image uri
        }
        else{
            sendIntent.putExtra("sms_body", "Use this QR Code to share my event details on the RallyUp app!");

        }
        sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        // on below line setting intent type.
        sendIntent.setType("image/png");
        // on below line starting activity to send sms.
        startActivity(sendIntent);
    }



}