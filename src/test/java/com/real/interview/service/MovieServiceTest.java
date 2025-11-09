package com.real.interview.service;

import com.real.interview.entity.Movie;
import com.real.interview.exception.MovieNotFoundException;
import com.real.interview.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    // Inject the mocked repository into the service instance being tested
    @InjectMocks
    private MovieService movieService;

    // Test data setup
    private Movie testMovie1;
    private Movie testMovie2;

    @BeforeEach
    void setUp() {
        // We assume the Movie entity has properties like title and releaseYear
        testMovie1 = new Movie();
        testMovie1.setId(1L);
        testMovie1.setTitle("Avatar");
        testMovie1.setReleaseYear(2009);

        testMovie2 = new Movie();
        testMovie2.setId(2L);
        testMovie2.setTitle("Titanic");
        testMovie2.setReleaseYear(1997);
    }

    @Test
    void testFindAll_ReturnsAllMovies() {
        // Arrange: Define the behavior of the mocked repository
        List<Movie> expectedMovies = Arrays.asList(testMovie1, testMovie2);
        when(movieRepository.findAll()).thenReturn(expectedMovies);

        // Act: Call the service method
        List<Movie> actualMovies = movieService.findAll();

        // Assert: Verify the result and mock interactions
        assertEquals(2, actualMovies.size());
        assertEquals("Avatar", actualMovies.get(0).getTitle());
        // Verify that the repository method was called exactly once
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testSave_SavesMovieSuccessfully() {
        // Arrange
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie1);
        // Act
        Movie savedMovie = movieService.save(testMovie1);

        // Assert
        assertNotNull(savedMovie);
        assertEquals(1L, savedMovie.getId());
        // Verify that the save method was called
        verify(movieRepository, times(1)).save(testMovie1);
    }

    @Test
    void testFindById_Found() {
        // Arrange: Note: Service takes Integer, Repository takes Long
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie1));

        // Act
        Movie foundMovie = movieService.findById(1L);

        // Assert
        assertNotNull(foundMovie);
        assertEquals("Avatar", foundMovie.getTitle());
    }

    @Test()
    void testFindById_NotFound() {
        // Arrange
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());
        // Act Exception
        MovieNotFoundException exception=assertThrows(MovieNotFoundException.class,()->movieService.findById(99L));
        // Assert
        assertEquals("Movie not found exception for id:99", exception.getMessage());
    }

    @Test
    void testFindByTitleAndReleaseYear_Found() {
        // Arrange
        List<Movie> expectedMovies = Arrays.asList(testMovie1);
        when(movieRepository.findByTitleAndReleaseYear("Avatar", 2009)).thenReturn(expectedMovies);

        // Act
        List<Movie> actualMovies = movieService.findByTitleAndReleaseYear("Avatar", 2009);

        // Assert
        assertEquals(1, actualMovies.size());
        assertEquals(2009, actualMovies.get(0).getReleaseYear());
        verify(movieRepository, times(1)).findByTitleAndReleaseYear("Avatar", 2009);
    }

    @Test
    void testFindByTitleAndReleaseYear_NotFound() {
        // Arrange
        when(movieRepository.findByTitleAndReleaseYear("Dune", 2021)).thenReturn(Collections.emptyList());
        // Act
        List<Movie> actualMovies = movieService.findByTitleAndReleaseYear("Dune", 2021);
        // Assert
        assertTrue(actualMovies.isEmpty());
    }

    @Test
    void testDeleteById_Success() {
        // Arrange: Mock the existence check to return true
        when(movieRepository.existsById(1L)).thenReturn(true);
        // We don't need to mock the deleteById return value since it's void

        // Act
        boolean result = movieService.deleteById(1L);

        // Assert
        assertTrue(result);
        // Verify that both existence check and delete method were called
        verify(movieRepository, times(1)).existsById(1L);
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange: Mock the existence check to return false
        when(movieRepository.existsById(99L)).thenReturn(false);

        // Act
        boolean result = movieService.deleteById(99L);

        // Assert
        assertFalse(result);
        // Verify that the existence check was called, but deleteById was NOT called
        verify(movieRepository, times(1)).existsById(99L);
        verify(movieRepository, never()).deleteById(anyLong());
    }
}
