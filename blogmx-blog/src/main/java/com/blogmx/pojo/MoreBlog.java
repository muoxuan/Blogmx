package com.blogmx.pojo;

import java.util.List;

public class MoreBlog {

    private Blog b;
    private String day;
    private String month;
    private String year;
    private String url;
    private String subtitle;
    private List<String> lables;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Blog getB() {
        return b;
    }

    public void setB(Blog blog) {
        this.b = blog;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
