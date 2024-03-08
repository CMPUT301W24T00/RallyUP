package com.example.rallyup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.rallyup.firestoreObjects.Attendance;
import com.example.rallyup.firestoreObjects.Event;

import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.example.rallyup.firestoreObjects.User;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirestoreController {
    private static final FirestoreController instance = new FirestoreController();

    private final FirebaseFirestore dbRef;
    private final CollectionReference usersRef;
    private final CollectionReference eventsRef;
    private final CollectionReference eventAttendanceRef;
    private final CollectionReference qrRef;


    public FirestoreController() {
        dbRef = FirebaseFirestore.getInstance();
        usersRef = dbRef.collection("users");
        eventsRef = dbRef.collection("events");
        eventAttendanceRef = dbRef.collection("eventAttendance");
        qrRef = dbRef.collection("qrCodes");
    }

    public static FirestoreController getInstance() {
        return instance;
    }


    public void getPosterByEvent(String event, ImageView callbackListener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef;
        storageRef = storage.getReference("/images/Posters/Sample Event Updated");

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch(Exception e) {
            Log.e("FirestoreController", "Error getting picture:" + e);
        }
        File finalLocalFile = localFile;
        storageRef.getFile(finalLocalFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                String path = finalLocalFile.getAbsolutePath();
                //callbackListener.onGetImage(BitmapFactory.decodeFile(path));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreController", "Error getting picture: " + e);
            }
        });
    }

    public void getEventAttendantsByEventID(String eventID, FirestoreCallbackListener callbackListener) {
        Query query = eventAttendanceRef.whereEqualTo("eventID", eventID);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Attendance> attendanceList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    attendanceList.add(new Attendance(documentSnapshot));
                }
                callbackListener.onGetAttendants(attendanceList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreController", "Error getting documents: " + e);
            }
        });
    }

    public void getEventByID(String eventID, FirestoreCallbackListener callbackListener) {

        DocumentReference docRef = eventsRef.document(eventID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Event thisEvent;
                thisEvent = documentSnapshot.toObject(Event.class);
                try {
                    callbackListener.onGetEvent(thisEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreController", "Error getting document: " + e);
            }
        });
    }

    /**
     * Adds a new Event to the event collection in firebase
     *
     * @param event The new event to be added.
     */
    public void addEvent(Event event) {
        // Add the event to the Firestore collection
        HashMap<String, Object> data = new HashMap<>();
        data.put("eventName", event.getEventName());
        data.put("eventLocation", event.getEventLocation());
        data.put("eventDescription", event.getEventDescription());
        data.put("eventDate", event.getEventDate());
        data.put("eventTime", event.getEventTime());
        data.put("signUpLimit", event.getSignUpLimit());
        data.put("signUpLimitBool", event.getSignUpLimitBool());
        data.put("geolocation", event.getGeolocation());
        data.put("reUseQR", event.getReUseQR());
        data.put("newQR", event.getNewQR());
        data.put("posterRef", event.getPosterRef());
        data.put("shareQRRef", event.getShareQRRef());
        data.put("checkInQRRef", event.getCheckInQRRef());

        // event.getName should be replaced with unique event ID
        eventsRef.document(event.getEventName()).set(data);
    }

    /**
     * Generates a new User ID in the Firestore.
     *
     * @@return the new User ID.
     */
    // Create a new user in the Firestore and return its userID
    public void createUserID(final OnCompleteListener<DocumentReference> onCompleteListener) {
        usersRef.add(new User())
                .addOnCompleteListener(onCompleteListener);
    }

    /**
     * Uploads a Uri image to the firebase icloud storage.
     *
     * @param image The Uri image to be uploaded.
     * @param reference The storage reference in which the image will be stored
     */
    public void uploadImage(Uri image, StorageReference reference) {
        //final StorageReference posters = reference.child("images/" + "eventPosters/" + reference);

        reference.putFile(image)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                    }
                });
    }

    /**
     * Uploads a bitmap image to the firebase icloud storage.
     *
     * @param image The bitmap image to be uploaded.
     * @param sReference The storage reference in which the image will be stored
     */
    public void uploadImageBitmap(ImageView image, StorageReference sReference) {
        // Get the data from an ImageView as bytes
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = sReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }

    @Deprecated
    public void getEventAttendantsByEventID(String eventID, final OnSuccessListener<QuerySnapshot> onSuccessListener) {
        Query query = eventAttendanceRef.whereEqualTo("eventID", eventID);
        query.get().addOnSuccessListener(onSuccessListener).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreController", "Error getting documents: " + e);
            }
        });
    }

    @Deprecated
    public void getEventByID(String eventID, final OnSuccessListener<DocumentSnapshot> onSuccessListener) {
        DocumentReference docRef = eventsRef.document(eventID);
        docRef.get().addOnSuccessListener(onSuccessListener).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreController", "Error getting document: " + e);
            }
        });
    }


    public void getPosterByEventID(String posterPath, Context context, ImageView poster) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(posterPath);

        Glide.with(context)
                .load(storageReference)
                .into(poster);
        }


    }


