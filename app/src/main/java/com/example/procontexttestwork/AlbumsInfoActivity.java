package com.example.procontexttestwork;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

public class AlbumsInfoActivity extends AppCompatActivity {
    private RecyclerView AlbumsRecyclerView;
    private RVAdapter mAdapter;
    private SearchView searchView;
    private int photographerId;
    private DBHelper mDBHelper;
    private static final String TAG = "AlbumsInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ablums_activity);
        setTitle("Albums");
        Intent intent = getIntent();
        photographerId = intent.getIntExtra("id", 0);
        mDBHelper = new DBHelper(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        AlbumsRecyclerView = findViewById(R.id.rvAlbums);
        AlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RVAdapter(mDBHelper.getAlbumArray(photographerId), this);
        AlbumsRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_albums, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

}
