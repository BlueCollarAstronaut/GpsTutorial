package com.example.sstewart.gpstutorial;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements LocationListener {


    private TextView latitudeView;
    private TextView longitudeView;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the controls
        latitudeView = (TextView) findViewById(R.id.latitudeValue);
        longitudeView = (TextView) findViewById(R.id.longitudeValue);


        // Initialize location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {

        boolean enabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
        if (!enabled)    {
            new AlertDialog.Builder(this)
                    .setTitle("GPS Not Entabled")
                    .setMessage("Enabling GPS will heighten this experience")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          if (which == DialogInterface.BUTTON_POSITIVE) {
                              Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                              startActivity(intent);
                          }
                        };
                    })
                    .create()
                    .show();
        }


            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);

            // Initialize the location fields:
            if (location == null) {
                longitudeView.setText(R.string.location_unavailable);
                latitudeView.setText(R.string.location_unavailable);
            } else {
                System.out.println("Provider " + provider + " has been selected.");
                onLocationChanged(location);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 100, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public  void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        longitudeView.setText(Double.toString(longitude));
        latitudeView.setText(Double.toString(latitude));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
