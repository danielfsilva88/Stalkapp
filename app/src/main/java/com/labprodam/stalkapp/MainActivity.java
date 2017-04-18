package com.labprodam.stalkapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private double mLat, mLon;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String id = Installation.id(this); // get appUser ID
        String[] info = getSensorsData();
        int nsensors = Integer.valueOf(info[3]);
        String[] infos = new String[nsensors+1];
        infos[0] = id;
        System.arraycopy(info,0,infos,1,nsensors);

        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(infos[0] + "\n" + infos[1] + "\n" + infos[2] + "\n" + infos[3] + "\n");
        /*
        mContext = this.getApplicationContext();

        // creates an instance (mSensorManager) to access sensors
        SensorManager mSensorManager = ( SensorManager ) getSystemService( Context.SENSOR_SERVICE );
        // accesses accelerometer sensor
        Sensor mAcc = mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
        // controls the interval at which sensor events are sent to your application via the onSensorChanged() callback method (SENSOR_DELAY_FASTEST has 0 ms delay)
        mSensorManager.registerListener( this, mAcc, SensorManager.SENSOR_DELAY_FASTEST );

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }

        // getting last known GPS coordinates - if coords changes, function onLocationChanged is autocalled
        ///*
        //Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //String lat = String.format(Locale.getDefault(), "%.2f", loc.getLatitude());
        //String lon = String.format(Locale.getDefault(), "%.2f", loc.getLongitude());
        //TextView coord = (TextView) findViewById(R.id.Coords);
        //coord.setText( "Latitude = " + lat + "º;\n" + "Longitude = " + lon + "º.");
        //
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        */

        // starting the background service
        Intent intentbg = new Intent(this, BGIntentService.class);
        startService(intentbg);

    }

    // Called when user taps Cell Info button
    public void nextScreen(View view){

        String id = Installation.id(this); // get appUser ID
        String[] info = getSensorsData();
        int nsensors = Integer.valueOf(info[3]);
        String[] infos = new String[nsensors+1];
        infos[0] = id;
        System.arraycopy(info,0,infos,1,nsensors);

        Intent intent_button = new Intent(this, PrintScreen.class);
        intent_button.putExtra(EXTRA_MESSAGE, infos);
        startActivity(intent_button);
    }

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

    private String[] getSensorsData() {
        // Teste para printar na tela do app o num de sensores e algumas das suas infos. Para obter outras infos:
        //https://developer.android.com/guide/topics/sensors/sensors_overview.html
        // creates an instance (mSensorManager) to access sensors
        SensorManager mSensorManager = ( SensorManager ) getSystemService( Context.SENSOR_SERVICE );
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        int nsensors = deviceSensors.size();
        String name, vendor, version;
        String[] sensorinfo = new String [nsensors+1]; //+1 to pass number of sensors

///*
        for (int i = 0; i < nsensors; i++){

            name = deviceSensors.get(i).getName();
            vendor = deviceSensors.get(i).getVendor();
            version = String.valueOf(deviceSensors.get(i).getVersion());
            sensorinfo[i] = ("" + name + "; " + vendor + "; " + version);
        }
        sensorinfo[3] = Integer.toString(nsensors);
        return sensorinfo;

    }

    //@Override
    public void onLocationChanged(Location location) {
        mLat = location.getLatitude();
        mLon = location.getLongitude();
        //TextView coord = (TextView) findViewById(R.id.Coords);
        //coord.setText( "Latitude = " + mLat + ";" + "Longitude = " + mLon );
    }

    private class MonitorTimer extends TimerTask {
        @Override
        public void run() {
            DeadTimer mDeadTimer = new DeadTimer();
            new Timer().schedule(mDeadTimer, 5000);
            //double lastResulting = resulting;
        }
    }

    // Creates a connection to http://labprodam.prefeitura.sp.gov.br/labfall/add
    private class DeadTimer extends TimerTask {
        @Override
        public void run() {

            final ConnectivityManager connectivityManager = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE));

            if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {

                try {
                    URL url = new URL("http://labprodam.prefeitura.sp.gov.br/labfall/add");
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

    // Functions created when declared "implements SensorEventListener, LocationListener"
    /**
     * Called when there is a new sensor event.  Note that "on changed"
     * is somewhat of a misnomer, as this will also be called if we have a
     * new reading from a sensor with the exact same sensor values (but a
     * newer timestamp).
     * <p>
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    /**
     * Called when the accuracy of the registered sensor has changed.  Unlike
     * onSensorChanged(), this is only called when this accuracy value changes.
     * <p>
     * <p>See the SENSOR_STATUS_* constants in
     * {@link SensorManager SensorManager} for details.
     *
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *                 {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Called when the provider status changes. This method is called when
     * a provider is unable to fetch a location or if the provider has recently
     * become available after a period of unavailability.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     * @param status   {@link LocationProvider#OUT_OF_SERVICE} if the
     *                 provider is out of service, and this is not expected to change in the
     *                 near future; {@link LocationProvider#TEMPORARILY_UNAVAILABLE} if
     *                 the provider is temporarily unavailable but is expected to be available
     *                 shortly; and {@link LocationProvider#AVAILABLE} if the
     *                 provider is currently available.
     * @param extras   an optional Bundle which will contain provider specific
     *                 status variables.
     *                 <p>
     *                 <p> A number of common key/value pairs for the extras Bundle are listed
     *                 below. Providers that use any of the keys on this list must
     *                 provide the corresponding value as described below.
     *                 <p>
     *                 <ul>
     *                 <li> satellites - the number of satellites used to derive the fix
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {

    }
}
