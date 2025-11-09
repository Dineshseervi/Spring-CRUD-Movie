package com.real.interview.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.real.interview.entity.Movie;
import com.real.interview.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the MovieController using MockMvc and @WebMvcTest.
 * This test only loads the web layer context, isolating it from the service and persistence layers.
 */
@WebMvcTest(MovieController.class)
public class MovieControllerTwoTest {

    // MockMvc is used to simulate HTTP requests to the controller
    @Autowired
    private MockMvc mockMvc;

    // ObjectMapper is used for converting Java objects to JSON strings and vice-versa
    @Autowired
    private ObjectMapper objectMapper;

    // The service layer is mocked to prevent database calls
    @MockBean
    private MovieService movieService;

    // Helper method to create a sample movie object
    private Movie createMovie(Long id, String title, Integer year) {
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setReleaseYear(year);
        movie.setGenre("Action");
        movie.setRating(8.5);
        return movie;
    }

    // --- CRUD Operation Tests ---

    @Test
    void createMovie_ShouldReturn201Created() throws Exception {
        Movie inputMovie = createMovie(null, "Inception", 2010);
        Movie savedMovie = createMovie(1L, "Inception", 2010);

        // Given: The service returns a movie with an ID upon saving
        when(movieService.save(any(Movie.class))).thenReturn(savedMovie);
        // When/Then: Perform POST and expect 201 status and correct body content
        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputMovie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Inception")));

        // Verify that the service method was called once
        verify(movieService, times(1)).save(any(Movie.class));
    }

    @Test
    void createMovie201Created() throws Exception {
        Movie inputMovie=createMovie(null,"ABC",2025);
        Movie outMovie=createMovie(1L,"ABC",2025);
        when(movieService.save(any(Movie.class))).thenReturn(outMovie);

        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputMovie))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",is(1)))
                .andExpect(jsonPath("$.title",is("ABC")));
    }

    /**
     * Test case for the GET /api/movies endpoint with pagination.
     * It verifies that the controller correctly processes pageable parameters,
     * delegates to the service, and returns the paginated results with 200 OK.
     */
    @Test
    void getAllMovies_ShouldReturnPageOfMovies() throws Exception {
        // 1. Setup Mock Data
        List<Movie> movieList = Arrays.asList(
                createMovie(10L, "Movie A", 2020),
                createMovie(11L, "Movie B", 2021),
                createMovie(12L, "Movie C", 2022)
        );

        // Define the pagination parameters we expect the controller to receive (page=0, size=3, sorted by title ASC)
        int page = 0;
        int size = 3;
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        // Create the mock Page result
        long totalElements = 5; // Total elements in the DB, spanning multiple pages
        Page<Movie> moviesPage = new PageImpl<>(movieList, pageable, totalElements);


        // 2. Mock Service Call
        // We tell Mockito that when findAll is called with *any* Pageable object, return our mock page.
        when(movieService.findAll(any(Pageable.class))).thenReturn(moviesPage);

        // 3. Perform Request & 4. Assert Results
        // Send a GET request with page and size parameters
        mockMvc.perform(get("/api/movies")
                        .param("page", String.valueOf(page)) // page=0
                        .param("size", String.valueOf(size)) // size=3
                        .param("sort", "title,asc") // sort by title ascending
                        .contentType(MediaType.APPLICATION_JSON))

                // 4.1. Assert HTTP Status
                .andExpect(status().isOk())

                // 4.2. Assert Content and Pagination Metadata
                // Check if the content array has the correct size
                .andExpect(jsonPath("$.content", hasSize(movieList.size()))) // Should have 3 movies
                // Check pagination metadata fields
                .andExpect(jsonPath("$.totalElements", is((int) totalElements))) // Total elements in DB
                .andExpect(jsonPath("$.totalPages", is(2))) // totalElements 5, size 3 -> 2 pages
                .andExpect(jsonPath("$.number", is(page))) // Current page number (0)
                .andExpect(jsonPath("$.size", is(size))) // Page size (3)
                // Check a specific element's detail
                .andExpect(jsonPath("$.content[0].title", is("Movie A")))
                .andExpect(jsonPath("$.content[2].releaseYear", is(2022)))

                // 4.3. Assert Content Type
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // 5. Verify Service Interaction
        // Verify that the service method was called once with a Pageable object
        verify(movieService, times(1)).findAll(any(Pageable.class));
    }

