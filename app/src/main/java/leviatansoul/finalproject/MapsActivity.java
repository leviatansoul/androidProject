package leviatansoul.finalproject;

import android.Manifest;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import android.location.Location;

import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private LocationManager locationManager;
    private Location myLocation;

    // Declare a variable for the cluster manager.
    private ClusterManager <MyItem> clusterManager;

    private GoogleMap mMap;

    Button favorites;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

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

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton refresh = findViewById(R.id.refresh);

        checkLocationPermissionBT();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

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
        myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MapsActivity.refreshWebPageTask task = new MapsActivity.refreshWebPageTask();
                mMap.clear();
                task.execute();

            }
        });
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

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setInfoWindowAdapter(new InfoWindowAdapter(this));


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                int res = 0;
                for (int i = 0; i < ExtractJson.stationList.size(); i++) {

                    if (marker.getTitle().equals(Integer.toString(ExtractJson.stationList.get(i).getId()))) {
                        res = i;
                    }
                }

                Log.d("MAP LISTETNER ", Integer.toString(res));
                FavStorage.insertFav(Integer.toString(res), MapsActivity.this);
                Toast.makeText(MapsActivity.this, "Agregado a favoritos", Toast.LENGTH_SHORT).show();



            }
        });


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

        //ubicacion actual
        LatLng ubicLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

        //mMap.addMarker(new MarkerOptions().position(ubicLatLng));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ubicLatLng, 17);
        mMap.animateCamera(cameraUpdate);

        setUpClusterer();

        clusterManager.clearItems();

        //ubicacion estaciones
        for(int i=0; i<ExtractJson.stationList.size(); i ++) {

            // Add cluster items (markers) to the cluster manager.
            MyItem item = new MyItem(ExtractJson.stationList.get(i).getLatitude(), ExtractJson.stationList.get(i).getLongitude(), Integer.toString(ExtractJson.stationList.get(i).getId()),
                    "Bicis disponibles: "+ ExtractJson.stationList.get(i).getDock_bikes(), ExtractJson.stationList.get(i).getDock_bikes());
            clusterManager.addItem(item);
        }

        //Force a re-cluster. You may want to call this after adding new item(s).
        clusterManager.cluster();

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
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


    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        clusterManager = new ClusterManager<MyItem>(this, mMap);

        clusterManager.setRenderer(new MyClusterRenderer(this, mMap, clusterManager));

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

    }

    public class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {

        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());

        public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {

            if(item.getDock_bikes()>10){
                BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                markerOptions.icon(markerDescriptor);
            }
            else if (item.getDock_bikes()<3){
                BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                markerOptions.icon(markerDescriptor);
            }
            else{
                BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                markerOptions.icon(markerDescriptor);
            }

        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);

        }

    }

    private class DownloadWebPageTask extends AsyncTask<String, Integer, String> {

        private String contentType = "";

        @Override
        @SuppressWarnings( "deprecation" )
        protected String doInBackground(String... urls) {
            String response = "";

            try {
                ExtractJson.fillStationList();
            } catch (Exception e) {
                response = e.toString();
            }
            return response;
        }
    }

    private class refreshWebPageTask extends AsyncTask<String, Integer, String> {

        private String contentType = "";

        @Override
        @SuppressWarnings( "deprecation" )
        protected String doInBackground(String... urls) {
            String response = "";

            try {
                ExtractJson.fillStationList();
            } catch (Exception e) {
                response = e.toString();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            setUpClusterer();

            clusterManager.clearItems();

            //ubicacion estaciones
            for(int i=0; i<ExtractJson.stationList.size(); i ++) {

                // Add cluster items (markers) to the cluster manager.
                MyItem item = new MyItem(ExtractJson.stationList.get(i).getLatitude(), ExtractJson.stationList.get(i).getLongitude(), Integer.toString(ExtractJson.stationList.get(i).getId()),
                        "Bicis disponibles: "+ ExtractJson.stationList.get(i).getDock_bikes(), ExtractJson.stationList.get(i).getDock_bikes());
                clusterManager.addItem(item);
            }

            //Force a re-cluster. You may want to call this after adding new item(s).
            clusterManager.cluster();

        }
    }
}
