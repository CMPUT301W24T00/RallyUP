package com.example.rallyup.uiReference.testingClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rallyup.R;

import java.util.ArrayList;

/**
 * This class contains the attendee's list array adapter
 */
public class AttListArrayAdapter extends ArrayAdapter<AttendeeStatsClass> {

    /**
     * Constructs an attendee list array adapter based on a given context and the stats of an attendee
     * @param context the context for this method
     * @param attStats the array of stats for an attendee
     */
    public AttListArrayAdapter(Context context, ArrayList<AttendeeStatsClass> attStats) {
        super(context, 0, attStats);
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
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.attlist_entry, parent, false);
        } else {
            view = convertView;
        }
        // says where the item is at that position and returns the item at that position.
        AttendeeStatsClass stats = super.getItem(position);

        TextView username = view.findViewById(R.id.username);
        TextView att_count = view.findViewById(R.id.count_status);

        username.setText(stats.getAttName());
        att_count.setText(stats.getCheckInCount().toString());

        return view;
    }
}

