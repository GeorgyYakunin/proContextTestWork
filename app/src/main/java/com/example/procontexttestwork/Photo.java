package com.example.procontexttestwork;

public class Photo extends JsonInfoClass{
    String url;
    int id;
    int albumId;
    String name;

    public Photo (String thumbnailUrl, int id, String name, int albumId){
        super(id, name);
        this.url = thumbnailUrl;
        this.albumId = albumId;
    }

    public String getUrl(){
        return url;
    }
    public int getAlbumId(){
        return albumId;
    }
}

