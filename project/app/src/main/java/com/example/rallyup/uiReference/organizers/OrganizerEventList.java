package com.example.rallyup.uiReference.organizers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rallyup.R;
import com.example.rallyup.uiReference.ListAdapter;

import java.util.ArrayList;

public class OrganizerEventList extends AppCompatActivity {

    // previous ui trial
    ListView listView;
    ArrayList<Integer> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui_reference);
        listView = findViewById(R.id.events_list);

        arrayList.add(R.drawable.poster1);
        arrayList.add(R.drawable.poster2);
        arrayList.add(R.drawable.poster1);
        arrayList.add(R.drawable.poster2);
        arrayList.add(R.drawable.poster1);
        arrayList.add(R.drawable.poster2);


        ListAdapter listAdapter = new ListAdapter(OrganizerEventList.this, arrayList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Integer poster = (Integer) adapterView.getItemAtPosition(i);

                Intent appInfo = new Intent(getBaseContext(), OrganizerEventDetailsActivity.class);
//                appInfo.putExtra("poster", poster);
                startActivity(appInfo);
            }
        });
    }
}