package com.example.keinonen.environmentnews;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String LOG_TAG = Utils.class.getName();

    public static List<News> fetchNewsData(String requestUrl, Context context) {
        Log.e(LOG_TAG, "fetchNewsData()");
        URL url = createUrl(requestUrl, context);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url, context);
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.problem_http_request), e);
        }

        return extractFeatureFromJson(jsonResponse, context);
    }


    private static URL createUrl(String stringUrl, Context context) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, context.getString(R.string.problem_http_request), e);
        }
        return url;
    }

    private static final int SUCCESS_CODE = 200;

    static String makeHttpRequest(URL url, Context context) throws IOException {
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == SUCCESS_CODE){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, context.getString(R.string.error_code) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.problem_retrieving_results), e);
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


    private static List<News> extractFeatureFromJson(String newsJSON, Context context) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> newest = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject newsObject = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = newsObject.getJSONArray("results");

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);

                String author;
                JSONArray tagsArray = currentNews.getJSONArray("tags");
                if (tagsArray.length() > 0) {
                    JSONObject currentTag = tagsArray.getJSONObject(0);
                    author = currentTag.getString("webTitle");
                } else author = context.getString(R.string.no_author);

                String title = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String time = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");

                News news = new News(title, author, section, time, url);

                newest.add(news);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, context.getString(R.string.problem_parsing), e);
        }
        return newest;
    }
}
