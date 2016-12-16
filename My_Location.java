package com.microsoft.wise.myapplication;

import android.app.Activity;
import android.content.Context;
//import android.support.v7.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class My_Location extends Activity {
    ListView lv1;
    Context context;
    Cursor cursor;
    ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
    ArrayList<String> list = new ArrayList<String>();
    Database database = new Database(My_Location.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location);
        lv1 = (ListView)findViewById(R.id.location_list);
       // Log.i("Add_place 6", "In my_location ");

        cursor = database.getLocation();
        if(cursor.getCount() > 0) {
            Log.i( "count", String.valueOf(cursor.getCount()));

            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(0));
                    Log.i("String ", list.toString());

                } while (cursor.moveToNext());
                database.close();
            }
        }



       // Toast.makeText(My_Location.this, "retrived", Toast.LENGTH_SHORT).show();
        latLngs = database.getLatLngs();
        context=this;

        lv1.setAdapter(new Location_Adapter(this, list ,latLngs ));
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String location_name  = parent.getItemAtPosition(position).toString();
                Log.i("location", location_name);
                Toast.makeText(My_Location.this, location_name,Toast.LENGTH_LONG).show();

            }

        });
    }
}
