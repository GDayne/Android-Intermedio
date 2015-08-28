package com.example.dayne.adventuremap;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.activeandroid.query.Select;
import com.example.dayne.adventuremap.model.FPlace;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;



public class MapsActivity extends FragmentActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acelerometer;
    private int x,y,z;
    private long lastTime;
    private static final int MAX_TIME_REFRESH = 2000;

    private static final int MAX_PLACE = 5;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Button info,next;

    private int lat;
    private int lng;

    private static int indexPlace=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Parse.initialize(this, "YOUR_KEY", "CLient_ KEY");
        ParseAnalytics.trackAppOpened(getIntent());
        //ParseObject.registerSubclass(ParsePlace.class);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        info=(Button) findViewById(R.id.info);
        next=(Button)findViewById(R.id.nextplacebttn);

        sensorManager=(SensorManager) getSystemService (SENSOR_SERVICE);
        acelerometer =sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(null== acelerometer){
            finish();
        }
        lastTime=System.currentTimeMillis();

        llenarDB();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        sensorManager.registerListener(this, acelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(android.os.Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }




    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        List<FPlace> places = new ArrayList<>();
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            long now = System.currentTimeMillis();


            if (now - lastTime > MAX_TIME_REFRESH) {
                lastTime = now;

                float rawX = sensorEvent.values[0];
                float rawY = sensorEvent.values[1];
                float rawZ = sensorEvent.values[2];

                if (mMap != null) {

                    if(rawX >=3){
                        if (places.size() < MAX_PLACE - 1) {
                            indexPlace++;
                        }else{
                            indexPlace = 0;

                        }
                        LatLng latLng=new LatLng(getRandom().lat,getRandom().lng);
                        animateToPlace(latLng,getRandom().place);

                    }else{
                        if(rawX <=-3){
                            if (indexPlace > 0)
                                indexPlace--;
                            else
                                indexPlace = MAX_PLACE - 1;


                            LatLng latLng=new LatLng(getRandom().lat,getRandom().lng);
                            animateToPlace(latLng,getRandom().place);
                        }
                    }

                }
            }

        }
    }

    private void animateToPlace(LatLng latLng,String place){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void nextPlace(View view){
        LatLng latLng=new LatLng(getRandom().lat,getRandom().lng);
        animateToPlace(latLng, getRandom().place);

    }

    public MarkerOptions toMarker() {
        MarkerOptions mo = new MarkerOptions();
        mo.position(getLatLng());
        mo.title("go!");
        BitmapDescriptor b = BitmapDescriptorFactory.fromResource(R.drawable.point);
        mo.icon(b);
        return mo;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    private void llenarDB(){
        //parse
        saveUbication(-18.1363309, -65.758873,"Toro Toro");
        saveUbication(-17.3606341, -66.1812372,"villa tunari");
        saveUbication(-17.4080441, -66.1368193,"laguna alalay");
        saveUbication(-16.9717554, -65.4241418,"parque Tunari");
        saveUbication(-17.4005556, -66.1741667, "parque mariscal Santa cruz");
    }

    public void saveUbication(double lat,double lng,String place) {
        FPlace places = new FPlace();
        places.place=place;
        places.lat=lat;
        places.lng=lng;
        places.save();
    }

    public FPlace getRandom() {
        return new Select().from(FPlace.class).orderBy("RANDOM()").executeSingle();
    }
    public static List<FPlace> getAll(FPlace place) {
        return new Select()
                .from(FPlace.class)
                .where("Category = ?", place.getId())
                .orderBy("Name ASC")
                .execute();
    }

    public void parsePlace(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
        query.whereExists("lat");
        query.getFirstInBackground(new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject parseObject, ParseException e) {

            }
        });
     //String namePlace = Place.getString("");

    }




}
