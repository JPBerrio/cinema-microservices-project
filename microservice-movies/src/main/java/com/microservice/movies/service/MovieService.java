package com.microservice.movies.service;

import com.microservice.movies.exception.GenreNotFoundException;
import com.microservice.movies.exception.InvalidMovieEntityException;
import com.microservice.movies.exception.MovieNotFoundException;
import com.microservice.movies.exception.TitleNotFoundException;
import com.microservice.movies.model.MovieEntity;
import com.microservice.movies.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    private void validatePageAndSize(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number cannot be negative and size must be greater than zero.");
        }
    }

    private void validateIdMovie(int idMovie) {
        if (idMovie <= 0) {
            throw new MovieNotFoundException("Movie ID must be greater than 0.");
        }
        if (!movieRepository.existsByIdMovie(idMovie)) {
            throw new MovieNotFoundException("The movie with ID " + idMovie + " does not exist.");
        }
    }

    private void validateTitle(String title) {
        if (!movieRepository.existsByTitleContainingIgnoreCase(title)) {
            throw new TitleNotFoundException("The title " + title + " does not exist.");
        }
    }

    private void validateIdGenre(int idGenre) {
        if (idGenre <= 0) {
            throw new GenreNotFoundException("Genre ID must be greater than 0.");
        }
        if (!movieRepository.existsByGenreEntityIdGenre(idGenre)) {
            throw new GenreNotFoundException("Genre with ID " + idGenre + " does not exist.");
        }
    }

    private void validateMovieExists(int idMovie) {
        if (!movieRepository.existsByIdMovie(idMovie)) {
            throw new MovieNotFoundException("The movie with ID " + idMovie + " does not exist.");
        }
    }

    private void validateGenreExists(int idGenre) {
        if (!movieRepository.existsByGenreEntityIdGenre(idGenre)) {
            throw new GenreNotFoundException("Genre with ID " + idGenre + " does not exist.");
        }
    }

    private void validateMovieEntity(MovieEntity movieEntity) {
        if (movieEntity == null) {
            throw new InvalidMovieEntityException("Movie entity cannot be null.");
        }

        if (movieEntity.getTitle() == null || movieEntity.getTitle().trim().isEmpty() || movieEntity.getTitle().length() > 100) {
            throw new InvalidMovieEntityException("Movie must have a valid title.");
        }

        if (movieEntity.getDescription() == null || movieEntity.getTitle().trim().isEmpty() || movieEntity.getDescription().length() > 500) {
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


    public Page<MovieEntity> findAllMovies(int page, int size) {
        validatePageAndSize(page, size);
        return movieRepository.findAll(PageRequest.of(page, size));
    }

    public MovieEntity findMovieById(int idMovie) {
        validateIdMovie(idMovie);
        return movieRepository.findByIdMovie(idMovie);
    }

    public Page<MovieEntity> findMoviesByGenre(int idGenre, int page, int size) {

        if (!movieRepository.existsByGenreEntityIdGenre(idGenre)) {
            throw new GenreNotFoundException("We did not find movies that contain the ID " + idGenre + " in the database.");
        }

        validateIdGenre(idGenre);
        validatePageAndSize(page, size);

        return movieRepository.findByGenreEntityIdGenre(idGenre, PageRequest.of(page, size));
    }

    public Page<MovieEntity> findMoviesByTitle(String title, int page, int size) {
        if (title == null || title.trim().isEmpty()) {
            throw new TitleNotFoundException("Title cannot be null or empty.");
        }

        validateTitle(title);

        validatePageAndSize(page, size);
        return movieRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size));
    }

    public boolean existsMovie(int idMovie) {
        validateIdMovie(idMovie);
        return movieRepository.existsByIdMovie(idMovie);
    }

    @Transactional
    public MovieEntity saveMovie(MovieEntity movieEntity) {
        validateMovieEntity(movieEntity);
        return movieRepository.save(movieEntity);
    }

    @Transactional
    public void deleteMovie(int idMovie) {
        validateIdMovie(idMovie);
        validateMovieExists(idMovie);
        movieRepository.deleteById(idMovie);
    }

    @Transactional
    public MovieEntity updateMovie(int idMovie, MovieEntity movieEntity) {
        validateMovieEntity(movieEntity);
        validateIdMovie(idMovie);

        return movieRepository.findById(idMovie)
                .map(existingMovie -> {
                    existingMovie.setTitle(movieEntity.getTitle());
                    existingMovie.setDescription(movieEntity.getDescription());
                    existingMovie.setDuration(movieEntity.getDuration());
                    existingMovie.setImageUrl(movieEntity.getImageUrl());
                    existingMovie.setGenreEntity(movieEntity.getGenreEntity());
                    return movieRepository.save(existingMovie);
                })
                .orElseThrow(() -> new IllegalArgumentException("Error updating the movie with id " + idMovie));
    }
}
