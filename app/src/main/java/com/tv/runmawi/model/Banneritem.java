package com.tv.runmawi.model;

import java.io.Serializable;

public class Banneritem implements Serializable {
    private String banner_id;
    private String title;
    private String director;
    private String release_year;
    private String trailer_url;
    private String genre;
    private String img;
    private String ppv_cost;
    private String direct_url;
    private String video_id;
    private String summary;
    private String category;

    public String getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(String banner_id) {
        this.banner_id = banner_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRelease_year() {
        return release_year;
    }

    public void setRelease_year(String release_year) {
        this.release_year = release_year;
    }

    public String getTrailer_url() {
        return trailer_url;
    }

    public void setTrailer_url(String trailer_url) {
        this.trailer_url = trailer_url;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPpv_cost() {
        return ppv_cost;
    }

    public void setPpv_cost(String ppv_cost) {
        this.ppv_cost = ppv_cost;
    }

    public String getDirect_url() {
        return direct_url;
    }

    public void setDirect_url(String direct_url) {
        this.direct_url = direct_url;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
