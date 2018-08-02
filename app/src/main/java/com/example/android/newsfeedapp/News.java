package com.example.android.newsfeedapp;

public class News {

   private String mWebTitle,  mPublicationDate, mWebUrl;

    public News (String WebTitle, String PublicationDate, String WebUrl){
        mWebTitle = WebTitle;
        mPublicationDate = PublicationDate;
        mWebUrl = WebUrl;
    }
public String getWebTitle(){
        return mWebTitle;
}


    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getWebUrl() {
        return mWebUrl;
    }
}
