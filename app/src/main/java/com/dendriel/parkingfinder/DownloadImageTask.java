package com.dendriel.parkingfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Vitor Rozsa on 08/03/2016.
 */
class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
{
    IMapImageResponse mapResponseCb;

    public DownloadImageTask(IMapImageResponse _mapResponseCb)
    {
        mapResponseCb = _mapResponseCb;
    }

    protected Bitmap doInBackground(String... urls)
    {
        String urlDisplay = urls[0];
        Bitmap mIcon11 = null;

        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result)
    {
        mapResponseCb.handleMapImageResponse(result);
    }
}