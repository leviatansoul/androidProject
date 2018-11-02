package leviatansoul.finalproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;


    private LocationManager locationManager;
    private Location myLocation;


    // Declare a variable for the cluster manager.
    private ClusterManager <MyItem> clusterManager;

    private GoogleMap mMap;

    RadioGroup radGrp;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    //Intent For Navigating to MapsActivity
                    Intent i = new Intent(MapsActivity.this,MapsActivity.class);
                    startActivity(i);

                    return true;
                case R.id.navigation_dashboard:

                    //Intent For Navigating to FavActivity
                    Intent a = new Intent(MapsActivity.this,FavActivity.class);
                    startActivity(a);

                    return true;
                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        radGrp = findViewById(R.id.grupoRadioMapType);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        checkLocationPermissionBT();
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        //ubicacion actual
        /*LatLng ubicLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

        mMap.addMarker(new MarkerOptions().position(ubicLatLng));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ubicLatLng, 17);
        mMap.animateCamera(cameraUpdate);*/

        setUpClusterer();

        //ubicacion estaciones
        for(int i=0; i<ExtractJson.stationList.size(); i ++) {

            // Add cluster items (markers) to the cluster manager.
            addItems(ExtractJson.stationList.get(i).getLatitude(), ExtractJson.stationList.get(i).getLongitude());
        }



        radGrp.setOnCheckedChangeListener(new radioGroupCheckedChanged());
    }

    //permisions
    private void checkLocationPermissionBT() {
        //If Android version is M (6.0 API 23) or newer, check if it has Location permissions

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1222);
            }
        }

    }


    // Listener related to the user choosing a different map type (through the radio buttons)
    class radioGroupCheckedChanged implements RadioGroup.OnCheckedChangeListener {
        public void onCheckedChanged(RadioGroup arg0, int id) {
            switch (id) {
                case R.id.typeMap:
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case R.id.typeSatellite:
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case R.id.typeHybrid:
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
            }
        }
    }

    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        clusterManager = new ClusterManager<MyItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

    }

    private void addItems(double lat, double lng) {

            MyItem item = new MyItem(lat, lng);
            clusterManager.addItem(item);
    }
}
