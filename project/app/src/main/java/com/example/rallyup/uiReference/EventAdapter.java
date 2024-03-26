package com.example.rallyup.uiReference;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rallyup.FirestoreController;
import com.example.rallyup.R;

import com.example.rallyup.firestoreObjects.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This class contains the adapter for an array of events
 */
public class EventAdapter extends BaseAdapter {
    private Context context;
    private List<Event> eventList;
    private LayoutInflater inflater;

    /**
     * Constructs an event adapter
     * @param context the context for this method
     * @param eventList the list of event objects
     */
    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * This method retrieves the count of objects inside the event list
     * @return an integer for the number of objects in the list
     */
    @Override
    public int getCount() {
        return eventList.size();
    }

    /**
     * This method retrieves the event object from a position in the adapter
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return the event object based on the position
     */
    @Override
    public Event getItem(int position) {
        Log.d("EVENTADAPTER", "onItemClick:" + eventList.get(position).getEventName());
        return eventList.get(position);
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
            convertView = inflater.inflate(R.layout.event_list, parent, false);
        }

        Event event = eventList.get(position);

        FirestoreController fController = new FirestoreController();

        // Set event details to respective TextViews
        TextView nameTextView = convertView.findViewById(R.id.event_list_name);
        TextView locationTextView = convertView.findViewById(R.id.event_list_location_name);
        TextView dateTextView = convertView.findViewById(R.id.event_list_date_name);
        ImageView posterImageView = convertView.findViewById(R.id.events_poster);

        nameTextView.setText(event.getEventName());
        locationTextView.setText(event.getEventLocation());
        String unformattedDate = event.getEventDate();
        String formattedDate = getProperDateFormatting(unformattedDate);
        String unformattedTime = event.getEventTime();
        String formattedTime = unformattedTime.substring(0,2) + ":" + unformattedTime.substring(2,4);
        String completeDateTime = formattedDate + " At " + formattedTime;
        dateTextView.setText(completeDateTime);
        fController.getPosterByEventID(event.getPosterRef(), context, posterImageView);


        return convertView;
    }

    /**
     * This method correctly formats the date
     * @param date a string for the date
     * @return the string of the correct date format
     */
    public String getProperDateFormatting(String date) {
        String year = date.substring(0,4);
        String month;
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.CANADA);
        int monthNum=(Integer.parseInt(date.substring(4,6))) - 1;
        cal.set(Calendar.MONTH,monthNum);
        month = month_date.format(cal.getTime());
        String day = date.substring(6,8);
        return month + " " + day + ", " + year;
    }
}