//    @Test
//    void getAllMovie_ShouldReturn200AndListOfMovies() throws Exception {
//        Movie movie1 = createMovie(1L, "Parasite", 2019);
//        Movie movie2 = createMovie(2L, "Joker", 2019);
//        List<Movie> allMovies = Arrays.asList(movie1, movie2);
//
//        // Given: The service returns a list of movies
//        when(movieService.findAll()).thenReturn(allMovies);
//
//        // When/Then: Perform GET and expect 200 status and list size
//        mockMvc.perform(get("/api/movies"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].title", is("Parasite")));
//
//        verify(movieService, times(1)).findAll();
//    }

    @Test
    void getAllMovie_WithPaginationAndSorting_ReturnsPagedResult() throws Exception {
        // Prepare page content (2 items on page 0) and total 3 items overall
        List<Movie> pageContent = Arrays.asList(
                createMovie(1L, "Alpha", 2000),
                createMovie(2L, "Beta", 2001)
        );
        PageImpl<Movie> page = new PageImpl<>(
                pageContent,
                PageRequest.of(0, 2, Sort.by("title").ascending()),
                3L
        );

        // Mock service to return the page when any Pageable is provided
        when(movieService.findAll(any())).thenReturn(page);

        // Call endpoint with pageable params and sorting
        mockMvc.perform(get("/api/movies")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "title,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Assert page JSON structure: content array size and first item's title
                .andExpect(jsonPath("$.content", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$.content[0].title", org.hamcrest.Matchers.is("Alpha")))
                // totalElements should match mocked total (3)
                .andExpect(jsonPath("$.totalElements", org.hamcrest.Matchers.is(3)));
    }

    @Test
    void getMovieForId_ShouldReturn200AndMovie_WhenFound() throws Exception {
        Long movieId = 10L;
        Movie movie = createMovie(movieId, "Pulp Fiction", 1994);

        // Given: The service returns the specific movie
        when(movieService.findById(movieId)).thenReturn(movie);

        // When/Then: Perform GET and expect 200 status and the correct movie title
        mockMvc.perform(get("/api/movies/{movieId}", movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.title", is("Pulp Fiction")));

        verify(movieService, times(1)).findById(movieId);
    }

    @Test
    void getMovieForId_ShouldReturn200AndNullBody_WhenNotFound() throws Exception {
        Long movieId = 99L;

        // Given: The service returns null (as per your controller's logic: findById returns Movie, not Optional)
        when(movieService.findById(movieId)).thenReturn(null);

        // When/Then: Perform GET and expect 200 OK status but an empty/null body
        mockMvc.perform(get("/api/movies/{movieId}", movieId))
                .andExpect(status().isOk())
                .andExpect(content().string("")); // Content is empty string for a null Movie object

        verify(movieService, times(1)).findById(movieId);
    }

    // --- Search Endpoint Test ---

    @Test
    void searchMovie_ShouldReturn200AndMovieList_WhenFound() throws Exception {
        String title = "Matrix";
        Integer year = 1999;
        Movie movie = createMovie(3L, title, year);
        List<Movie> foundMovies = Arrays.asList(movie);

        // Given: The service returns a list of movies
        when(movieService.findByTitleAndReleaseYear(title, year)).thenReturn(foundMovies);

        // When/Then: Perform GET /search and expect 200 OK
        mockMvc.perform(get("/api/movies/search")
                        .param("title", title)
                        .param("year", String.valueOf(year)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(title)));

        verify(movieService, times(1)).findByTitleAndReleaseYear(title, year);
    }

    @Test
    void searchMovie_ShouldReturn404NotFound_WhenNoMoviesFound() throws Exception {
        String title = "Missing";
        Integer year = 3000;

        // Given: The service returns null (to test the ResponseEntity.of(Optional.ofNullable(movies)) behavior)
        when(movieService.findByTitleAndReleaseYear(title, year)).thenReturn(null);

        // When/Then: Perform GET /search and expect 404 Not Found due to ResponseEntity.of(Optional.ofNullable(null))
        mockMvc.perform(get("/api/movies/search")
                        .param("title", title)
                        .param("year", String.valueOf(year)))
                .andExpect(status().isNotFound());

        verify(movieService, times(1)).findByTitleAndReleaseYear(title, year);
    }

    // --- Delete Endpoint Tests ---

    @Test
    void deleteMovie_ShouldReturn204NoContent_WhenDeleted() throws Exception {
        Long movieId = 5L;

        // Given: The service successfully deletes the movie
        when(movieService.deleteById(movieId)).thenReturn(true);

        // When/Then: Perform DELETE and expect 204 No Content
        mockMvc.perform(delete("/api/movies/{movieId}", movieId))
                .andExpect(status().isNoContent())
                .andExpect(content().string("")); // Body should be empty

        verify(movieService, times(1)).deleteById(movieId);
    }

    @Test
    void deleteMovie_ShouldReturn404NotFound_WhenNotExists() throws Exception {
        Long movieId = 999L;

        // Given: The service indicates the movie was not found/deleted
        when(movieService.deleteById(movieId)).thenReturn(false);

        // When/Then: Perform DELETE and expect 404 Not Found
        mockMvc.perform(delete("/api/movies/{movieId}", movieId))
                .andExpect(status().isNotFound());

        verify(movieService, times(1)).deleteById(movieId);
    }
}


