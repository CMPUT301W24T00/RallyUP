package com.example.rallyup.uiReference.admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Event;
import com.example.rallyup.firestoreObjects.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the image adapter for the admin browse event activity
 */
public class AdminBrowseImagesAdapter extends BaseAdapter implements FirestoreCallbackListener {
    private Context context;
    private LayoutInflater inflater;
    List<Event> events;
    List<User> users;
    List<Object> combinedEventsUsers;
    List<Object> dupedcombinedEventsUsers;
    private int totalTasks;
    private int completedTasks;


    /**
     * Constructs an event adapter
     * @param context the context for this method
     * @param combinedEventsUsers the list of event objects
     */
    public AdminBrowseImagesAdapter(Context context, List<Object> combinedEventsUsers) {
        this.context = context;
        this.combinedEventsUsers = combinedEventsUsers;
        this.inflater = LayoutInflater.from(context);
        this.dupedcombinedEventsUsers = new ArrayList<>();
        dupedList();

    }

    /**
     * This method retrieves the count of objects inside the event list
     * @return an integer for the number of objects in the list
     */
    @Override
    public int getCount() {
        return dupedcombinedEventsUsers.size();
    }

    /**
     * This method retrieves the event object from a position in the adapter
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return the event object based on the position
     */
    @Override
    public Object getItem(int position) {
        return dupedcombinedEventsUsers.get(position);
    }

    /**
     * This method retrieves the id of an object in the adapter
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return the id of the object
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This method gets the string path of an object
     * @param item the object to get path
     * @return the string path of the object
     */
    public String getPath(Object item) {
        String path = null;
        if (item instanceof Event) {
            Event thing = (Event) item;
            path = thing.getPosterRef();
            // Process event
        } else if (item instanceof User) {
            User thing = (User) item;
            path = "images/ProfilePicture/" + thing.getId();
        }
        return path;
    }

    /**
     * This method contains the functionality of the duped list
     */
    public void dupedList() {
        totalTasks = combinedEventsUsers.size();
        completedTasks = 0;

        for (Object item : combinedEventsUsers) {
            String path = getPath(item);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(path);
            // Check if the reference points to an existing object in Firebase Storage
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                // If the reference points to an existing object, add the item to dupedcombinedEventsUsers
                this.dupedcombinedEventsUsers.add(item);

                // Increment the completed tasks counter
                completedTasks++;

                // Check if all tasks are completed
                if (completedTasks == totalTasks) {
                    // All tasks are completed, update your UI or do any post-processing here
                    // For example, notify the adapter that the data has changed
                    notifyDataSetChanged();
                }
            }).addOnFailureListener(exception -> {
                // Handle the case where the reference does not point to an existing object
                Log.e("dupedList", "Failed to get download URL for item: " + item, exception);

                // Increment the completed tasks counter
                completedTasks++;

                // Check if all tasks are completed
                if (completedTasks == totalTasks) {
                    // All tasks are completed, update your UI or do any post-processing here
                    // For example, notify the adapter that the data has changed
                    notifyDataSetChanged();
                }
            });
        }
    }



    /**
     * This method retieves the view of the event adapter
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return the view of the event adapter
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.admin_images_list, parent, false);
        }

        FirestoreController fController = new FirestoreController();
        ImageView adminImages = convertView.findViewById(R.id.admin_image);
        ImageButton deleteButton = convertView.findViewById(R.id.delete_button);

        Object item = this.dupedcombinedEventsUsers.get(position);
        fController.getPosterByEventID(getPath(item), context, adminImages);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dupedcombinedEventsUsers.remove(position);
                notifyDataSetChanged();
                FirestoreController fc = FirestoreController.getInstance();
                fc.deleteImageByPath(getPath(item), AdminBrowseImagesAdapter.this);
            }
        });

        return convertView;
    }
}