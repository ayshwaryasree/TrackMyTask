package com.microsoft.wise.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;



class ToDoList_Adapter extends BaseAdapter {
    int year, month, day;
    Calendar calendar;
    String dateView;
    private ToDoList_Adapter adapter;
    To_Do_List to_do_list = new To_Do_List();
    ArrayList<String> result;
    Context context;
    Database database;
    ArrayList<String> numbers;
    static final int date_id = 0;
    private static LayoutInflater inflater=null;
    public ToDoList_Adapter(To_Do_List to_do_list, ArrayList<String> tasks) {
        // TODO Auto-generated constructor stub
        result=tasks;
        context= to_do_list;
        database = new Database(context);
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(day, month + 1, year);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        Button del, edit;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_adapter1, null);
        holder.tv=(TextView) rowView.findViewById(R.id.lw1);
        holder.del=(Button) rowView.findViewById(R.id.del);
        holder.edit=(Button) rowView.findViewById(R.id.edit);
        holder.tv.setText(result.get(position));
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("task_name = ",result.get(position));

                database.del_Task(result.get(position));
                Intent in = new Intent(context, To_Do_List.class);
                context.startActivity(in);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("Date Picker", "shown");
                ((Activity)ToDoList_Adapter.this.context).showDialog(999);
                LatLng latLng = database.getLatLngTask(result.get(position));
                Double lat = latLng.latitude;
                Double logn = latLng.longitude;
                Log.i("updated date : ", dateView);
                boolean isUpdate = database.UpdateTask(result.get(position),dateView,lat, logn );
                if(isUpdate)
                    Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context, "Sorry , not updated", Toast.LENGTH_LONG).show();
                Intent in = new Intent(context, To_Do_List.class);
                context.startActivity(in);



            }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result.get(position), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

    protected Dialog onCreateDialog(int id) {
        if(id == 999){
            return new DatePickerDialog(context, myDateListener, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(dayOfMonth, monthOfYear + 1, year);
        }
    };
    void showDate(int year, int month, int day){
        dateView = (new StringBuilder().append(year).append("/").append(month).append("/").append(day)).toString();
        Toast.makeText(context, dateView, Toast.LENGTH_SHORT).show();
    }

}