package com.iza.ppc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class representing a program
 *
 * @author Zakhar Izverov
 * created on 04.10.2021
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Program {

    private String id;
    private String title;
    private TvShow tvShow;

    public Program() {
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

    public TvShow getTvShow() {
        return tvShow;
    }

    public void setTvShow(TvShow tvShow) {
        this.tvShow = tvShow;
    }

    @Override
    public String toString() {
        return "Program{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tvShow=" + tvShow +
                '}';
    }
}
