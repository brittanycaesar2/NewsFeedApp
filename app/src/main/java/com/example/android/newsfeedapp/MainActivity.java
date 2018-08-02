package com.example.android.newsfeedapp;


import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /*
     * URl for news data from guardian website
     */
    public String REQUEST_URL =
            "https://content.guardianapis.com/search?from-date=2018-07-25&to-date=2018-07-26&use-date=published&q=air%20quality&api-key=23665810-aed6-4bd3-9887-3ea3d1382f57";

    private static final int NEWS_LOADER_ID = 1;
    //list of news
    private NewsAdapter mAdapter;
    //displayed when list is empty
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //reference to ListView
        ListView newsListView = (ListView) findViewById(R.id.list);

        //new adapter with empty list of news
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);
            //item click listener on ListView, with Intent to web
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                //convert string URL to URI object
                Uri newsUri = Uri.parse(currentNews.getWebUrl());
                //new intent to view news uri
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                //send intent to new activity
                startActivity(webIntent);
            }
        });
        //check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //current data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //if no connection
        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            //update empty state text to no internet connection
            mEmptyStateTextView.setText("No internet Connection");

        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        //Hide loading indicator
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        //empty state text to no news
        mEmptyStateTextView.setText(R.string.no_news);
        //Clear from previous news data
        mAdapter.clear();
        //update ListView
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }

    }

    //Loader reset, clear existing data
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }
}
