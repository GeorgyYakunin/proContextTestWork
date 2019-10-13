package com.example.procontexttestwork;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private HTTPClient httpClient;
    private DBObservable mDBObservable;

    private static final String TAG = "DBHelper";

    public static final String PHOTOGRAPHERS_TABLE_NAME = "Photographers_Table";
    public static final String PHOTOGRAPHER_ID = "photographer_ID";
    public static final String PHOTOGRAPHER_NAME = "photographer_name";
    public static final String PHOTOGRAPHERS_TABLE_EXEC = "create table IF NOT EXISTS " + PHOTOGRAPHERS_TABLE_NAME +
                                                          " ( " + PHOTOGRAPHER_ID + " integer primary key , " +
                                                          PHOTOGRAPHER_NAME + " text not null);";

    public static final String ALBUMS_TABLE_NAME = "Albums_Table";
    public static final String ALBUM_ID = "album_ID";
    public static final String ALBUM_NAME = "album_name";
    public static final String ALBUM_PHOTOGRAPHER_ID = "album_photographer_ID";
    public static final String ALBUMS_TABLE_EXEC = "create table IF NOT EXISTS " + ALBUMS_TABLE_NAME +
                                                   " ( " + ALBUM_ID + " integer primary key , " +
                                                   ALBUM_NAME + " text not null, " +
                                                   ALBUM_PHOTOGRAPHER_ID + " integer not null);";

    public static final String PHOTOS_TABLE_NAME = "Photos_Table";
    public static final String PHOTO_ID = "photo_ID";
    public static final String PHOTO_ALBUM_ID = "photo_album_ID";
    public static final String PHOTO_URL = "photo_url";
    public static final String PHOTO_NAME = "photo_name";
    public static final String PHOTOS_TABLE_EXEC = "create table IF NOT EXISTS " + PHOTOS_TABLE_NAME +
                                                   " ( " + PHOTO_ID + " integer primary key , " +
                                                   PHOTO_ALBUM_ID + " integer not null, " +
                                                   PHOTO_URL + " text not null," +
                                                   PHOTO_NAME + " text not null);";

    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    public DBHelper(Context context, DBObservable mDBObservable) {
        super(context, "myDB", null, 1);
        this.mDBObservable = mDBObservable;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        httpClient = new HTTPClient();
        Log.e(TAG, "---onCreate DB---");
        db.execSQL(PHOTOGRAPHERS_TABLE_EXEC);
        db.execSQL(ALBUMS_TABLE_EXEC);
        db.execSQL(PHOTOS_TABLE_EXEC);
        loadInfo();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @SuppressLint("StaticFieldLeak")
    private void loadInfo() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    addPhotographsArrayInDB(httpClient.readPhotographerInfo());
                    addAlbumsArrayInDB(httpClient.readAlbumInfo());
                    addPhotoArrayInDB(httpClient.readPhotoInfo());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(Void result) {
                mDBObservable.setNotificationFromDBHelper(true);
            }
        }.execute();
    }

    private void addPhotographsArrayInDB(ArrayList<Photographer> photographers){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        for( Photographer p : photographers){
            values.put(PHOTOGRAPHER_ID, p.getId());
            values.put(PHOTOGRAPHER_NAME, p.getName());
            db.insert(PHOTOGRAPHERS_TABLE_NAME, null, values);
        }
    }

    private void addAlbumsArrayInDB(ArrayList<Album> albums){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        for(Album a : albums){
            values.put(ALBUM_ID, a.getId());
            values.put(ALBUM_NAME, a.getName());
            values.put(ALBUM_PHOTOGRAPHER_ID, a.getPhotographerId());
            db.insert(ALBUMS_TABLE_NAME, null, values);
        }
    }

    private void addPhotoArrayInDB(ArrayList<Photo> photos){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        for(Photo p : photos){
            values.put(PHOTO_ID, p.getId());
            values.put(PHOTO_ALBUM_ID, p.getAlbumId());
            values.put(PHOTO_URL, p.getUrl());
            values.put(PHOTO_NAME, p.getName());
            db.insert(PHOTOS_TABLE_NAME, null, values);
        }

    }

    public ArrayList<Photographer> getPhotographerArray() {
        ArrayList<Photographer> photographers = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PHOTOGRAPHERS_TABLE_NAME,null,null,null,null,null,null);
        try{
            if(cursor.moveToFirst()){
                int id = cursor.getColumnIndex(PHOTOGRAPHER_ID);
                int name = cursor.getColumnIndex(PHOTOGRAPHER_NAME);
                do{
                    photographers.add(new Photographer(cursor.getInt(id), cursor.getString(name)));
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }  else{
                cursor.close();
            }
        } finally {
            cursor.close();

            return photographers;
        }
    }

    public ArrayList<Album> getAlbumArray(int pID) {
        String where = ALBUM_PHOTOGRAPHER_ID + " = ? ";
        String[] whereArgs = new String[] {
                "" + pID
        };
        ArrayList<Album> albums = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ALBUMS_TABLE_NAME,null, where, whereArgs,null,null,null);
        try{
            if(cursor.moveToFirst()){
                int id = cursor.getColumnIndex(ALBUM_ID);
                int name = cursor.getColumnIndex(ALBUM_NAME);
                int photographerId = cursor.getColumnIndex(ALBUM_PHOTOGRAPHER_ID);
                do{
                    albums.add(new Album(cursor.getInt(id), cursor.getString(name), cursor.getInt(photographerId)));
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }  else{
                cursor.close();
            }
        } finally {
            cursor.close();
            return albums;
        }
    }

    public ArrayList<Photo> getPhotoArray(int aID) {
        String where = PHOTO_ALBUM_ID + " = ? ";
        String[] whereArgs = new String[] {
                "" + aID
        };
        ArrayList<Photo> photos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PHOTOS_TABLE_NAME,null, where, whereArgs,null,null,null);
        try{
            if(cursor.moveToFirst()){
                int id = cursor.getColumnIndex(PHOTO_ID);
                int albumId = cursor.getColumnIndex(PHOTO_ALBUM_ID);
                int url = cursor.getColumnIndex(PHOTO_URL);
                int name = cursor.getColumnIndex(PHOTO_NAME);
                do{
                    photos.add(new Photo(cursor.getString(url), cursor.getInt(id), cursor.getString(name), cursor.getInt(albumId)));
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }  else{
                cursor.close();
            }
        } finally {
            cursor.close();
            return photos;
        }
    }

    public void deleteData(){
        SQLiteDatabase db = getReadableDatabase();
        db.delete(PHOTOGRAPHERS_TABLE_NAME, null, null);
        db.delete(ALBUMS_TABLE_NAME, null, null);
        db.delete(PHOTOS_TABLE_NAME, null, null);
    }

    public void rename (String newName, Class ArrayType, int id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if(ArrayType == Photographer.class){
            values.put(PHOTOGRAPHER_NAME, newName);
            db.update(PHOTOGRAPHERS_TABLE_NAME, values,PHOTOGRAPHER_ID + "= ?", new String[] {Integer.toString(id)});
        }
        else if (ArrayType == Album.class){
            values.put(ALBUM_NAME,newName);
            db.update(ALBUMS_TABLE_NAME, values,ALBUM_ID + "= ?", new String[] {Integer.toString(id)});
        }
    }
}


