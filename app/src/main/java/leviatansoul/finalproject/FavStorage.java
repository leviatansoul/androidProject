package leviatansoul.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavStorage {

    public static List<String> favList = new ArrayList<>();

    public void initFavTest(){
        favList.add("2");
        favList.add("3");
        favList.add("4");
    }

    public String[] getFavs(){

        String[] favs = {};
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String favsString = sharedPref.getString("favs", null);
        if (favs != null){
            favs = favsString.split(",");
        }
        return favs;

    }

    public void insertFav(String station){
        String[] favs = getFavs();
        favList = Arrays.asList(favs);
        favList.add(station);
        saveFavs((String[])favList.toArray());
    }

    public void deleteFav(String station){
        String[] favs = getFavs();
        favList = Arrays.asList(favs);
        favList.remove(station);
        saveFavs((String[])favList.toArray());

    }

    public void saveFavs(String[] favs){

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("favs", favs.toString());
        editor.commit();

    }



}
