package com.example.keinonen.environmentnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    NewsAdapter(Context context, List<News> newest) {
        super(context, 0, newest);
    }

    private static final String NO_AUTHOR = "No author";

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView authorTextView = listItemView.findViewById(R.id.author_view);
        if (currentNews != null) {
            if (currentNews.getAuthorName().equals(NO_AUTHOR)) {
                authorTextView.setText(R.string.no_author);
            } else {
                authorTextView.setText(currentNews.getAuthorName());
            }
        }

        TextView sectionTextView = listItemView.findViewById(R.id.section_view);
        if (currentNews != null) {
            sectionTextView.setText(currentNews.getSectionName());
        }

        TextView titleTextView = listItemView.findViewById(R.id.title_view);
        if (currentNews != null) {
            titleTextView.setText(currentNews.getTitle());
        }

        String patternIncome = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat dateFormat = new SimpleDateFormat(patternIncome);

        Date myDate = null;
        try {
            if (currentNews != null) {
                myDate = dateFormat.parse(currentNews.getDateTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String datePatternOutcome = "d MMM, yyyy";
        SimpleDateFormat newDateFormat = new SimpleDateFormat(datePatternOutcome);
        String outcomeDate = newDateFormat.format(myDate);

        TextView dateView = listItemView.findViewById(R.id.date);
        dateView.setText(outcomeDate);

        return listItemView;
    }
}