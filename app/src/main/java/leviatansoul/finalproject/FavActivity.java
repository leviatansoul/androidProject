package leviatansoul.finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        //mTextMessage = (TextView) findViewById(R.id.message);

        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute( "jeje" );

        /*
        new Thread(new Runnable() {
            public void run() {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start(); */



        lista = (ListView)findViewById(R.id.favlist);


        ArrayList<Station> itemsStations = ExtractJson.stationList;

        adapter = new StationAdapter(this, itemsStations);

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
