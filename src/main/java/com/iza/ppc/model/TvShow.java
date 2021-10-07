package com.iza.ppc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class representing a TV show
 *
 * @author Zakhar Izverov
 * created on 04.10.2021
 */

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvShow {

    private String id;
    private String title;

    public TvShow() {
    }

    public TvShow(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", showTitle='" + title + '\'' +
                '}';
    }
}
