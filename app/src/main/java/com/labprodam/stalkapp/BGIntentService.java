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

/* Parte de inserção de GPS - chama o 'locationManager', pega lat e lon; tb usa uma funcao para atualizar o gps a cada variacao

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

        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        String lat = String.format(Locale.getDefault(), "%.2f", loc.getLatitude());
        String lon = String.format(Locale.getDefault(), "%.2f", loc.getLongitude());

        // arrumar
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, (LocationListener) MainActivity);




    //@Override
    public void onLocationChanged(Location location) {
        mLat = location.getLatitude();
        mLon = location.getLongitude();
        //TextView coord = (TextView) findViewById(R.id.Coords);
        //coord.setText( "Latitude = " + mLat + ";" + "Longitude = " + mLon );
    }

*/

/* Cria conecção para a página http://labprodam.prefeitura.sp.gov.br/labfall/add

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

*/

/* criação da pagina que conecta o app ao db

import MySQLdb
@app.route("/add", methods=["POST"])
def add():
	data = request.form
	if VerifyArgs(data, "id", "type", "lat", "lon"):
		print ("Persists")
		conn = MySQLdb.connect(host="localhost", user="root", db="fall")
		cur = conn.cursor()

		sql = "INSERT INTO occurrence (id_user, type, latitude, longitude) values (%s, %s, %s, %s)"
		cur.execute(sql, (data["id"], data["type"], data["lat"], data["lon"]))

		conn.commit()
		conn.close()

	return "Ok"

 */