package com.dh.gatewayservice.Controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallBackController
{
    @CircuitBreaker(name = "moviesService")
    @GetMapping("/movies")
    public ResponseEntity<String> moviesFallback()
    {
        return new ResponseEntity<>("Movie server not available.Please, contact support.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @CircuitBreaker(name = "seriesService")
    @GetMapping("/series")
    public ResponseEntity<String> serieFallback()
    {
        return new ResponseEntity<>("Series server not available.Please, contact support.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @CircuitBreaker(name = "catalogsService")
    @GetMapping("/catalogs")
    public ResponseEntity<String> catalogFallback()
    {
        return new ResponseEntity<>("Catalog server not available.Please, contact support.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
