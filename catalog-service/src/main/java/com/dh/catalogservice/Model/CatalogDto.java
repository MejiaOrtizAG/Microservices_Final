package com.dh.catalogservice.Model;

import com.dh.catalogservice.Model.Document.Movie;
import com.dh.catalogservice.Model.Document.Serie;

import java.util.List;

public class CatalogDto {
    private String genre;
    private List<Movie> movies;
    private List<Serie> series;

    public CatalogDto() {
    }

    public CatalogDto(String genre, List<Movie> movies, List<Serie> series) {
        this.genre = genre;
        this.movies = movies;
        this.series = series;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }

    @Override
    public String toString() {
        return "CatalogDto{" +
                "genre='" + genre + '\'' +
                ", movies=" + movies +
                ", series=" + series +
                '}';
    }
}
