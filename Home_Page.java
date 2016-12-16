package com.microsoft.wise.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.Arrays;

public class Home_Page extends Activity {
    Button addBtn,ListBtn,settings, my_location;
    String[] sel_add = {"New Location", "New Task"};
    String[] sel_view = {"My Locations", "To Do List"};
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // buttons intialization
        addBtn = (Button)findViewById(R.id.add);
        ListBtn = (Button)findViewById(R.id.to_do_list);
        settings = (Button) findViewById(R.id.setting);
        my_location = (Button)findViewById(R.id.my_location);

        // create a new google client
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(LocationServices.API)
                .build();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(Home_Page.this);
                //.setTitle("Select an Option");

                adb.setItems(sel_add, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String seltd_loc = Arrays.asList(sel_add).get(which);
                        // Toast.makeText(Home_Page.this, seltd_loc, Toast.LENGTH_SHORT).show();

                        if(seltd_loc.equals("New Location")){
                            Intent intent = new Intent(Home_Page.this, Add_Place.class);
                            startActivity(intent);
                        } else if(seltd_loc.equals("New Task")){
                            Intent intent = new Intent(Home_Page.this, Add_Task.class);
                            startActivity(intent);
                        }
                    }
                });
                android.app.AlertDialog dialog = adb.create();
                dialog.show();
            }
        });

        ListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(Home_Page.this);
                //.setTitle("Select an Option");

                adb.setItems(sel_view, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String seltd_loc = Arrays.asList(sel_view).get(which);
                        // Toast.makeText(Home_Page.this, seltd_loc, Toast.LENGTH_SHORT).show();

                        if(seltd_loc.equals("My Locations")){
                            Intent intent = new Intent(Home_Page.this, My_Location.class);
                            startActivity(intent);
                        } else if(seltd_loc.equals("To Do List")){
                            Intent intent = new Intent(Home_Page.this, To_Do_List.class);
                            startActivity(intent);
                        }
                    }
                });
                android.app.AlertDialog dialog = adb.create();
                dialog.show();

            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationChecker(mGoogleApiClient, Home_Page.this);
            }
        });
    }
    public static void locationChecker(GoogleApiClient mGoogleApiClient, final Activity activity) {
        final String TAG = "connection";
        mGoogleApiClient.connect();
        Log.i(TAG, "locationChecker: here");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                Log.i(TAG, "locationChecker: here" + result.getStatus().toString());
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            Log.i(TAG, "locationChecker: here");
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
}