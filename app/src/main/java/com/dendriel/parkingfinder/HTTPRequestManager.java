package com.dendriel.parkingfinder;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


/**
 * Created by Vitor Rozsa on 07/03/2016.
 *
 * Handles all HTTP requests and return the responses through callbacks.
 */
public class HTTPRequestManager
{
    private static HTTPRequestManager inst = null;

    Context ctx;
    RequestQueue reqQueue;

    protected HTTPRequestManager(Context context)
    {
        ctx = context;
        reqQueue = Volley.newRequestQueue(ctx);
    }

    /**
     * Get the instance of the HTTP Request Manager (or first create if needed).
     * @param context
     * @return The instance of the HTTP Request Manager.
     */
    public static HTTPRequestManager getInstance(Context context) {
        if(inst == null) {
            inst = new HTTPRequestManager(context);
        }
        return inst;
    }

    /**
     * Add a new HTTP request into the request's queue.
     * @param url The URL to be used in the HTTP request.
     * @param responseCb The callback that will receive the HTTP response.
     */
    public void addRequest(String url, final IHTTPResponse responseCb)
    {
        System.out.println("New request: " + url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) { responseCb.handleHTTPResponse(response); }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) { System.out.println("Failed to send request"); }
                });

        // Add the request to the RequestQueue.
        reqQueue.add(stringRequest);
    }
}
