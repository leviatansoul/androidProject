package leviatansoul.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FavActivity extends AppCompatActivity {

    private ListView lista;
    public static StationAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        /**Navigation panel**/
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    //Intent For Navigating to MapsActivity
                    Intent i = new Intent(FavActivity.this, MapsActivity.class);
                    startActivity(i);

                    return true;

                case R.id.navigation_dashboard:

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

        navigation.setSelectedItemId(R.id.navigation_dashboard);

        lista = (ListView) findViewById(R.id.favlist);

        ArrayList<Station> itemsFavStations = ExtractJson.favStationList;

        adapter = new StationAdapter(this, itemsFavStations, FavActivity.this);

        lista.setAdapter(adapter);

        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute();

    }

    /**Fill the favourite list and display it**/
    class DownloadWebPageTask extends AsyncTask<String, Void, String> {

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

            adapter.notifyDataSetChanged();
        }
    }
}
