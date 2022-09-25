package com.dh.catalogservice.Client;

import com.dh.catalogservice.Model.Document.Serie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "serie-service")
public interface SerieClient
{
    @GetMapping("/series/{genre}")
    ResponseEntity<List<Serie>> findSerieByGenre(@PathVariable("genre") String genre);

    //CircuitBreaker Test
    @GetMapping("/series/withErrors/{genre}")
    ResponseEntity<List<Serie>> findSerieByGenreError(@PathVariable(value = "genre") String genre, @RequestParam("throwError") boolean throwError);
}
