package leviatansoul.finalproject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExtractJson {

    public static ArrayList<Station> stationList = new ArrayList<Station>();
    public static ArrayList<Station> favStationList = new ArrayList<Station>();
    private final static String URL_BICIMAD = "https://rbdata.emtmadrid.es:8443/BiciMad/get_stations/WEB.SERV.diego2.gd@gmail.com/9933C03A-C88F-4222-8556-6431A1D0D84A/";


    public static String getFile(String url) {
        //-----------------------------------------------------//
        //  Step 1:  Start creating a few objects we'll need.
        //-----------------------------------------------------//

        URL u;
        InputStream is = null;
        BufferedReader dis;
        String s;
        String res = ""; //

        try {

            //------------------------------------------------------------//
            // Step 2:  Create the URL.                                   //
            //------------------------------------------------------------//
            // Note: Put your real URL here, or better yet, read it as a  //
            // command-line arg, or read it from a file.                  //
            //------------------------------------------------------------//

            u = new URL(url);

            //----------------------------------------------//
            // Step 3:  Open an input stream from the url.  //
            //----------------------------------------------//

            is = u.openStream();         // throws an IOException

            //-------------------------------------------------------------//
            // Step 4:                                                     //
            //-------------------------------------------------------------//
            // Convert the InputStream to a buffered DataInputStream.      //
            // Buffering the stream makes the reading faster; the          //
            // readLine() method of the DataInputStream makes the reading  //
            // easier.                                                     //
            //-------------------------------------------------------------//

            dis = new BufferedReader(new InputStreamReader(u.openStream()));

            //------------------------------------------------------------//
            // Step 5:                                                    //
            //------------------------------------------------------------//
            // Now just read each record of the input stream, and print   //
            // it out.  Note that it's assumed that this problem is run   //
            // from a command-line, not from an application or applet.    //
            //------------------------------------------------------------//

            while ((s = dis.readLine()) != null) {
                //System.out.println(s);
                res = res + s;
            }

        } catch (MalformedURLException mue) {

            System.out.println("Ouch - a MalformedURLException happened.");
            mue.printStackTrace();
            System.exit(1);

        } catch (IOException ioe) {

            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            System.exit(1);

        } finally {

            //---------------------------------//
            // Step 6:  Close the InputStream  //
            //---------------------------------//

            try {
                is.close();

            } catch (IOException ioe) {
                // just going to ignore this one
            }

        } // end of 'finally' clause

        System.out.println(res);
        return res;
    }

    public static JsonObject getJson(String url) {

        JsonObject jobj = new JsonObject();
        try {
            //Url del json (aÃ±adir la de BiciMad)
            String jsonString = ExtractJson.getFile(url); //Obtiene Json en String
            Gson gson = new Gson();
            JsonElement jelem = gson.fromJson(jsonString, JsonElement.class);
            jobj = jelem.getAsJsonObject(); //Obtenemos Json de la web

        }
        catch (Exception e) {

            e.printStackTrace();
        }
        return jobj;
    }

    public static void fillStationList(){
        JsonObject json = ExtractJson.getJson(URL_BICIMAD);

        String data = json.get("data").getAsString();


        Gson gson = new Gson();
        JsonElement jelem = gson.fromJson(data, JsonElement.class);
        JsonObject dataJson = jelem.getAsJsonObject();

        JsonArray stationsJson = dataJson.getAsJsonArray("stations");

        for(int i = 0; i<stationsJson.size(); i++) {
            Station st = gson.fromJson(stationsJson.get(i), Station.class);
            System.out.println(st.getLatitude());
            stationList.add(st);
        }


        System.out.println(URL_BICIMAD);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub







    }

}
