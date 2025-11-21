package com.real.interview.controller;


import com.real.interview.entity.Movie;
import com.real.interview.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping()
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie newMovie = movieService.save(movie);
        return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
    }



    /**
     * Retrieves all movies with pagination and sorting support.
     * The Pageable object is automatically created by Spring from request params:
     * e.g., /api/movies?page=0&size=10&sort=title,asc
     */
    @GetMapping()
    public ResponseEntity<Page<Movie>> getAllMovie(Pageable pageable) {
        Page<Movie> moviesPage = movieService.findAll(pageable);
        return new ResponseEntity<>(moviesPage, HttpStatus.OK);
    }


    @GetMapping("/{movieId}")
    public ResponseEntity<Movie> getMovieForId(@PathVariable Long movieId) {
        Movie movie = movieService.findById(movieId);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long movieId,@RequestBody Movie movie)
    {
        Movie updatedMovie=movieService.updateMovieById(movieId,movie);
        return new ResponseEntity<>(movie,HttpStatus.OK);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        boolean isDeleted = movieService.deleteById(movieId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovie(
            @RequestParam String title,
            @RequestParam Integer year
    ) {

        List<Movie> movies = movieService.findByTitleAndReleaseYear(title, year);
        return ResponseEntity.of(Optional.ofNullable(movies));
    }

}
