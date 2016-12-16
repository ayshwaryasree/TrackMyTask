package com.microsoft.wise.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ayshu on 05-Dec-16.
 * hgctgvhyv
 */
public class Database extends SQLiteOpenHelper {
    private static String TAG = "info";
    public Database(Context context) {
        super(context, "Database.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table task" + "( task_name TEXT PRIMARY KEY, due_date TEXT, latitude DOUBLE, longitude DOUBLE)");
        //date fromat dd-mm-yyyy
        Log.i(TAG, "onCreate: table tasks created");
        db.execSQL("create table locations" + "(location_name TEXT PRIMARY KEY, latitude DOUBLE, longitude DOUBLE)");
        Log.i(TAG, "onCreate: table location created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists task");
        db.execSQL("drop table if exists locations");
        onCreate(db);
    }

    // Inserting the TASK into table

    public boolean insertTask(String task_name, String due_date, Double latitude , Double longitude ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(task_name == null || due_date == null || latitude == null || longitude == null)
            return false;
         else
        {

            contentValues.put("task_name", task_name);
            contentValues.put("due_date", due_date);
            contentValues.put("latitude",latitude);
            contentValues.put("longitude",longitude);


            Log.i(TAG, "insertValues: inserting");
            Log.i(TAG, "inserValues: " + task_name + " date : " + due_date + "latitude: " + latitude + "longitude : "+ longitude);


            long result = db.insert("task", null, contentValues);

            Log.i(TAG, "insertValues: inserted");
            db.close();
            if (result == -1)
                return false;
            else
                return true;

         }

    }

    // to delete a task
    public void del_Task(String task_name) {
        Log.i("task_name db = ", task_name);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from  task where task_name = ?", new String[] {"" +task_name});
        db.close();

    }

    // to get tasks from table
    public  Cursor getTask(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> tasks = new ArrayList<>();
        String query = "select task_name, due_date from task";
        Cursor cursor = db.rawQuery(query, null);
        Log.i(TAG, "getTasks: " + cursor.getCount());

        return  cursor;

    }

    // updating the task

    public boolean UpdateTask(String task_name, String due_date, Double latitude, Double longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("task_name",task_name );
        contentValues.put("due_date",due_date );
        contentValues.put("latitude",latitude );
        contentValues.put("longitude",longitude );
        db.update("task", contentValues, " task_name= ?",new String[] {task_name });
        return true;

    }


    // to get date whether to know that duration of the task
    public String getDate(String day){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        if(day.equals("today")){
            Log.i(TAG, "onCreate: today " +  dateFormat.format(date).toString());
           return dateFormat.format(date).toString();
        }

        return null;
    }

    // get latLng of a task
    LatLng getLatLngTask(String Task_name){
        SQLiteDatabase db = this.getReadableDatabase();
        LatLng latLng;
        if(IsTask(Task_name)){
            double lat = getLatitudeT(Task_name);
            double log = getLongitudeT(Task_name);
            latLng = new LatLng(lat, log);

        }else{
            latLng = new LatLng(0, 0);
        }
        return latLng;
    }

    //Check whether the task exists or not
    boolean IsTask(String Task_name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * from task where task_name = ? ",new String[] {"" +Task_name} );
        boolean isAvail = Boolean.getBoolean(String.valueOf(cursor.getCount()));
        if(isAvail == false)
            return  false;
        else
            return  true;
    }

    // to get latitude from Locations

    Double getLatitudeT(String Task_name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT latitude from task where task_name = ? ",new String[] {"" +Task_name} );
        double lat = cursor.getDouble(1);
        return lat;

    }

    // to get longitude from Locations

    Double getLongitudeT(String Task_name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT longitude from task where task_name = ? ",new String[] {"" +Task_name} );
        double longitude = cursor.getDouble(2);
        return longitude;

    }


    // Inserting into LOCATION TABle

    public boolean insert_location(String location_name, Double latitude , Double longitude ){
        SQLiteDatabase db = this.getWritableDatabase();
        if(location_name == null || latitude == null || longitude == null   ){
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("location_name", location_name);
        contentValues.put("latitude", latitude );
        contentValues.put("longitude", longitude );
        Log.i(TAG, "inserValues: " + location_name + "latitude: " + latitude + "longitude : "+ longitude);

        long result =  db.insert("locations", null, contentValues);
        Log.i(TAG, "insertValues_location: inserted");
        db.close();
        if(result == -1)
            return  false;
        else
            return true;

    }

    // deelete locations from the table

    public void del_Location(String loc_name) {
        Log.i("location name  db = ", loc_name);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from  locations where location_name = ?", new String[] {"" + loc_name});
        db.close();
    }

    //TO GET LOCATIONS FROM TABLE
    public Cursor getLocation(){

        SQLiteDatabase db = this.getReadableDatabase();
        //String query = "select * from location";
        ArrayList<String> list = new ArrayList<String>();
        Log.i("In getLocations()", "before query");

        Cursor cursor = db.rawQuery("SELECT location_name FROM locations", null);
        Log.i("locations count", String.valueOf(cursor.getCount()));


        return cursor;
    }

        //TO GET LATLNGS FROM LOCATIONS

    ArrayList<LatLng> getLatLngs(){
        SQLiteDatabase db = this.getReadableDatabase();
        LatLng latLng;
        ArrayList<LatLng> lngArrayList = new ArrayList<LatLng>();
        Cursor cursor = db.rawQuery("SELECT latitude, longitude FROM locations", null);
        if(cursor.moveToFirst()){
            do{
                double lat = cursor.getDouble(0);
                Log.i("lat = ", String.valueOf(lat));
                double logn = cursor.getDouble(1);
                Log.i("logn= ", String.valueOf(logn));
                latLng = new LatLng(lat, logn);
                lngArrayList.add(latLng);
            }
            while (cursor.moveToNext());
        }
        return lngArrayList;
    }

    // to get latlng for a specific location

    LatLng getLatLng(String location_name){
        SQLiteDatabase db = this.getReadableDatabase();
        LatLng latLng;
        if(IsLocation(location_name)){
            double lat = getLatitude(location_name);
            double log = getLongitude(location_name);
            latLng = new LatLng(lat, log);

        }else{
            latLng = new LatLng(0, 0);
        }
        return latLng;
    }

    // TO check if the location exists or not

    boolean IsLocation(String location_name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * from locations where location_name = ? ",new String[] {"" +location_name} );
        boolean isAvail = Boolean.getBoolean(String.valueOf(cursor.getCount()));
        if(isAvail == false)
            return  false;
        else
            return  true;
    }

        // to get latitude from Locations

    Double getLatitude(String location_name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT latitude from locations where location_name = ? ",new String[] {"" +location_name} );
        double lat = cursor.getDouble(1);
        return lat;

    }

    // to get longitude from Locations

    Double getLongitude(String location_name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT longitude from locations where location_name = ? ",new String[] {"" +location_name} );
        double longitude = cursor.getDouble(2);
        return longitude;

    }



}
