package com.example.procontexttestwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = "GalleryActivity";
    private int albumId;
    private RecyclerView PhotosRecyclerView;
    private RVPhotoAdapter mAdapter;
    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        setTitle("Gallery");
        Intent intent = getIntent();
        albumId = intent.getIntExtra("id", 0);
        mDBHelper = new DBHelper(this);
        initRecyclerView();
    }



    public void initRecyclerView(){
        PhotosRecyclerView = findViewById(R.id.rvPhotos);
        PhotosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RVPhotoAdapter(mDBHelper.getPhotoArray(albumId), this);
        PhotosRecyclerView.setAdapter(mAdapter);

    }
}
