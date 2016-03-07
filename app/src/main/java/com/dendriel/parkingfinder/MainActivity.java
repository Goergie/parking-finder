package com.dendriel.parkingfinder;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
{
    PositionManager posMan;

    TextView currLocationTxt;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currLocationTxt = (TextView) findViewById(R.id.curr_location_txt);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        posMan = new PositionManager(this, new IUpdateLocation() {
            @Override
            public Location doJob(Location loc) {
                locationReceived(loc);
                return null;
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        updateInitialLocation();
    }

    void locationReceived(Location loc)
    {
        progressBar.setVisibility(View.GONE);
        updateInitialLocation();
    }

    /**
     * Displays the current location to the user.
     */
    void updateInitialLocation()
    {
        String currLocation = "";
        if (posMan.currPosition != null) {
            currLocationTxt.setText( getResources().getString(R.string.curr_location) + "\n" + posMan.Street + ", " + posMan.District + ", " + posMan.State);
        } else {
            currLocationTxt.setText( getResources().getString(R.string.curr_location) + getResources().getString(R.string.unknown_location));
        }
    }
}
