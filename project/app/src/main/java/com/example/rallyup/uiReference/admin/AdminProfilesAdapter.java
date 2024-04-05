package com.example.rallyup.uiReference.admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rallyup.FirestoreCallbackListener;
import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.User;

import java.util.List;

public class AdminProfilesAdapter extends BaseAdapter implements FirestoreCallbackListener {
    private Context context;
    private List<User> userList;
    private LayoutInflater inflater;

    /**
     * Constructs an event adapter
     * @param context the context for this method
     * @param eventList the list of event objects
     */
    public AdminProfilesAdapter(Context context, List<User> eventList) {
        this.context = context;
        this.userList = eventList;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * This method retrieves the count of objects inside the event list
     * @return an integer for the number of objects in the list
     */
    @Override
    public int getCount() {
        return userList.size();
    }

    /**
     * This method retrieves the event object from a position in the adapter
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return the event object based on the position
     */
    @Override
    public User getItem(int position) {
        Log.d("EVENTADAPTER", "onItemClick:" + userList.get(position).getId());
        return userList.get(position);
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
            convertView = inflater.inflate(R.layout.admin_profiles_list, parent, false);
        }

        User user = userList.get(position);

        FirestoreController fController = new FirestoreController();

        // Set event details to respective TextViews
        TextView nameTextView = convertView.findViewById(R.id.username_id);
        String temp = "UserID: " + user.getId();
        nameTextView.setText(temp);
        ImageButton deleteView = convertView.findViewById(R.id.delete_button);
        // Set delete button behavior
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList.remove(position);
                notifyDataSetChanged();
                FirestoreController fc = FirestoreController.getInstance();
                fc.deleteUserByUserID(user.getId(), AdminProfilesAdapter.this);
            }
        });

        return convertView;

    }
}
