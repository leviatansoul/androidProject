package leviatansoul.finalproject;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter, View.OnClickListener {

    private View view;

    private int station = 1;

    private FragmentActivity myContext;

    public InfoWindowAdapter(FragmentActivity aContext) {
        this.myContext = aContext;

        LayoutInflater inflater = (LayoutInflater) myContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.info_window, null);
    }

    @Override
    public View getInfoContents(Marker marker) {

        if (marker != null && marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
            marker.showInfoWindow();
        }
        return null;
    }


    /**Display the information of the station when the user click on the marker**/
    @Override
    public View getInfoWindow(final Marker marker) {

        if (marker.getTitle() == null) {
            return null;
        }
        TextView name = view.findViewById(R.id.name);
        TextView address = view.findViewById(R.id.address);
        TextView bicis = view.findViewById(R.id.bicis);
        TextView espacios = view.findViewById(R.id.espacios);
        TextView noavailable = view.findViewById(R.id.noavailable);
        TextView number = view.findViewById(R.id.station);

        view.setClickable(true);

        //find the station 
        for (int i = 0; i < ExtractJson.stationList.size(); i++) {

            if (marker.getTitle().equals(Integer.toString(ExtractJson.stationList.get(i).getId()))) {
                station = i;
            }
        }

        name.setText(ExtractJson.stationList.get(station).getName());
        address.setText(ExtractJson.stationList.get(station).getAddress());
        bicis.setText(Integer.toString(ExtractJson.stationList.get(station).getDock_bikes()));
        espacios.setText(Integer.toString(ExtractJson.stationList.get(station).getFree_bases()));
        noavailable.setText(Integer.toString(ExtractJson.stationList.get(station).getNo_available()));
        number.setText(ExtractJson.stationList.get(station).getNumber());

        view.setOnClickListener(InfoWindowAdapter.this);

        return view;

    }

    // This method is called when any of the activity's view components is clicked.
    @Override
    public void onClick(View view) {
        if (view != null) {

        }
    }
}

