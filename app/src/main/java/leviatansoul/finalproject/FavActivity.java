package leviatansoul.finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends AppCompatActivity {

    private  ListView lista;
    public static StationAdapter adapter;

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


        lista = (ListView)findViewById(R.id.favlist);


        ArrayList<Station> itemsFavStations = ExtractJson.favStationList;

        adapter = new StationAdapter(this, itemsFavStations, FavActivity.this);

        lista.setAdapter(adapter);

        //FavStorage.initFavList(FavActivity.this);
        adapter.notifyDataSetChanged();

        //DownloadWebPageTask task = new DownloadWebPageTask();
        //task.execute();

        //You can Use this method
      /* lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Log.v("long clicked","pos: " + pos);


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

                return true;
            }
        }); */


    }

    class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        private String contentType = "";

        @Override
        @SuppressWarnings("deprecation")
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                FavStorage.initFavList(FavActivity.this);
            } catch (Exception e) {
                response = e.toString();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
           // Toast.makeText(FavActivity.this, contentType, Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }

    }

}
