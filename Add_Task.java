package com.microsoft.wise.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Calendar;

public class Add_Task extends Activity {
    Database database = new Database(this);

    int year, month, day;
    Calendar calendar;
    EditText task_name;
    Button set_date, place_pick, save;
    String dateView;
    String[] sel_location = {"My Locations", "Add new"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        // Iniatialisation of widgets
        set_date = (Button) findViewById(R.id.set_date);
        place_pick = (Button) findViewById(R.id.place_pick);
        save = (Button)findViewById(R.id.save_task);
        task_name = (EditText)findViewById(R.id.task_name);
        calendar = Calendar.getInstance();

        // extracting the current date from calender object
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(day, month + 1, year);

        // date picker to  get gue date
        set_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

        // allocates location for the task
        place_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(Add_Task.this);
                adb.setItems(sel_location, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String seltd_loc = Arrays.asList(sel_location).get(which);
                        Toast.makeText(Add_Task.this, seltd_loc, Toast.LENGTH_SHORT).show();
                        if(seltd_loc.equals("My Locations")){
                            Intent intent = new Intent(Add_Task.this, My_Location.class);
                            startActivity(intent);
                        }else if(seltd_loc.equals("Add new")){
                            Intent intent = new Intent(Add_Task.this, MapsActivityNewPlace.class);
                            startActivity(intent);
                        }
                    }
                });
                AlertDialog dialog = adb.create();
                dialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getIntent().getParcelableExtra("bundle");
                LatLng latLng = bundle.getParcelable("Lat_Lng");
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                if(task_name == null){
                    task_name.requestFocus();
                    task_name.setError("Task name cannot be empty");
                }
                if(latLng == null){
                    place_pick.requestFocus();
                    place_pick.setError("select a place");
                }
                // inserting the task into task table
                boolean isInsert = database.insertTask(task_name.getText().toString(), dateView, latitude, longitude);
                if (isInsert== false) {
                    Toast.makeText(Add_Task.this, "Task Name  ALREADY EXISTS", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(Add_Task.this, To_Do_List.class);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == 999){
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            showDate(dayOfMonth, monthOfYear + 1, year);
        }
    };
    void showDate(int day, int month, int year){
        dateView = (new StringBuilder().append(day).append("/").append(month).append("/").append(year)).toString();
        set_date.setText(dateView);
        Toast.makeText(this, dateView, Toast.LENGTH_SHORT).show();
    }
}
