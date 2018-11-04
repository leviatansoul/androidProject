package leviatansoul.finalproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BikeStopWidgetConfigureActivity BikeStopWidgetConfigureActivity}
 */
public class BikeStopWidget extends AppWidgetProvider {

    private static CharSequence widgetText;
    private static String name = "";
    private static int appWidgetIdentificador = 0;
    private static Context contexto;
    private static AppWidgetManager awm;
    private static String num ;
    public static String WIDGET_BUTTON = "android.appwidget.action.APPWIDGET_UPDATE";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        appWidgetIdentificador = appWidgetId;
        contexto = context;
        awm = appWidgetManager;
        widgetText = BikeStopWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        final StringBuilder sb = new StringBuilder(widgetText.length());
        sb.append(widgetText);
       String s = sb.toString();
        Log.d("WIDGET ", s);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bike_stop_widget);
        num = s;
        if(!s.equals("Num. de estación")){

            GetStation task = new GetStation();
            task.execute(s);

        }




        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BikeStopWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }



    public void onReceive(Context context, Intent intent) {

        if (WIDGET_BUTTON.equals(intent.getAction())){
            Log.d("WIDGED", "FUNCIONA");
            recargar();
        }
    };

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void recargar(){
        GetStation task = new GetStation();
        task.execute(num);
    }


    private static class GetStation extends AsyncTask<String, Integer, String> {

        private String contentType = "";

        @Override
        @SuppressWarnings( "deprecation" )
        protected String doInBackground(String... urls) {
            String response = "";

            try {

                RemoteViews views = new RemoteViews(contexto.getPackageName(), R.layout.bike_stop_widget);
                Station station = ExtractJson.getSingleStation(urls[0]);
                name = station.getName();
                views.setTextViewText(R.id.wname, name);
                views.setTextViewText(R.id.waddress, station.getAddress());
                views.setTextViewText(R.id.wstation, Integer.toString(station.getId()));
                views.setTextViewText(R.id.wbicis, Integer.toString(station.getDock_bikes()));
                views.setTextViewText(R.id.wespacios, Integer.toString(station.getFree_bases()));
                views.setTextViewText(R.id.wnoavailable, Integer.toString(station.getNo_available()));

                Intent intent = new Intent(WIDGET_BUTTON);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(contexto, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.wbutton, pendingIntent);

                // Instruct the widget manager to update the widget
                awm.updateAppWidget(appWidgetIdentificador, views);

            } catch (Exception e) {
                response = e.toString();
            }
            return response;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Executed on UI thread

        }

        @Override
        protected void onPostExecute(String result) {


        }
    }




}

