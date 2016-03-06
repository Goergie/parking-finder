package com.dendriel.parkingfinder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Vitor Rozsa on 03/03/2016.
 */
public class PositionManager implements LocationListener
{
    final int minUpdateTimeInMs = 5000;
    final int minUpdateDistanceInMeters = 10;

    Context ctx;
    Location currPosition;
    boolean providerAvailable;

    LocationManager lm;
    IUpdateLocation updateLocationCallback;

    public PositionManager(Context context, IUpdateLocation _updateLocationCallback)
    {
        ctx = context;
        currPosition = null;
        providerAvailable = false;
        updateLocationCallback = _updateLocationCallback;

        lm = (LocationManager) ctx.getSystemService(ctx.LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minUpdateTimeInMs, minUpdateDistanceInMeters, this);
    }

    /**
     * Get the current position (Location).
     * @return The current position.
     */
    Location getCurrPosition()
    {
        return currPosition;
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String arg0)
    {
        providerAvailable = true;
        Toast.makeText(ctx, R.string.app_name + " - The GPS was turned on.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String arg0)
    {
        providerAvailable = false;
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        ctx.startActivity(intent);
        Toast.makeText(ctx, R.string.app_name + "Please, enable the GPS to get this application working.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location arg0)
    {
        currPosition = arg0;
        updateLocationCallback.doJob(arg0);
        getLocationInfo();
    }

    public void getLocationInfo()
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(ctx);

        String url = "http://maps.google.com/maps/api/geocode/json?latlng="+currPosition.getLongitude()+","+currPosition.getLatitude()+"&sensor=true";
        System.out.println("Request string is: " + url);

    // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {
                            System.out.println("Response is null!");
                        } else {
                            System.out.println("Response received! " + response);
                            // Display the first 500 characters of the response string.
                        }

                        JSONObject jsonObject = new JSONObject();
                        try {
                            StringBuilder stringBuilder = new StringBuilder();
                            jsonObject = new JSONObject(response);
                            System.out.println(jsonObject.get("results"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Failed to send request");
            }
        });

    // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
