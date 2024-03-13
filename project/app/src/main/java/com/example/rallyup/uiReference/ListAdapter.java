package com.example.rallyup.uiReference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.rallyup.R;

import java.util.ArrayList;

/**
 * This class contains a list adapter for an integer array list
 */
public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Integer> arrayList;
    LayoutInflater layoutInflater;

    /**
     * Constructs a list adapter based on a given context and integer array list
     * @param context the context for this method
     * @param arrayList an integer aarray list
     */
    public ListAdapter(Context context, ArrayList<Integer> arrayList){
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * This method returns the count of ojbects inside the list
     * @return the size of the list
     */
    @Override
    public int getCount() {
        return arrayList.size();
    }

    /**
     * This method gets the item in a list
     * @param i Position of the item whose data we want within the adapter's
     * data set.
     * @return the object we want to retrieve
     */
    @Override
    public Object getItem(int i) {
        return i;
    }

    /**
     * this method gets the id of an item in the list
     * @param i The position of the item within the adapter's data set whose row id we want.
     * @return the integer id of an item
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * This method  retrives the view for the event list
     * @param i The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param viewGroup The parent that this view will eventually be attached to
     * @return the event list view
     */
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.event_list,viewGroup,false);
        ImageView imageView = view.findViewById(R.id.events_poster);
        imageView.setImageResource(arrayList.get(i));
        return view;
    }
}
