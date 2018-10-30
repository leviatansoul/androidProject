package leviatansoul.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StationAdapter extends ArrayAdapter<Station> {

    private ArrayList<Station> items;
    private Context mContext;
    StationAdapter(Context context, ArrayList<Station> stations ) {
        super( context,0, stations ); // Call to super class constructor
        items = stations;
        mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent ) {
        View newView = convertView;
// This approach can be improved for performance
        if ( newView == null ) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            newView = inflater.inflate(R.layout.fav_list_item, parent, false);
        }
//-----
        TextView textView = (TextView) newView.findViewById(R.id.name);
      //  ImageView imageView = (ImageView) newView.findViewById(R.id.imgCountry);
        Station station = items.get(position);
        textView.setText(station.getName());
      //  imageView.setImageResource(country.getImageResource());
        return newView;
    }
}
