package com.example.rallyup.uiReference;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rallyup.R;
import com.example.rallyup.firestoreObjects.Notification;
import com.example.rallyup.notification.NotificationObject;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {
    private Context context;
    private List<Notification> notificationList;
    private LayoutInflater inflater;

    public NotificationAdapter(Context context, List<Notification> noteList ) {
        this.context = context;
        this.notificationList = noteList;
        this.inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return notificationList.size();
    }

    @Override
    public Object getItem(int i) {
        return notificationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            view = inflater.inflate(R.layout.attendee_announcement_content,viewGroup,false);
        }

        Notification notification = notificationList.get(i);
        TextView notificationTitle = view.findViewById(R.id.title_notification);
        notificationTitle.setText(notification.getTitle());

        // put firestore stuff here


        return view;
    }

}


