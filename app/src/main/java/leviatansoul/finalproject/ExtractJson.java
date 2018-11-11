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
    private final static String URL_BICIMAD_SINGLE = "https://rbdata.emtmadrid.es:8443/BiciMad/get_single_station/WEB.SERV.diego2.gd@gmail.com/9933C03A-C88F-4222-8556-6431A1D0D84A/";

    /**Returns the Json file in String**/
    public static String getFile(String url) {

        URL u;
        InputStream is = null;
        BufferedReader dis;
        String s;
        String res = "";

        try {
            //Create the URL.
            u = new URL(url);

            //Open an input stream from the url.
            is = u.openStream();

            // Convert the InputStream to a buffered DataInputStream.
            dis = new BufferedReader(new InputStreamReader(u.openStream()));

            //read each record of the input stream
            while ((s = dis.readLine()) != null) {

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
            //Close the InputStream  //
            try {
                is.close();

            } catch (IOException ioe) {

            }
        }
        return res;
    }

    /** Returns a Json Object with all the information of the web **/
    public static JsonObject getJson(String url) {

        JsonObject jobj = new JsonObject();
        try {
            String jsonString = ExtractJson.getFile(url);
            Gson gson = new Gson();
            JsonElement jelem = gson.fromJson(jsonString, JsonElement.class);
            jobj = jelem.getAsJsonObject();

        } catch (Exception e) {

            e.printStackTrace();
        }
        return jobj;
    }

    /**Fill the station class with the Json information **/
    public static void fillStationList() {
        JsonObject json = ExtractJson.getJson(URL_BICIMAD);

        String data = json.get("data").getAsString();

        Gson gson = new Gson();
        JsonElement jelem = gson.fromJson(data, JsonElement.class);
        JsonObject dataJson = jelem.getAsJsonObject();

        JsonArray stationsJson = dataJson.getAsJsonArray("stations");

        //clear the list
        stationList.clear();

        for (int i = 0; i < stationsJson.size(); i++) {
            Station st = gson.fromJson(stationsJson.get(i), Station.class);
            stationList.add(st);
        }

        System.out.println(URL_BICIMAD);
    }

    /**Get a single station from the web **/
    public static Station getSingleStation(String station) {
        String singleUrl = URL_BICIMAD_SINGLE + station;
        JsonObject json = ExtractJson.getJson(singleUrl);
        String data = json.get("data").getAsString();
        Gson gson = new Gson();
        JsonElement jelem = gson.fromJson(data, JsonElement.class);
        JsonObject dataJson = jelem.getAsJsonObject();
        JsonArray stationsJson = dataJson.getAsJsonArray("stations");
        return gson.fromJson(stationsJson.get(0), Station.class);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}