package com.laszlo.unum.map_test;

import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    public static final String TAG = MapsActivity.class.getSimpleName();

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval((long) (0.01 * 1000)); // 1 second, in milliseconds


        CameraUpdate pointTo = CameraUpdateFactory.newLatLng(new LatLng(52.83658,-6.923585));
        mMap.moveCamera(pointTo);
        mMap.animateCamera(pointTo);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }

    private void setUpMapIfNeeded()

    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null)
        {
            Context context = getApplicationContext();
            CharSequence text = "Active connection required";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context,text,duration);
            //toast.show();
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
            {

            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap()
    {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void handleNewLocation(Location location)
    {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        String x = getIntent().getExtras().getString("hunt");
        //Toast.makeText(getApplicationContext(), x + " is chosen", Toast.LENGTH_LONG).show();


        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        String marker = "Clue: " +  x;

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(marker);
        mMap.addMarker(options);
        CameraUpdate newLocation = CameraUpdateFactory.newLatLng(new LatLng(currentLatitude,currentLongitude));
        mMap.moveCamera(newLocation);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));

    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null)
        {
            Context context = getApplicationContext();
            CharSequence text = "Cannot get current location!";

            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context,text,duration);
            //toast.show();

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else
        {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i(TAG,"Connection Suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution())
        {
            try
            {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            }
            catch (IntentSender.SendIntentException e)
            {
                // Log the error
                e.printStackTrace();
            }
        }
        else
        {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());

            Context context = getApplicationContext();
            CharSequence text = "Active internet connection required";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context,text,duration);
            //toast.show();
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {//update location
        handleNewLocation(location);
    }
}