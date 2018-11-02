package leviatansoul.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends AppCompatActivity {

    private TextView mTextMessage;
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

                    //Intent For Navigating to FavActivity
                    Intent a = new Intent(FavActivity.this,FavActivity.class);
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
        setContentView(R.layout.activity_fav);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //mTextMessage = (TextView) findViewById(R.id.message);

        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute();


        lista = (ListView)findViewById(R.id.favlist);


        ArrayList<Station> itemsFavStations = ExtractJson.favStationList;

        adapter = new StationAdapter(this, itemsFavStations);

        lista.setAdapter(adapter);
    }


    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        private String contentType = "";

        @Override
        @SuppressWarnings( "deprecation" )
        protected String doInBackground(String... urls) {
            String response = "";

            try {
                ExtractJson.fillStationList();
                FavStorage.initFavList( FavActivity.this);
            } catch (Exception e) {
                response = e.toString();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(FavActivity.this, contentType, Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
    }
}
