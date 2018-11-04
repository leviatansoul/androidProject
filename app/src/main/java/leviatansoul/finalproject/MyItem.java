package leviatansoul.finalproject;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle = null;
    private String mSnippet = null;
    private String mName = null;
    private String mNumber = null;
    private String mAddress = null;
    private int mDock_bikes = 0;
    private int mFree_bases = 0;
    private int mNo_available = 0;


    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MyItem(double lat, double lng, String title, String snippet, int bikes) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mDock_bikes = bikes;
    }

    public MyItem(double lat, double lng, String name, String address, int dock_bikes, String number, int free_bases, int no_available) {
        mPosition = new LatLng(lat, lng);
        mName = name;
        mAddress = address;
        mDock_bikes = dock_bikes;
        mNumber = number;
        mFree_bases = free_bases;
        mNo_available = no_available;
    }



    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }


    public int getDock_bikes() {
        return mDock_bikes;
    }

    public String getNumber() {
        return mNumber;
    }

    public String getAddress() {
        return mAddress;
    }

    public int getFree_bases() {
        return mFree_bases;
    }

    public int getNo_available() { return mNo_available; }

    public String getName() {
        return mName;
    }
}
