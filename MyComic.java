package com.example.comicapi;

import android.webkit.WebViewClient;

import java.net.URL;

public class MyComic {
    private String name;
    private int number;
    private String url;

    public MyComic(String name, int number, String url) {
        this.name = name;
        this.number = number;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getUrl() { return url; }

    @Override
    public String toString() {
        return "MyComic{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", url=" + url +
                '}';
    }
}