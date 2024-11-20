package com.microservice.movies.validation;

import com.microservice.movies.exception.GenreNotFoundException;
import com.microservice.movies.exception.InvalidMovieEntityException;
import com.microservice.movies.exception.MovieNotFoundException;
import com.microservice.movies.exception.TitleNotFoundException;
import com.microservice.movies.model.MovieEntity;
import com.microservice.movies.repository.MovieRepository;
import org.springframework.stereotype.Component;

@Component
public class MovieValidation {
    
    public final MovieRepository movieRepository;

    public MovieValidation(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void validatePageAndSize(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number cannot be negative and size must be greater than zero.");
        }
    }

    public void validateIdMovie(int idMovie) {
        if (idMovie <= 0) {
            throw new MovieNotFoundException("Movie ID must be greater than 0.");
        }
        if (!movieRepository.existsByIdMovie(idMovie)) {
            throw new MovieNotFoundException("The movie with ID " + idMovie + " does not exist.");
        }
    }

    public void validateTitle(String title) {
        if (!movieRepository.existsByTitleContainingIgnoreCase(title)) {
            throw new TitleNotFoundException("The title " + title + " does not exist.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new TitleNotFoundException("Title cannot be null or empty.");
        }
    }

    public void validateIdGenre(int idGenre) {
        if (idGenre <= 0) {
            throw new GenreNotFoundException("Genre ID must be greater than 0.");
        }
        if (!movieRepository.existsByGenreEntityIdGenre(idGenre)) {
            throw new GenreNotFoundException("Genre with ID " + idGenre + " does not exist.");
        }
    }

    public void validateGenreExists(int idGenre) {
        if (!movieRepository.existsByGenreEntityIdGenre(idGenre)) {
            throw new GenreNotFoundException("Genre with ID " + idGenre + " does not exist.");
        }
    }

    public void validateMovieEntity(MovieEntity movieEntity) {
        if (movieEntity == null) {
            throw new InvalidMovieEntityException("Movie entity cannot be null.");
        }

        if (movieEntity.getTitle() == null || movieEntity.getTitle().trim().isEmpty() || movieEntity.getTitle().length() > 100) {
            throw new InvalidMovieEntityException("Movie must have a valid title.");
        }

        if (movieEntity.getDescription() == null || movieEntity.getDescription().trim().isEmpty() || movieEntity.getDescription().length() > 500) {
            throw new InvalidMovieEntityException("Movie must have a valid description.");
        }

        if (movieEntity.getDuration() == null || movieEntity.getDuration() <= 45 || movieEntity.getDuration() > 180) {
            throw new InvalidMovieEntityException("The duration should be between 45 and 180 minutes.");
        }

        if (movieEntity.getImageUrl() == null || movieEntity.getImageUrl().trim().isEmpty()) {
            throw new InvalidMovieEntityException("Movie must have a valid image URL.");
        }

        validateGenreExists(movieEntity.getGenreEntity().getIdGenre());
    }
}
