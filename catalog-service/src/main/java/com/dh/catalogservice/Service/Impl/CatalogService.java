package com.dh.catalogservice.Service.Impl;

import com.dh.catalogservice.Model.CatalogDto;
import com.dh.catalogservice.Model.Document.Catalog;
import com.dh.catalogservice.Model.Document.Movie;
import com.dh.catalogservice.Model.Document.Serie;
import com.dh.catalogservice.Repository.CatalogRepository;
import com.dh.catalogservice.Service.ICatalogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CatalogService implements ICatalogService {

    public static Logger LOG = LoggerFactory.getLogger(CatalogService.class);

    private final SerieService serieService;

    private final MovieService movieService;

    private final CatalogRepository catalogRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public CatalogService(SerieService serieService, MovieService movieService, CatalogRepository catalogRepository, ObjectMapper objectMapper)
    {
        this.serieService = serieService;
        this.movieService = movieService;
        this.catalogRepository = catalogRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = {"${queue.movie.name}"})
    public void saveMovie(Movie movie)
    {
        LOG.info("Got movie through Rabbitmq: " + movie.toString());
        Catalog catalog;
        List<Movie> movies;
        Optional<Catalog> catalogOptional = catalogRepository.findByGenre(movie.getGenre());
        if(!catalogOptional.isPresent())
        {
            catalog = new Catalog();
            catalog.setGenre(movie.getGenre());
            catalog.setSeries(new ArrayList<>());
            movies = new ArrayList<>();
        }
        else
        {
            catalog = catalogOptional.get();
            movies = catalog.getMovies();
        }
        movies.add(movie);
        catalog.setMovies(movies);
        catalogRepository.save(catalog);
        LOG.info("Catalog registered successfully: " + catalog);
    }

    @RabbitListener(queues = {"${queue.serie.name}"})
    public void saveSerie(Serie serie)
    {
        LOG.info("Got serie through Rabbitmq: " + serie.toString());
        Catalog catalog;
        List<Serie> series;
        Optional<Catalog> catalogOptional = catalogRepository.findByGenre(serie.getGenre());
        if (!catalogOptional.isPresent())
        {
            catalog = new Catalog();
            catalog.setGenre(serie.getGenre());
            catalog.setMovies(new ArrayList<>());
            series = new ArrayList<>();
        }
        else
        {
            catalog = catalogOptional.get();
            series = catalog.getSeries();
        }
        series.add(serie);
        catalog.setSeries(series);
        catalogRepository.save(catalog);
        LOG.info("Catalog registered successfully: " + catalog);
    }

    @Override
    public void save(CatalogDto catalogDto)
    {
        Catalog catalog;
        Optional<Catalog> catalogOptional = catalogRepository.findByGenre(catalogDto.getGenre());
        if (!catalogOptional.isPresent())
        {
            catalog = new Catalog();
            catalog.setGenre(catalogDto.getGenre());
        }
        else
        {
            catalog = catalogOptional.get();
        }
        catalog.setSeries(catalogDto.getSeries());
        catalog.setMovies(catalogDto.getMovies());
        catalogRepository.save(catalog);
        LOG.info("Catalog registered successfully: " + catalogDto);
    }

    @Override
    public CatalogDto getCatalogByGenreDB(String genere)
    {
        CatalogDto catalogDto= new CatalogDto();
        Optional<Catalog> catalogOptional = catalogRepository.findByGenre(genere);
        if (catalogOptional.isPresent())
        {
            Catalog catalog = catalogRepository.findByGenre(genere).get();
            catalogDto = objectMapper.convertValue(catalog,CatalogDto.class);
            LOG.info("Successful search: " + catalogDto);
            return catalogDto;
        }
        return null;
    }

    @Override
    @CircuitBreaker(name = "catalog", fallbackMethod = "catalogFallbackMethod")
    public CatalogDto getCatalogByGenreFeign(String genere)
    {
        List<Movie> movies = movieService.findMovieByGenre(genere);
        List<Serie> series = serieService.findSerieByGenre(genere);
        CatalogDto catalogDto = new CatalogDto(genere, movies, series);
        LOG.info("Successful search: " + catalogDto);
        save(catalogDto);
        return catalogDto;
    }

    //CircuitBreaker Test
    @Override
    @CircuitBreaker(name = "catalog", fallbackMethod = "catalogFallbackMethod")
    public CatalogDto getCatalogByGenreFeignError(String genere, Boolean movieErrors, Boolean serieErrors)
    {
        List<Movie> movies = movieService.findMovieByGenreError(genere, movieErrors);
        List<Serie> series = serieService.findSerieByGenreError(genere,serieErrors);
        CatalogDto catalogDto = new CatalogDto(genere, movies, series);
        LOG.info("Successful search: " + catalogDto);
        save(catalogDto);
        return catalogDto;
    }

    private CatalogDto catalogFallbackMethod(CallNotPermittedException exception) {
        LOG.info("Circuit breaker is active");
        return null;
    }
}
