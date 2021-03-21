package com.example.covidcare.NewsPackage;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Headlines {

    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private String totalResults;

    @SerializedName("articles")
   List<Article>newsArticle;

    public List<Article> getNewsArticle() {
        return newsArticle;
    }

    public void setNewsArticle(List<Article> newsArticle) {
        this.newsArticle = newsArticle;
    }

    private List<Article> articleslist;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }



}
