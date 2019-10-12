package com.example.procontexttestwork;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    private static final String TAG = "JsonParser";

    public ArrayList<Photographer> getPhotographers(String response) {
        String name;
        int id;
        ArrayList<Photographer> photographers = new ArrayList<>();
        try {
            JSONArray userJson = new JSONArray(response);
            for(int i = 0; i<userJson.length(); i++){
                JSONObject o = userJson.getJSONObject(i);
                name = o.getString("name");
                id = Integer.valueOf(o.getString("id"));
                photographers.add(new Photographer(id, name));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photographers;
    }

    public ArrayList<Album> getAlbums(String response) {
        String title;
        int id;
        int photographerId;
        ArrayList<Album> albums = new ArrayList<>();
        try {
            JSONArray userJson = new JSONArray(response);
            for(int i = 0; i<userJson.length(); i++){
                JSONObject o = userJson.getJSONObject(i);
                title = o.getString("title");
                id = Integer.valueOf(o.getString("id"));
                photographerId = Integer.valueOf(o.getString("userId"));
                albums.add(new Album(id, title, photographerId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albums;
    }

    public ArrayList<Photo> getPhotos(String response) {
        String title;
        int id;
        int albumId;
        String Url;
        ArrayList<Photo> photos = new ArrayList<>();
        try {
            JSONArray userJson = new JSONArray(response);
            for(int i = 0; i<userJson.length(); i++){
                JSONObject p = userJson.getJSONObject(i);
                title = p.getString("title");
                id = Integer.valueOf(p.getString("id"));
                albumId = Integer.valueOf(p.getString("albumId"));
                Url = p.getString("url");
//                Log.e(TAG, thumbnailUrl);
                photos.add(new Photo(Url, id, title, albumId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photos;
    }

}
