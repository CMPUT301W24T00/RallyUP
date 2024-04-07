package com.example.rallyup.uiReference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Attendance;

import java.util.List;

/**
 * This class contains the attendee's list array adapter
 */
public class AttendeeCheckInAdapter extends BaseAdapter {

    private Context context;
    private List<Attendance> userList;
    private LayoutInflater inflater;

    /**
     * Constructs an attendee adapter
     * @param context the context for this method
     * @param userList the list of user objects
     */
    public AttendeeCheckInAdapter(Context context, List<Attendance> userList) {
        this.context = context;
        this.userList = userList;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return userList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Custom view is created. this is the posting displayed for each book in the arraylist of books on main activity
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return returns the view for the array adapter
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.attlist_entry, parent, false);
        }

        // says where the item is at that position and returns the item at that position.
        //AttendeeStatsClass stats = getItem(position);
        Attendance user = userList.get(position);

        TextView username = convertView.findViewById(R.id.username);
        TextView att_count = convertView.findViewById(R.id.count_status);

        username.setText(user.getUserID());
        att_count.setText(String.valueOf(user.getTimesCheckedIn()));

        return convertView;
    }
}

