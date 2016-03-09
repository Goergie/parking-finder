package com.dendriel.parkingfinder;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;


public class MainActivity extends AppCompatActivity
{
    PositionManager posMan;
    MapManager mapMan;

    TextView currLocationTxt;
    ProgressBar progressBar;
    ImageView bgImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currLocationTxt = (TextView) findViewById(R.id.curr_location_txt);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        bgImage = (ImageView) findViewById(R.id.bg_image);

        posMan = new PositionManager(this, new IUpdateLocation() {
            @Override
            public void doJob(Location loc) {
                locationReceived(loc);
            }
        });

        mapMan = new MapManager(this);

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        updateInitialLocation();
    }

    /**
     * The location data was received.
     * @param loc The location data.
     */
    void locationReceived(Location loc)
    {
        progressBar.setVisibility(View.GONE);
        updateInitialLocation();
    }

    /**
     * The location's map image was received.
     * @param locMap The location's map image
     */
    void locationMapReceived(Bitmap locMap)
    {
        bgImage.setImageBitmap(locMap);
    }

    /**
     * Displays the current location to the user.
     */
    void updateInitialLocation()
    {
        String currLocation = "";
        if (posMan.currPosition != null) {
            currLocationTxt.setText(getResources().getString(R.string.curr_location) + "\n" + posMan.Street + ", " + posMan.District + ", " + posMan.State);

            mapMan.createMapForCurrentLocation(posMan.currPosition, new IMapImageResponse() {
                @Override
                public void handleMapImageResponse(Bitmap mapBmp) { locationMapReceived(mapBmp); }
            });

        } else {
            currLocationTxt.setText(getResources().getString(R.string.curr_location) + getResources().getString(R.string.unknown_location));
        }
    }
}
