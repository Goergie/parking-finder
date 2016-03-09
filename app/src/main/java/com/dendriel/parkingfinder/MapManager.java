package com.dendriel.parkingfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Vitor Rozsa on 07/03/2016.
 */
public class MapManager
{
    final String staticMapServiceUrl = "https://maps.googleapis.com/maps/api/staticmap?";
    final String centerTag = "center=";
    final String sizeTag = "size=";
    final int defaultWidth = 512;
    final int defaultheight = 512;
    final String zoomTag = "zoom=";
    final int defaultZoom = 15;
    final String mapTypeTag = "maptype=";
    final String defaultMapType = "roadmap";
    final String markerTag = "markers=";
    final String defaultMarkerColor = "color:blue";
    final String defaultMarkerLabel = "label:C";

    Context ctx;

    public MapManager(Context context)
    {
        ctx = context;
    }

    /**
     * Create a static map  for the current location of the user.
     * @param currLocation
     * @return A map for the current location.
     */
    String createMapForCurrentLocation(Location currLocation, IMapImageResponse mapRequestCb)
    {
        final String url = buildUrl(
                currLocation.getLatitude(), currLocation.getLongitude(),
                defaultWidth, defaultheight,
                defaultZoom);

        // Schedule downloading task.
        new DownloadImageTask(mapRequestCb).execute(url);

        return url;
    }

    /**
     * Build the request URL for a static google map.
     * @param lat
     * @param lng
     * @param w
     * @param h
     * @param zoom
     * @return
     */
    String buildUrl(double lat, double lng, int w, int h, int zoom)
    {
        String url = staticMapServiceUrl;

        // Set latitude and longitude values.
        url += centerTag + lng + "," + lat + "&";
        // Set map width and weight.
        url += sizeTag + w + "x" + h + "&";
        // Set zoom value.
        url += zoomTag + zoom  + "&";
        // Set the map type.
        url += mapTypeTag + defaultMapType + "&";
        // Set the marker.
        url += markerTag + defaultMarkerColor + "|" + defaultMarkerLabel + "|" + lng + "," + lat;

        return url;
    }
}
