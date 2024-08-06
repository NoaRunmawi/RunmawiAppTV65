package com.tv.runmawi.model;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

public class Movies implements Serializable {
    private String video_id;
    private String title;
    private String category;
    private String thumbnail;
    private String director;
    private String producer;
    private String description;
    private String genre;
    private String length;
    private String direct_url;
    private String ppv_cost;
    private String ppv_validity;
    private String status;
    private String priority;
    private String premier_on;
    private String trailer;

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
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

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDirect_url() {
        return direct_url;
    }

    public void setDirect_url(String direct_url) {
        this.direct_url = direct_url;
    }

    public String getPpv_cost() {
        return ppv_cost;
    }

    public void setPpv_cost(String ppv_cost) {
        this.ppv_cost = ppv_cost;
    }

    public String getPpv_validity() {
        return ppv_validity;
    }

    public void setPpv_validity(String ppv_validity) {
        this.ppv_validity = ppv_validity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPremier_on() {
        return premier_on;
    }

    public void setPremier_on(String premier_on) {
        this.premier_on = premier_on;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public Movies() {
    }

    public URI getCardImageURI() {
        try {
            return new URI(getThumbnail());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
}

