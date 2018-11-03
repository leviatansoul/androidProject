package leviatansoul.finalproject;

import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById( R.id.progressBar);

        MainActivity.DownloadWebPageTask task = new MainActivity.DownloadWebPageTask();
        task.execute();

    }

    private class DownloadWebPageTask extends AsyncTask<String, Integer, String> {

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
        protected void onProgressUpdate(Integer... values) {
            // Executed on UI thread
            progressBar.incrementProgressBy( 10 );
        }

        @Override
        protected void onPostExecute(String result) {

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);

        }
    }
}


