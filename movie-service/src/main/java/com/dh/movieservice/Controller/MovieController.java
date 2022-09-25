package com.dh.movieservice.Controller;

import com.dh.movieservice.Model.Movie;
import com.dh.movieservice.Service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController
{
    @Value("${server.port}")
    private String port;

    private final IMovieService movieService;

    @Autowired
    public MovieController(IMovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{genre}")
    public ResponseEntity<List<Movie>> getMovieByGenre(@PathVariable String genre, HttpServletResponse response)
    {
        response.addHeader("port", port);
        return ResponseEntity.ok().body(movieService.getListByGenre(genre));
    }

    @GetMapping("/withErrors/{genre}")
    public ResponseEntity<List<Movie>> getMovieByGenreError (@PathVariable String genre, @RequestParam("throwError") boolean throwError, HttpServletResponse response)
    {
        response.addHeader("port", port);
        return ResponseEntity.ok().body(movieService.getListByGenreError(genre, throwError));
    }

    @PostMapping
    public ResponseEntity<String> saveMovie(@RequestBody Movie movie)
    {
        movieService.save(movie);
        return ResponseEntity.ok("The movie '"+movie.getName()+"' was registered correctly");
    }
}
