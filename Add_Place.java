package com.microsoft.wise.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLng;

public class Add_Place extends Activity {
    EditText Place_name;
    Button map, save;
    GoogleApiClient mGoogleApiClient;
    Database database = new Database(Add_Place.this);
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_place);

        // initialistion of the widgets
        Place_name = (EditText)findViewById(R.id.task_name);
        map = (Button)findViewById(R.id.map);
        save = (Button)findViewById(R.id.save_place);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // locationChecker(mGoogleApiClient, Add_Place.this);
                Intent intent = new Intent(Add_Place.this, Maps_Place_Activity.class);
                Toast.makeText(Add_Place.this, "Maps", Toast.LENGTH_SHORT);
                Log.i("Place_name",String.valueOf(Place_name.getText()) );
                intent.putExtra("Place_name",(Place_name.getText()).toString());
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getIntent().getParcelableExtra("bundle");
                latLng = bundle.getParcelable("Lat_Lng");
                if(latLng == null){
                    map.requestFocus();
                    map.setError("no Map selected");
                }if(Place_name.getText().toString() == null){
                    Place_name.requestFocus();
                    Place_name.setError("Field Cannot be empty ");
                }
                Log.i("Add_place lat lng", latLng.toString());
                Log.i("Add_place place name ", Place_name.getText().toString());

                // extracting latitude and longitude from LatLng object
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;

                //Inserting into the locations table
                boolean isInsert = database.insert_location(Place_name.getText().toString(), latitude, longitude);

                // verification for successful insertion
                if (isInsert== false) {
                    Toast.makeText(Add_Place.this, "location name  ALREADY EXISTS", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(Add_Place.this, My_Location.class);
                    startActivity(intent);
                }
            }
        });
    }

    // permission request to access GPS
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
