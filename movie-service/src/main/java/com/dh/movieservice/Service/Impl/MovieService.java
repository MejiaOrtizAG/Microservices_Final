package com.dh.movieservice.Service.Impl;

import com.dh.movieservice.Model.Movie;
import com.dh.movieservice.Repository.MovieRepository;
import com.dh.movieservice.Service.IMovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService implements IMovieService
{
    public static Logger LOG = LoggerFactory.getLogger(MovieService.class);

    @Value("${queue.movie.name}")
    private String movieQueue;

    private final MovieRepository movieRepository;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MovieService(MovieRepository movieRepository, RabbitTemplate rabbitTemplate)
    {
        this.movieRepository = movieRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public List<Movie> getListByGenre(String genre)
    {
        List<Movie> movies = movieRepository.findAllByGenre(genre);
        LOG.info("Search successful: " + movies);
        return movies;
    }

    //CircuitBreaker Test
    @Override
    public List<Movie> getListByGenreError(String genre, Boolean throwError)
    {
        if (throwError)
        {
            LOG.error("Search error " + genre);
            throw new RuntimeException();
        }
        List<Movie> movies = movieRepository.findAllByGenre(genre);
        LOG.info("Search successful: " + movies);
        return movies;
    }

    @Override
    public void save(Movie movie)
    {
        movieRepository.save(movie);
        LOG.info("Movie registered correctly: " + movie);
        rabbitTemplate.convertAndSend(movieQueue, movie);
        LOG.info("Movie was send to queue");
    }
}
