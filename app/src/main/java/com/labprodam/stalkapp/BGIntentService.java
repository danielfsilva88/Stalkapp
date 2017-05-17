package com.labprodam.stalkapp;

/**
 * Created by daniel on 18/04/17.
 * https://www.youtube.com/watch?v=-sBxmjrSn34
 */

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Locale;

public class BGIntentService extends IntentService {

    private static final String TAG = "com.labprodam.stalkapp";
    private double mLat, mLon;

    public BGIntentService() {
        super("BGIntentService");
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     *               This may be null if the service is being restarted after
     *               its process has gone away; see
     *               {@link Service#onStartCommand}
     *               for details.
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // This is what service does
        Log.i(TAG, "it's working!");



    }

}
