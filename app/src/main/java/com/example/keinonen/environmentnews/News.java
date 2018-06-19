package com.example.keinonen.environmentnews;

public class News {

    private String title;

    private String authorName;

    private String sectionName;

    private String date;

    private String url;


    News(String titleText, String authorText, String sectionText, String dateText, String urlText) {
        authorName = authorText;
        sectionName = sectionText;
        title = titleText;
        date = dateText;
        url = urlText;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getAuthorName(){
        return authorName;}

    public String getTitle() {
        return title;
    }

    public String  getDateTime() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
