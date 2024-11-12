package com.microservice.movies.controller;

import com.microservice.movies.model.MovieEntity;
import com.microservice.movies.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/movies")
@Tag(name = "Movies", description = "Endpoints for movie microservice")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all-movies")
    @Operation(summary = "Get all movies",
               description = "Get all movies from the database",
               tags = {"Get"})
    public ResponseEntity<Page<MovieEntity>> getAllMovies(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int elements) {
        Page<MovieEntity> movies = movieService.findAllMovies(page, elements);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/search-movie/{idMovie}")
    @Operation(summary = "Get movie by id",
               description = "Get a movie by its id",
               tags = {"Get"})
    public ResponseEntity <MovieEntity> getMovieById(@PathVariable int idMovie) {
        return ResponseEntity.ok(movieService.findMovieById(idMovie));
    }

    @GetMapping("/search-movies-by-genre/{idGenre}")
    @Operation(summary = "Get movies by genre",
               description = "Get movies by genre id",
               tags = {"Get"})
    public ResponseEntity<Page<MovieEntity>> getMoviesByGenre(@PathVariable int idGenre,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int elements
                                                              ) {
        Page<MovieEntity> movies = movieService.findMoviesByGenre(idGenre, page, elements);

        return movies.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(movies);
    }

    @GetMapping("/search-movie-by-title")
    @Operation(summary = "Get movies by title",
               description = "Get movies by title",
               tags = {"Get"})
    public ResponseEntity<Page<MovieEntity>> getMoviesByTitle(@RequestParam String title,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int elements
                                                              ) {
        Page<MovieEntity> movies = movieService.findMoviesByTitle(title, page, elements);

        return movies.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(movies);
    }

    @PostMapping("/add-movie")
    @Operation(summary = "Add a movie",
               description = "Add a movie to the database",
               tags = {"Post"},
               requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                          description = "Movie to be added",
                          required = true,
                          content = @Content(
                                 mediaType = "application/json",
                                 schema = @Schema(implementation = MovieEntity.class)
                          )
               ),
               responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Movie added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MovieEntity.class)
                            )
                    )
               }
            )
    public ResponseEntity<MovieEntity> addMovie(@RequestBody MovieEntity movieEntity, BindingResult result) {
        MovieEntity savedMovie = movieService.saveMovie(movieEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    @PutMapping("/update-movie/{idMovie}")
    @Operation(summary = "Update a movie",
               description = "Update a movie in the database",
               tags = {"Put"},
               requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                          description = "Movie to be updated",
                          required = true,
                          content = @Content(
                                 mediaType = "application/json",
                                 schema = @Schema(implementation = MovieEntity.class)
                          )
               ),
               responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MovieEntity.class)
                            )
                    )
               }
            )
    public ResponseEntity<MovieEntity> updateMovie(@RequestBody MovieEntity movieEntity,
                                                   @PathVariable int idMovie) {
        MovieEntity updatedMovie = movieService.updateMovie(idMovie, movieEntity);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/delete-movie/{idMovie}")
    @Operation(summary = "Delete a movie",
               description = "Delete a movie from the database",
               tags = {"Delete"})
    public ResponseEntity<Void> deleteMovie(@PathVariable int idMovie) {
        movieService.deleteMovie(idMovie);
        return ResponseEntity.noContent().build();
    }
}
