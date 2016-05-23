package com.example.caladbolg.speedlimit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements SensorEventListener,IBaseGpsListener, OnMapReadyCallback,LocationListener {
    private SensorManager sensorManager;
    private boolean color = false;
    private View view;
    private long lastUpdate;
    private TextView xa,ya,za,sp;
    GoogleMap googleMap;
    CameraUpdate yourLocation;
    LocationManager locationManager;
    static final LatLng TutorialsPoint = new LatLng(14.676 , 121.0437);
    double lat =0;
    double lng = 0;
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xa = (TextView)findViewById(R.id.x_axis);
        ya = (TextView)findViewById(R.id.y_axis);
        za = (TextView)findViewById(R.id.z_axis);
        sp = (TextView)findViewById(R.id.speedold);
        //view = findViewById(R.id.textView);
        //view.setBackgroundColor(Color.GREEN);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();

       locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,this);
        }
        catch(SecurityException e){
            Log.e("Error",e.toString());
        }


            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            Criteria criteria = new Criteria();


        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //Marker TP = googleMap.addMarker(new MarkerOptions().position(TutorialsPoint).title("Quezon City"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getMyLocationAddress(double lt, double lg) {

        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);

        try {

            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(lt,lg, 1);

            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

//                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
//                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
//                }
                strAddress.append(fetchedAddress.getAddressLine(0));
                sp.setText("Street: " +strAddress.toString());
                LatLng coordinate = new LatLng(lat, lng);
                yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 18);
                googleMap.animateCamera(yourLocation);
                //myAddress.setText("I am at: " +strAddress.toString());

            }

            else
                Toast.makeText(getApplicationContext(),"No location found..!", Toast.LENGTH_LONG).show();
                //myAddress.setText("No location found..!");

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
//DO WHATEVER YOU WANT WITH GOOGLEMAP
        try {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.setMyLocationEnabled(true);
            map.setTrafficEnabled(true);
            map.setIndoorEnabled(true);
            map.setBuildingsEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);

        }
        catch (SecurityException e){
            Log.e("Error","Google map exception");
        }


    }
    public void finish()
    {
        super.finish();
        System.exit(0);
    }

//    private void updateSpeed(CLocation location) {
//        // TODO Auto-generated method stub
//        float nCurrentSpeed = 0;
//
//        if(location != null)
//        {
//            location.setUseMetricunits(this.useMetricUnits());
//            nCurrentSpeed = location.getSpeed();
//        }
//
//        Formatter fmt = new Formatter(new StringBuilder());
//        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
//        String strCurrentSpeed = fmt.toString();
//        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');
//
//        String strUnits = "miles/hour";
//        if(this.useMetricUnits())
//        {
//            strUnits = "km/hr";
//        }
//
//        TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
//        txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
//    }

//    private boolean useMetricUnits() {
//        // TODO Auto-generated method stub
//        CheckBox chkUseMetricUnits = (CheckBox) this.findViewById(R.id.chkMetricUnits);
//        return chkUseMetricUnits.isChecked();
//    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if(location != null)
        {
            float nCurrentSpeed = 0;
            Formatter fmt = new Formatter(new StringBuilder());
            fmt.format(Locale.US, "%5.1f", nCurrentSpeed);

            //strCurrentSpeed = strCurrentSpeed.replace(' ', '0')+"km/hour";
            nCurrentSpeed = 3.6f*location.getSpeed();
            String strCurrentSpeed = fmt.toString();
            String  speed = Float.toString(3.6f*location.getSpeed());
            TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
            txtCurrentSpeed.setText("Speed:"+ speed+"km/hr");

            lng = location.getLongitude();
            lat = location.getLatitude();
            if (nCurrentSpeed > 10) {
                Toast.makeText(getApplicationContext(),"You are above the speed limit please slow down", Toast.LENGTH_SHORT).show();
            }

            getMyLocationAddress(lat,lng);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsStatusChanged(int event) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }


    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        xa.setText("X: "+Float.toString(x));
        ya.setText("Y: "+Float.toString(y));
        za.setText("Z: "+Float.toString(z));

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;

            if(Math.abs(z) > 5)
            Toast.makeText(this, "Sudden acceleration/deceleration. Please drive carefully", Toast.LENGTH_SHORT)
                    .show();
            /*Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT)
                    .show();
            if (color) {
                view.setBackgroundColor(Color.GREEN);
            } else {
                view.setBackgroundColor(Color.RED);
            }
            color = !color;
            */
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
