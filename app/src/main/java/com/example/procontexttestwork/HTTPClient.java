package com.example.procontexttestwork;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HTTPClient {
    private final JsonParser jsonParser;
    private static final String TAG = "HTTPClient";

    public HTTPClient(){
        jsonParser = new JsonParser();
    }

    public ArrayList<Photographer> readPhotographerInfo() throws IOException {
        String requestUrl = "https://jsonplaceholder.typicode.com/users";
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream in;
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            in = connection.getErrorStream();
        } else {
            in = connection.getInputStream();
        }
        String response = convertStreamToString(in);
        ArrayList<Photographer> photographers = jsonParser.getPhotographers(response);

        return photographers;
    }

    public ArrayList<Album> readAlbumInfo() throws IOException {
        String requestUrl = "https://jsonplaceholder.typicode.com/albums";
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream in;
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            in = connection.getErrorStream();
        } else {
            in = connection.getInputStream();
        }
        String response = convertStreamToString(in);
        ArrayList<Album> albums = jsonParser.getAlbums(response);

        return albums;
    }

    public ArrayList<Photo> readPhotoInfo() throws IOException, JSONException {
        String requestUrl = "https://jsonplaceholder.typicode.com/photos";
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream in;
        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            in = connection.getErrorStream();
        } else {
            in = connection.getInputStream();
        }
        String response = convertStreamToString(in);
        ArrayList<Photo> photos = jsonParser.getPhotos(response);
        return photos;
    }

    private String convertStreamToString(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        stream.close();
        return sb.toString();
    }
}
