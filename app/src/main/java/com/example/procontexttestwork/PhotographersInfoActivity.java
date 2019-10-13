package com.example.procontexttestwork;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONException;

public class PhotographersInfoActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "PhotographersInfoActivity";
    private RecyclerView PhotographersRecyclerView;
    private RVAdapter mAdapter;
    private DBHelper mDBHelper;
    private DBObservable mDBObservable;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photographer_info_activity);
        setTitle("Photographers");
        mDBObservable = new DBObservable (this);
        mDBObservable.addObserver(this);
        if(mDBObservable.getDBState() == false){
            mDBHelper = new DBHelper (this, mDBObservable);
            mDBHelper.getReadableDatabase();
        } else {
            mDBHelper = new DBHelper (this);
            initRecyclerView();
        }
    }

    @SuppressLint("LongLogTag")
    private void initRecyclerView() {
        PhotographersRecyclerView = findViewById(R.id.rvPhotographers);
        PhotographersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RVAdapter(mDBHelper.getPhotographerArray(), this);
        PhotographersRecyclerView.setAdapter(mAdapter);
        View v1 = findViewById(R.id.download_placeholder);
        View v2 = findViewById(R.id.download_placeholder_layout);
        v1.setVisibility(View.GONE);
        v2.setVisibility(View.GONE);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void update(Observable observable, Object arg) {
            if(mDBObservable.getDBState() == true)
            initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete_data_button:
                mDBHelper.deleteData();
                mAdapter.notifyDataSetChanged();
//                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

