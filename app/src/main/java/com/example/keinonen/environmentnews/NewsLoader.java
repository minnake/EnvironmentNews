package com.example.keinonen.environmentnews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

        private String webUrl;

        NewsLoader(Context context, String url) {
            super(context);
            webUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<News> loadInBackground() {
            if (webUrl == null) {
                return null;
            }
            return Utils.fetchNewsData(webUrl, getContext());
        }
    }
