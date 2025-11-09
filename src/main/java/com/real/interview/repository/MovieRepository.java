package com.real.interview.repository;

import com.real.interview.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Why we are not @Repository annotation on MovieRepository
 * Because Spring Data creates and registers repository
 * beans for interfaces that extend JpaRepository, the @Repository annotation is not required.
 *
 */

public interface MovieRepository  extends JpaRepository<Movie, Long> {

    List<Movie> findByTitleContainingIgnoreCase(String title);


    List<Movie> findByTitleAndReleaseYear(String title, Integer releaseYear);

    List<Movie> findByReleaseYear(Integer releaseYear);

}
