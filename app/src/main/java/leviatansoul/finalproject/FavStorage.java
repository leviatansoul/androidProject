package leviatansoul.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavStorage {

    public static List<String> favList = new ArrayList<>();

    public static void initFavList(Activity a){
        String[] favs = getFavs(a);
        favList = new ArrayList<>(Arrays.asList(favs));
        updateFavList();
    }


    public static String[] getFavs(Activity a){

        String[] favs = {};
        SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        String favsString = sharedPref.getString("favs", null);
        if (favsString != null){
            favs = favsString.replace("[","").replace("]", "").replace(" ","").split(",");
        }
        return favs;

    }

    public static void insertFav(String station, Activity a){
        String[] favs = getFavs(a);
        favList = Arrays.asList(favs);
        favList = new ArrayList<>(favList);
        favList.add(station);
        saveFavs(favList.toArray(new String[0]), a);
    }

    public static void deleteFav(String station, Activity a){
        String[] favs = getFavs(a);
        favList = Arrays.asList(favs);
        favList = new ArrayList<>(favList);
        favList.remove(station);
        saveFavs(favList.toArray(new String[0]), a);

    }

    public static void saveFavs(String[] favs, Activity a){

        SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String prueba = Arrays.toString(favs);
        editor.putString("favs", Arrays.toString(favs));
        editor.commit();
        updateFavList();

    }

    public static void updateFavList(){
        for(String i: favList){
            ExtractJson.favStationList.add(ExtractJson.stationList.get(Integer.parseInt(i)-1));
        }
    }




}
