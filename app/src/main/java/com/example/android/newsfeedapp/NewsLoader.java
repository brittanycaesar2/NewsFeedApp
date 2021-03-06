package com.example.android.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();
    //URL
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;

    }
    @Override
    protected void onStartLoading(){
        forceLoad();
    }
    @Override
    public List<News> loadInBackground(){
        if (mUrl == null){
            return null;
        }
    List<News> news = QueryUtils.fetchNewsData(mUrl);
        return news;


    }
}
