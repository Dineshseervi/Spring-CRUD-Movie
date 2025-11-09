package com.real.interview.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.real.interview.entity.Movie;
import com.real.interview.exception.MovieNotFoundException;
import com.real.interview.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    //CRUD method
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Page<Movie> findAll(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }


    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie findById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
    }

    public Movie updateMovieById(Long movieId, Movie movie) {
        Movie currentMovie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException(movieId));

        currentMovie.setTitle(movie.getTitle());
        currentMovie.setReleaseYear(movie.getReleaseYear());
        currentMovie.setRating(movie.getRating());
        currentMovie.setReleaseYear(movie.getReleaseYear());
        currentMovie.setGenre(movie.getGenre());
       return movieRepository.save(currentMovie);
    }

    public List<Movie> findByTitleAndReleaseYear(String title, Integer releaseYear) {

        return movieRepository.findByTitleAndReleaseYear(title, releaseYear);
    }

    public boolean deleteById(Long id) {
        if (!movieRepository.existsById(id)) {
            return false;

        }
        movieRepository.deleteById(id);
        return true;
    }

}