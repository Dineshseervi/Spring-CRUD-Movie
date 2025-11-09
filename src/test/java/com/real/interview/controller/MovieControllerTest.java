package com.real.interview.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.interview.entity.Movie;
import com.real.interview.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Unit tests for the MovieController using MockMvc and @WebMvcTest.
 * This test only loads the web layer context, isolating it from the service and persistence layers.
 */
@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private Movie sampleMovie() {
        Movie m = new Movie();
        m.setId(1L);
        m.setTitle("Inception");
        m.setReleaseYear(2010);
        return m;
    }

    @Test
    void createMovie_returnsCreated() throws Exception {
        Movie movie = sampleMovie();
        when(movieService.save(any(Movie.class))).thenReturn(movie);

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(movie)));
    }

    @Test
    void getAllMovie_returnsList() throws Exception {
        Movie movie = sampleMovie();
        List<Movie> movies = Arrays.asList(movie);
        when(movieService.findAll()).thenReturn(movies);

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(movies)));
    }

    @Test
    void getMovieForId_returnsMovie() throws Exception {
        Movie movie = sampleMovie();
        when(movieService.findById(1L)).thenReturn(movie);

        mockMvc.perform(get("/api/movies/{movieId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(movie)));
    }

    @Test
    void deleteMovie_whenDeleted_returnsNoContent() throws Exception {
        when(movieService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/movies/{movieId}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMovie_whenNotFound_returnsNotFound() throws Exception {
        when(movieService.deleteById(2L)).thenReturn(false);

        mockMvc.perform(delete("/api/movies/{movieId}", 2L))
                .andExpect(status().isNotFound());
        verify(movieService, times(1)).deleteById(2L);
    }

    @Test
    void searchMovie_returnsList() throws Exception {
        Movie movie = sampleMovie();
        List<Movie> movies = Arrays.asList(movie);
        when(movieService.findByTitleAndReleaseYear(eq("Inception"), eq(2010))).thenReturn(movies);

        mockMvc.perform(get("/api/movies/search")
                        .param("title", "Inception")
                        .param("year", "2010"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(movies)));
    }
}

