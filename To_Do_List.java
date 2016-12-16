package com.microsoft.wise.myapplication;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ayshu on 01-Dec-16.
 * hgctgvhyv
 */
public class To_Do_List extends Activity {
    Database db;
    Cursor cursor;
    Context context;
    private final String TAG= "info";
    ArrayList<String> tasks_today, tasks_pending, upcoming_list;
    ListView view_today, view_pending, view_upcoming;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_do_list);

        view_today = (ListView)findViewById(R.id.task_list_today);
        view_pending = (ListView)findViewById(R.id.task_list_pending);
        view_upcoming = (ListView) findViewById(R.id.task_list_upcoming);


        db = new Database(To_Do_List.this);
        context = To_Do_List.this;

        tasks_today = new ArrayList<>();
        tasks_pending = new ArrayList<>();
        upcoming_list = new ArrayList<>();

        cursor = db.getTask();
        if(cursor.getCount() > 0) {
            Log.i(TAG, "count"+ String.valueOf(cursor.getCount()));

            if (cursor.moveToFirst()) {
                do {
                    Log.i(TAG, "onCreate: db date " + cursor.getString(1));
                    Date date = new Date(cursor.getString(1));
                    if (date.before(new Date(db.getDate("today")))) {
                        tasks_today.add(cursor.getString(0));
                        Log.i(TAG, "onCreate: today " + date);

                    } else
                if (date.equals(new Date(db.getDate("today")))) {
                    tasks_pending.add(cursor.getString(0));
                    Log.i(TAG, "onCreate: pending" + date);

                    } else {
                        upcoming_list.add(cursor.getString(0));
                        Log.i(TAG, "onCreate: upcoming" + date);
                    }
                } while (cursor.moveToNext());
                db.close();
            }
        }


        view_today.setAdapter(new ToDoList_Adapter(this, tasks_pending  ));
        view_pending.setAdapter(new ToDoList_Adapter(this, tasks_today));
        view_upcoming.setAdapter(new ToDoList_Adapter(this, upcoming_list ));

    }

}
