package com.microservice.movies.service;

import com.microservice.movies.dto.MovieWithGenreDTO;
import com.microservice.movies.exception.GenreNotFoundException;
import com.microservice.movies.exception.InvalidMovieEntityException;
import com.microservice.movies.exception.MovieNotFoundException;
import com.microservice.movies.exception.TitleNotFoundException;
import com.microservice.movies.model.GenreEntity;
import com.microservice.movies.model.MovieEntity;
import com.microservice.movies.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    //-----Tests for the findAllMovies method-----
    @Test
    void testFindAllMovies_whenPageAndSizeAreValid() {
        // Given
        int page = 0;
        int size = 10;
        List<MovieEntity> movieList = List.of(new MovieEntity(), new MovieEntity());
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MovieEntity> pageResult = new PageImpl<>(movieList, pageRequest, movieList.size());

        when(movieRepository.findAll(pageRequest)).thenReturn(pageResult);

        // When
        Page<MovieWithGenreDTO> result = movieService.findAllMovies(page, size);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(page, result.getPageable().getPageNumber());
        assertEquals(size, result.getPageable().getPageSize());
    }

    @Test
    void testFindAllMovies_whenPageIsNegative() {
        // Given
        int page = -1;
        int size = 10;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> movieService.findAllMovies(page, size));
    }

    @Test
    void testFindAllMovies_whenSizeIsZeroOrNegative() {
        // Given
        int page = 0;

        // Test when size is zero
        assertThrows(IllegalArgumentException.class, () -> movieService.findAllMovies(page, 0));

        // Test when size is negative
        assertThrows(IllegalArgumentException.class, () -> movieService.findAllMovies(page, -1));
    }

    @Test
    void testFindAllMovies_whenNoMoviesFound() {
        // Given
        int page = 0;
        int size = 10;
        Page<MovieEntity> emptyPageResult = new PageImpl<>(Collections.emptyList());

        when(movieRepository.findAll(PageRequest.of(page, size))).thenReturn(emptyPageResult);

        // When
        Page<MovieWithGenreDTO> result = movieService.findAllMovies(page, size);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllMovies_whenSizeIsZeroOrNegative_withValidPage() {
        // Given
        int page = 1;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> movieService.findAllMovies(page, 0));
        assertThrows(IllegalArgumentException.class, () -> movieService.findAllMovies(page, -1));
    }


    //-----Tests for the findMovieById method-----
    @Test
    void testGetMovieById_whenMovieExists() {
        // When
        int idMovie = 1;
        MovieEntity movie = new MovieEntity();
        movie.setIdMovie(idMovie);
        movie.setTitle("The Dark Knight");

        when(movieRepository.existsByIdMovie(idMovie)).thenReturn(true);
        when(movieRepository.findByIdMovie(idMovie)).thenReturn(movie);

        // Then
        MovieWithGenreDTO result = movieService.findMovieById(idMovie);

        // Given
        assertEquals("The Dark Knight", result.getTitle());
    }

    @Test
    void testFindMovieById_withInvalidIdThrowsException() {
        // Given
        int invalidId = 0;

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.findMovieById(invalidId));
    }

    @Test
    void testFindMovieById_whenMovieNotFoundThrowsException() {
        // Given
        int validId = 1;
        when(movieRepository.existsByIdMovie(validId)).thenReturn(false);

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.findMovieById(validId));
    }

    @Test
    void testFindMovieById_callsRepositoryMethods() {
        // Given
        int validId = 1;
        MovieEntity movie = new MovieEntity();
        movie.setIdMovie(validId);
        movie.setTitle("The Dark Knight");

        when(movieRepository.existsByIdMovie(validId)).thenReturn(true);
        when(movieRepository.findByIdMovie(validId)).thenReturn(movie);

        // When
        movieService.findMovieById(validId);

        // Then
        verify(movieRepository).existsByIdMovie(validId);
        verify(movieRepository).findByIdMovie(validId);
    }

    @Test
    void testFindMovieById_withNegativeIdThrowsException() {
        // Given
        int invalidId = -1;

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.findMovieById(invalidId));
    }


    //-----Tests for the findMoviesByGenre method-----
    @Test
    void testFindMoviesByGenre_whenGenreExistsAndPageAndSizeAreValid() {
        // Given
        int idGenre = 1;
        int page = 0;
        int size = 10;
        List<MovieEntity> movieList = List.of(new MovieEntity(), new MovieEntity());
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<MovieEntity> pageResult = new PageImpl<>(movieList, pageRequest, movieList.size());

        when(movieRepository.existsByGenreEntityIdGenre(idGenre)).thenReturn(true);
        when(movieRepository.findByGenreEntityIdGenre(idGenre, pageRequest)).thenReturn(pageResult);

        // When
        Page<MovieEntity> result = movieService.findMoviesByGenre(idGenre, page, size);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(page, result.getPageable().getPageNumber());
        assertEquals(size, result.getPageable().getPageSize());
    }

    @Test
    void testFindMoviesByGenre_whenGenreDoesNotExist() {
        // Given
        int idGenre = 1;
        int page = 0;
        int size = 10;

        when(movieRepository.existsByGenreEntityIdGenre(idGenre)).thenReturn(false);

        // When & Then
        assertThrows(GenreNotFoundException.class, () -> movieService.findMoviesByGenre(idGenre, page, size));
    }

    @Test
    void testFindMoviesByGenre_whenPageIsNegative() {
        // Given
        int idGenre = 1;
        int page = -1;
        int size = 10;

        when(movieRepository.existsByGenreEntityIdGenre(idGenre)).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> movieService.findMoviesByGenre(idGenre, page, size));
    }

    @Test
    void testFindMoviesByGenre_whenSizeIsZeroOrNegative() {
        // Given
        int idGenre = 1;
        int page = 0;

        when(movieRepository.existsByGenreEntityIdGenre(idGenre)).thenReturn(true);

        // Test when size is zero
        assertThrows(IllegalArgumentException.class, () -> movieService.findMoviesByGenre(idGenre, page, 0));

        // Test when size is negative
        assertThrows(IllegalArgumentException.class, () -> movieService.findMoviesByGenre(idGenre, page, -1));
    }

    @Test
    void testFindMoviesByGenre_whenIdGenreIsInvalid() {
        // Given
        int idGenre = -1;
        int page = 0;
        int size = 10;

        // When & Then
        assertThrows(GenreNotFoundException.class, () -> movieService.findMoviesByGenre(idGenre, page, size));
    }


    //-----Tests for the findMoviesByTitle method-----
    @Test
    void testFindMoviesByTitle_whenTitleIsNullOrEmpty() {
        // Given
        String emptyTitle = "";
        String nullTitle = null;

        // When & Then
        assertThrows(TitleNotFoundException.class, () -> movieService.findMoviesByTitle(emptyTitle, 0, 10));
        assertThrows(TitleNotFoundException.class, () -> movieService.findMoviesByTitle(nullTitle, 0, 10));
    }

    @Test
    void testFindMoviesByTitle_whenTitleIsInvalid() {
        // Given
        String invalidTitle = "a";
        int page = 0;
        int size = 10;

        // When & Then
        assertThrows(TitleNotFoundException.class, () -> movieService.findMoviesByTitle(invalidTitle, page, size));
    }

    @Test
    void testFindMoviesByTitle_whenTitleDoesNotExist() {
        // Given
        String title = "Non Existent Title";

        when(movieRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(0, 10))).thenReturn(Page.empty());

        // When & Then
        assertThrows(TitleNotFoundException.class, () -> movieService.findMoviesByTitle(title, 0, 10));
    }


    //-----Tests for the existsMovie method-----
    @Test
    void testExistsMovie_whenIdMovieIsZeroOrNegative() {
        // Given
        int invalidIdMovie = 0;

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.existsMovie(invalidIdMovie));
    }

    @Test
    void testExistsMovie_whenMovieDoesNotExist() {
        // Given
        int idMovie = 1;
        when(movieRepository.existsByIdMovie(idMovie)).thenReturn(false);

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.existsMovie(idMovie));
    }

    @Test
    void testExistsMovie_whenMovieExists() {
        // Given
        int idMovie = 1;
        when(movieRepository.existsByIdMovie(idMovie)).thenReturn(true);

        // When
        boolean result = movieService.existsMovie(idMovie);

        // Then
        assertTrue(result);
    }

    @Test
    void testExistsMovie_whenMovieDoesNotExistWithoutException() {
        // Given
        int idMovie = 100;
        when(movieRepository.existsByIdMovie(idMovie)).thenReturn(false);

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.existsMovie(idMovie));
    }


    //-----Tests for the saveMovie method-----
    @Test
    void testSaveMovie_whenMovieEntityIsNull() {
        // Given
        MovieEntity nullMovie = null;

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.saveMovie(nullMovie));
    }

    @Test
    void testSaveMovie_whenTitleIsInvalid() {
        // Given
        MovieEntity movieWithInvalidTitle = new MovieEntity();
        movieWithInvalidTitle.setTitle("");

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.saveMovie(movieWithInvalidTitle));

        // Given another invalid title (too long)
        movieWithInvalidTitle.setTitle("A".repeat(101));

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.saveMovie(movieWithInvalidTitle));
    }

    @Test
    void testSaveMovie_whenDescriptionIsInvalid() {
        // Given
        MovieEntity movieWithInvalidDescription = new MovieEntity();
        movieWithInvalidDescription.setDescription("");

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.saveMovie(movieWithInvalidDescription));

        // Given another invalid description (too long)
        movieWithInvalidDescription.setDescription("A".repeat(501));

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.saveMovie(movieWithInvalidDescription));
    }

    @Test
    void testSaveMovie_whenDurationIsInvalid() {
        // Given
        MovieEntity movieWithInvalidDuration = new MovieEntity();
        movieWithInvalidDuration.setDuration(30);

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.saveMovie(movieWithInvalidDuration));

        // Given another invalid duration (too long)
        movieWithInvalidDuration.setDuration(200);

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.saveMovie(movieWithInvalidDuration));
    }

    @Test
    void testSaveMovie_whenImageUrlIsInvalid() {
        // Given
        MovieEntity movieWithInvalidImageUrl = new MovieEntity();
        movieWithInvalidImageUrl.setImageUrl("");

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.saveMovie(movieWithInvalidImageUrl));
    }

    @Test
    void testSaveMovie_whenGenreDoesNotExist() {
        // Given
        MovieEntity movieWithNonExistentGenre = new MovieEntity();
        GenreEntity nonExistentGenre = new GenreEntity();
        nonExistentGenre.setIdGenre(999);  // Un ID de género que no existe
        movieWithNonExistentGenre.setGenreEntity(nonExistentGenre);

        when(movieRepository.existsById(nonExistentGenre.getIdGenre())).thenReturn(false);

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.saveMovie(movieWithNonExistentGenre));
    }


    //-----Tests for the deleteMovie method-----
    @Test
    void testDeleteMovie_whenIdMovieIsZeroOrNegative() {
        // Given
        int invalidIdMovie = 0;

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovie(invalidIdMovie));
    }

    @Test
    void testDeleteMovie_whenMovieDoesNotExist() {
        // Given
        int idMovie = 1;
        when(movieRepository.existsByIdMovie(idMovie)).thenReturn(false);

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovie(idMovie));
    }

    @Test
    void testDeleteMovie_whenMovieExists() {
        // Given
        int idMovie = 1;
        MovieEntity movie = new MovieEntity();
        movie.setIdMovie(idMovie);
        movie.setTitle("Valid Movie");

        when(movieRepository.existsByIdMovie(idMovie)).thenReturn(true);

        // When
        movieService.deleteMovie(idMovie);

        // Then
        verify(movieRepository, times(1)).deleteById(idMovie);
    }


    //-----Tests for the updateMovie method-----
    @Test
    void testUpdateMovie_whenIdMovieIsZeroOrNegative() {
        // Given
        int invalidIdMovie = 0;  // ID inválido
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setTitle("Updated Title");

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.updateMovie(invalidIdMovie, movieEntity));
    }

    @Test
    void testUpdateMovie_whenMovieEntityIsInvalid() {
        // Given
        int idMovie = 1;
        MovieEntity invalidMovieEntity = new MovieEntity();
        invalidMovieEntity.setTitle("");

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.updateMovie(idMovie, invalidMovieEntity));

        invalidMovieEntity.setTitle("Valid Title");
        invalidMovieEntity.setDuration(30);

        // When & Then
        assertThrows(InvalidMovieEntityException.class, () -> movieService.updateMovie(idMovie, invalidMovieEntity));
    }

    @Test
    void testUpdateMovie_whenMovieDoesNotExist() {
        // Given
        int idMovie = 1;
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setTitle("Updated Title");
        movieEntity.setDescription("Updated Description");
        movieEntity.setDuration(120);
        movieEntity.setImageUrl("http://example.com/updated-image.jpg");

        GenreEntity genre = new GenreEntity();
        genre.setIdGenre(1);
        movieEntity.setGenreEntity(genre);

        when(movieRepository.findById(idMovie)).thenReturn(Optional.empty());
        when(movieRepository.existsByGenreEntityIdGenre(1)).thenReturn(true);

        // When & Then
        assertThrows(MovieNotFoundException.class, () -> movieService.updateMovie(idMovie, movieEntity));
    }

    @Test
    void testUpdateMovie_whenMovieExistsAndIsValid() {
        // Given
        int idMovie = 1;

        MovieEntity existingMovie = new MovieEntity();
        existingMovie.setIdMovie(idMovie);
        existingMovie.setTitle("Original Title");
        existingMovie.setDescription("Original Description");
        existingMovie.setDuration(100);
        existingMovie.setImageUrl("http://example.com/old-image.jpg");

        GenreEntity genre = new GenreEntity();
        genre.setIdGenre(1);

        MovieEntity updatedMovie = new MovieEntity();
        updatedMovie.setTitle("Updated Title");
        updatedMovie.setDescription("Updated Description");
        updatedMovie.setDuration(120);
        updatedMovie.setImageUrl("http://example.com/updated-image.jpg");
        updatedMovie.setGenreEntity(genre);

        when(movieRepository.findById(idMovie)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(updatedMovie);
        when(movieRepository.existsByIdMovie(idMovie)).thenReturn(true);
        when(movieRepository.existsByGenreEntityIdGenre(1)).thenReturn(true);

        // When
        MovieEntity result = movieService.updateMovie(idMovie, updatedMovie);

        // Then
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(120, result.getDuration());
        assertEquals("http://example.com/updated-image.jpg", result.getImageUrl());
    }
}
