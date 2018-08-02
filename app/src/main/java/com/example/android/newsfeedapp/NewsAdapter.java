package com.example.android.newsfeedapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;

import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView webTitleView = (TextView) listItemView.findViewById(R.id.web_title);

        webTitleView.setText(currentNews.getWebTitle());

        TextView webUrlView = (TextView) listItemView.findViewById(R.id.web_url);

        webUrlView.setText(currentNews.getWebUrl());

        Date dateObject = new Date(currentNews.getPublicationDate());

        TextView dateView = (TextView) listItemView.findViewById(R.id.publication_date);
        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        return listItemView;
    }

        //return formatted date string from date object
    public String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

}
