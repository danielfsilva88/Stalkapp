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
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class BGIntentService extends IntentService {

    private static final String TAG = "com.labprodam.stalkapp";
    private double mLat, mLon;
    private Context mContext;

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

        // get this context to be used in functions
        mContext = this.getApplicationContext();
        // get appUser ID
        String id = Installation.id(this);


        SensorManager SM = ( SensorManager ) getSystemService( Context.SENSOR_SERVICE );
        List<Sensor> deviceSensors = SM.getSensorList(Sensor.TYPE_ALL);

    }

    // create in first use and maintain an userID
    public static class Installation {
        private static String sID = null;
        private static final String INSTALLATION = "INSTALLATION";

        public synchronized static String id(Context context) {
            if (sID == null) {
                File installation = new File(context.getFilesDir(), INSTALLATION);
                try {
                    if (!installation.exists())
                        writeInstallationFile(installation);
                    sID = readInstallationFile(installation);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return sID;
        }

        private static String readInstallationFile(File installation) throws IOException {
            RandomAccessFile f = new RandomAccessFile(installation, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            f.close();
            return new String(bytes);
        }

        // http://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it-in-java
        private static void writeInstallationFile(File installation) throws IOException {
            FileOutputStream out = new FileOutputStream(installation);
            String id = UUID.randomUUID().toString();
            out.write(id.getBytes());
            out.close();
        }
    }

    //@Override
    public void onLocationChanged(Location location) {
        mLat = location.getLatitude();
        mLon = location.getLongitude();
    }

    // Create a timer task to get web access
    private class MonitorTimer extends TimerTask {
        @Override
        public void run() {
            MainActivity.DeadTimer mDeadTimer = new MainActivity.DeadTimer();
            new Timer().schedule(mDeadTimer, 5000);
        }
    }

    // Creates a connection to http://labprodam.prefeitura.sp.gov.br/labfall/add
    private class DeadTimer extends TimerTask {
        @Override
        public void run() {

            final ConnectivityManager connectivityManager = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE));

            if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {

                try {
                    URL url = new URL("http://10.65.30.11:5000/add");
                    HttpURLConnection con = (HttpURLConnection) (url.openConnection());
                    con.setRequestMethod("POST");
                    con.setRequestProperty("USER-AGENT" , "Mozilla/5.0");
                    con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    String data = "id=0&type=Alert&lat=" + mLat + "&lon=" + mLon;

                    DataOutputStream os = new DataOutputStream(con.getOutputStream());
                    os.writeBytes(data);
                    os.flush();
                    os.close();

                    Log.e("FallResponse", "" + con.getResponseCode());

                    con.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
