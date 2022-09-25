package com.dh.serieservice.Model;

import java.util.Set;

public class Season
{
    private Integer id;
    private Integer seasonNumber;
    private String genre;
    private Set<Chapter> chapters;

    public Season() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(Set<Chapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public String toString() {
        return "Season{" +
                "id=" + id +
                ", seasonNumber=" + seasonNumber +
                ", genre='" + genre + '\'' +
                ", chapters=" + chapters +
                '}';
    }
}
