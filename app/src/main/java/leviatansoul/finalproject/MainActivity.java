package leviatansoul.finalproject;

import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.DownloadWebPageTask task = new MainActivity.DownloadWebPageTask();
        task.execute();
    }

    /**Download the Json from the web, fill the station class with the station information, and launch a new activity**/
    private class DownloadWebPageTask extends AsyncTask<String, Integer, String> {

        @Override
        @SuppressWarnings("deprecation")
        protected String doInBackground(String... urls) {
            String response = "";

            try {
                //fill the station class with the information from the web
                ExtractJson.fillStationList();
            } catch (Exception e) {
                response = e.toString();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            //Intent For Navigating to MapsActivity
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }
    }
}


