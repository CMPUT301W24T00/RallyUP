package com.example.ui_test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Integer> arrayList;

    LayoutInflater layoutInflater;
    public ListAdapter(Context context, ArrayList<Integer> arrayList){
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = layoutInflater.inflate(R.layout.event_list,viewGroup,false);
        ImageView imageView = view.findViewById(R.id.events);
        imageView.setImageResource(arrayList.get(i));
        return view;
    }
}
