package leviatansoul.finalproject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StationAdapter extends ArrayAdapter<Station> {

    private ArrayList<Station> items;
    private Context mContext;
    private Activity mActivity;

    StationAdapter(Context context, ArrayList<Station> stations, Activity activity) {
        super(context, 0, stations); // Call to super class constructor
        items = stations;
        mContext = context;
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
// This approach can be improved for performance
        if (newView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            newView = inflater.inflate(R.layout.fav_list_item, parent, false);
        }
//-----
        TextView name = (TextView) newView.findViewById(R.id.name);
        TextView address = (TextView) newView.findViewById(R.id.address);
        TextView bicis = (TextView) newView.findViewById(R.id.bicis);
        TextView espacios = (TextView) newView.findViewById(R.id.espacios);
        TextView noavailable = (TextView) newView.findViewById(R.id.noavailable);
        TextView number = (TextView) newView.findViewById(R.id.station);
        Button delete = (Button) newView.findViewById(R.id.delete);
        //  ImageView imageView = (ImageView) newView.findViewById(R.id.imgCountry);
        final Station station = items.get(position);
        name.setText(station.getName());
        address.setText(station.getAddress());
        bicis.setText("" + station.getDock_bikes());
        espacios.setText("" + station.getFree_bases());
        noavailable.setText("" + station.getNo_available());
        number.setText(station.getNumber());
        //  imageView.setImageResource(country.getImageResource());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("BORRRAR numero ", Integer.toString(station.getId()));
                FavStorage.deleteFav(Integer.toString(station.getId()), mActivity);
                FavActivity.adapter.notifyDataSetChanged();
            }
        });

        return newView;
    }
}
