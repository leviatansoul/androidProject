package leviatansoul.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class StationActivity extends FragmentActivity implements View.OnClickListener {

    ImageButton favorites;

    FavStorage fav = new FavStorage();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    //Intent For Navigating to MapsActivity
                    Intent i = new Intent(StationActivity.this,MapsActivity.class);
                    startActivity(i);

                    return true;
                case R.id.navigation_dashboard:

                    //Intent For Navigating to FavActivity
                    Intent a = new Intent(StationActivity.this,FavActivity.class);
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
        setContentView(R.layout.activity_satation);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        TextView name = findViewById(R.id.name);
        TextView address = findViewById(R.id.address);
        TextView bicis = findViewById(R.id.bicis);
        TextView espacios = findViewById(R.id.espacios);
        TextView reservas = findViewById(R.id.reservas);
        TextView number = findViewById(R.id.station);

        favorites = (ImageButton) findViewById(R.id.favorites);

        favorites.setOnClickListener(StationActivity.this);

        name.setText(ExtractJson.stationList.get(MapsActivity.station).getName());
        address.setText(ExtractJson.stationList.get(MapsActivity.station).getAddress());
        bicis.setText(Integer.toString(ExtractJson.stationList.get(MapsActivity.station).getDock_bikes()));
        espacios.setText(Integer.toString(ExtractJson.stationList.get(MapsActivity.station).getFree_bases()));
        reservas.setText(Integer.toString(ExtractJson.stationList.get(MapsActivity.station).getNo_available()));
        number.setText(ExtractJson.stationList.get(MapsActivity.station).getNumber());


    }

    @Override
    public void onClick(View view) {

        fav.insertFav(Integer.toString(ExtractJson.stationList.get(MapsActivity.station).getId()), StationActivity.this);

        Toast.makeText(this, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
    }


}



