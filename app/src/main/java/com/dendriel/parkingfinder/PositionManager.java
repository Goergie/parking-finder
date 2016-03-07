package com.dendriel.parkingfinder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    HTTPRequestManager httpRequestMan;
    IUpdateLocation updateLocationCb;

    String Street;
    String District;
    String City;
    String State;
    String Country;

    public PositionManager(Context context, IUpdateLocation _updateLocationCb)
    {
        ctx = context;
        currPosition = null;
        providerAvailable = false;
        updateLocationCb = _updateLocationCb;
        httpRequestMan = HTTPRequestManager.getInstance(ctx);

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
        // Store the current Lat/Lng positions.
        currPosition = arg0;

        // Request the address for the received position.
        String reqUrl = "http://maps.google.com/maps/api/geocode/json?latlng="+currPosition.getLongitude()+","+currPosition.getLatitude()+"&sensor=true";
        httpRequestMan.addRequest(
                reqUrl,
                new IHTTPResponse() {
                    @Override
                    public void handleHTTPResponse(String response)
                    {
                        onAddressResponseReceived(response);
                    }
            });
    }

    /**
     * Handle the address translation response.
     *
     * @param response The response content.
     */
    void onAddressResponseReceived(String response)
    {
        if (response.length() == 0) {
            Toast.makeText(ctx, R.string.app_name + " - Could not identify the current location. Try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject fullResponse = new JSONObject();
        try {
            StringBuilder stringBuilder = new StringBuilder();
            fullResponse = new JSONObject(response);

            JSONArray addressComps = fullResponse.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");

            Street = addressComps.getJSONObject(1).getString("short_name");
            District = addressComps.getJSONObject(2).getString("short_name");
            City = addressComps.getJSONObject(3).getString("short_name");
            State = addressComps.getJSONObject(5).getString("short_name");
            Country = addressComps.getJSONObject(6).getString("short_name");

            System.out.println(Street + " - " + District + " - " + City + " - " + State + " - " + Country);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateLocationCb.doJob(currPosition);
    }
}
