package com.tv.runmawi.model;

public class pShowsItem {
    private Integer id;
    private String shows_id;
    private String v_id;
    private String p_date;
    private String validity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getV_id() {
        return v_id;
    }

    public void setV_id(String v_id) {
        this.v_id = v_id;
    }

    public String getP_date() {
        return p_date;
    }

    public void setP_date(String p_date) {
        this.p_date = p_date;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getShows_id() {
        return shows_id;
    }

    public void setShows_id(String shows_id) {
        this.shows_id = shows_id;
    }
}
