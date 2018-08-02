package com.example.android.newsfeedapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public final class QueryUtils {
    private static final String LOG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        //HTTPS request to URL and get a JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG, "Problem making HTTP request", e);

        }
        List<News> news = extractResponseFromJson(jsonResponse);
        //return list of news
        return news;
    }

    //return list of news objects from JSON response
    private static List<News> extractResponseFromJson(String jsonResponse) {
        String publicationDate;
        String webTitle;
        String webUrl;

        //return early if JSON empty or nul
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        //create an empty ArrayList that we can add news to
        List<News> newsArrayList = new ArrayList<>();
        try {
            JSONObject baseJSONResponse = new JSONObject(jsonResponse);

            JSONObject JSONResponseResult = baseJSONResponse.getJSONObject("response");

            JSONArray newsArray = JSONResponseResult.getJSONArray("results");
            //make the array list items
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNewsArticles = newsArray.getJSONObject(i);

                publicationDate = currentNewsArticles.getString("webPublicationDate");
                webTitle = currentNewsArticles.getString("webTitle");
                webUrl = currentNewsArticles.getString("webUrl");


                News article = new News(webTitle, webUrl, publicationDate);
                newsArrayList.add(article);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "extractNewsFromJson: Problem parsing results", e);

        }
        //return list of news
        return newsArrayList;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG, "Problem building URL", e);
        }
        return url;
    }

    //make HTTP request to URL return String response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //if url is null return
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //successful response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG, "Error response Code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }


        return output.toString();
    }


}