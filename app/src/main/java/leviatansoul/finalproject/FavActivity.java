package leviatansoul.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends AppCompatActivity implements View.OnClickListener {

    private  ListView lista;
    public static StationAdapter adapter;

    Button delete;

    FavStorage fav = new FavStorage();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    //Intent For Navigating to MapsActivity
                    Intent i = new Intent(FavActivity.this,MapsActivity.class);
                    startActivity(i);

                    return true;
                case R.id.navigation_dashboard:


                    return true;
                case R.id.navigation_notifications:

                    //Intent For Navigating to StationActivity
                    Intent a = new Intent(FavActivity.this,StationActivity.class);
                    startActivity(a);

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(FavActivity.this);

        lista = (ListView)findViewById(R.id.favlist);


        ArrayList<Station> itemsFavStations = ExtractJson.favStationList;

        adapter = new StationAdapter(this, itemsFavStations);

        lista.setAdapter(adapter);


        lista.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Pulsación larga

                AlertDialog.Builder builder = new AlertDialog.Builder(FavActivity.this);
                // Add the buttons
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                builder.setMessage("¿Borrar?");


                // Create the AlertDialog
                AlertDialog dialog = builder.create();

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        fav.deleteFav(Integer.toString(ExtractJson.stationList.get(MapsActivity.station).getId()), FavActivity.this);

        Toast.makeText(this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
    }

}
