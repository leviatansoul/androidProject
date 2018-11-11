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

    private ClusterManager<MyItem> clusterManager;

    private GoogleMap mMap;

    private FloatingActionButton refresh;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        /**Navigation panel**/
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;

                case R.id.navigation_dashboard:

                    //Intent For Navigating to FavActivity
                    Intent a = new Intent(MapsActivity.this, FavActivity.class);
                    startActivity(a);

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

        refresh = findViewById(R.id.refresh);

        checkLocationPermissionBT();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //To find the best provider
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
        //get the best last location
        myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MapsActivity.refreshWebPageTask task = new MapsActivity.refreshWebPageTask();
                mMap.clear();
                refresh.setVisibility(View.GONE);
                task.execute();
            }
        });
    }

    /**Manipulates the map once available. This callback is triggered when the map is ready to be used.**/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //InfoWindowAdapter display a customize infoWindow
        mMap.setInfoWindowAdapter(new InfoWindowAdapter(this));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                int res = 0;
                for (int i = 0; i < ExtractJson.stationList.size(); i++) {

                    //search for the station, looking in the list which one match with the title of the marker that has been click
                    if (marker.getTitle().equals(Integer.toString(ExtractJson.stationList.get(i).getId()))) {
                        res = Integer.parseInt(marker.getTitle());
                    }
                }

                Log.d("MAP LISTETNER " + marker.getTitle() + "  id  " + marker.getId(), Integer.toString(res));

                if (FavStorage.exists(Integer.toString(res), MapsActivity.this)) {
                    Toast.makeText(MapsActivity.this, "Ya es una estación favorita", Toast.LENGTH_SHORT).show();

                } else {
                    FavStorage.insertFav(Integer.toString(res), MapsActivity.this);
                    Toast.makeText(MapsActivity.this, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
                }
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

        //Actual location
        LatLng ubicLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

        //place the camera in the user location
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ubicLatLng, 17);
        mMap.animateCamera(cameraUpdate);

        setUpClusterer();

        clusterManager.clearItems();

        // Add cluster items (markers) to the cluster manager.
        for (int i = 0; i < ExtractJson.stationList.size(); i++) {

            MyItem item = new MyItem(ExtractJson.stationList.get(i).getLatitude(), ExtractJson.stationList.get(i).getLongitude(), Integer.toString(ExtractJson.stationList.get(i).getId()),
                    "Bicis disponibles: " + ExtractJson.stationList.get(i).getDock_bikes(), ExtractJson.stationList.get(i).getDock_bikes());
            clusterManager.addItem(item);
        }

        //Force a re-cluster.
        clusterManager.cluster();

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    /**check Location Permission and ask for it if the user has not allow it yet**/
    private void checkLocationPermissionBT() {

        //If Android version is M (6.0 API 23) or newer, check if it has Location permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1222);
            }
        }
    }

    /**Set some cluster configuration**/
    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        clusterManager = new ClusterManager<MyItem>(this, mMap);

        clusterManager.setRenderer(new MyClusterRenderer(this, mMap, clusterManager));

        // Point the map's listeners at the listeners implemented by the cluster manager.
        mMap.setOnCameraIdleListener(clusterManager);
        mMap.setOnMarkerClickListener(clusterManager);

    }

    /** Custom ClusterRender class for display the markers in different colors depending of the bicycle available in each station**/
    public class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {

        public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {

            if (item.getDock_bikes() > 10) {
                BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                markerOptions.icon(markerDescriptor);
            } else if (item.getDock_bikes() < 3) {
                BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                markerOptions.icon(markerDescriptor);
            } else {
                BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                markerOptions.icon(markerDescriptor);
            }
        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }
    }

    /**Refresh the data, downloading again the information from the web**/
    private class refreshWebPageTask extends AsyncTask<String, Integer, String> {

        @Override
        @SuppressWarnings("deprecation")
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

            // Add cluster items (markers) to the cluster manager.
            for (int i = 0; i < ExtractJson.stationList.size(); i++) {

                MyItem item = new MyItem(ExtractJson.stationList.get(i).getLatitude(), ExtractJson.stationList.get(i).getLongitude(), Integer.toString(ExtractJson.stationList.get(i).getId()),
                        "Bicis disponibles: " + ExtractJson.stationList.get(i).getDock_bikes(), ExtractJson.stationList.get(i).getDock_bikes());
                clusterManager.addItem(item);
            }

            //Force a re-cluster.
            clusterManager.cluster();
            refresh.setVisibility(View.VISIBLE);
        }
    }
}