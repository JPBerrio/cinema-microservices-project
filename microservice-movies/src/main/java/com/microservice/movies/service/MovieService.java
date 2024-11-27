package com.microservice.movies.service;

import com.microservice.movies.dto.MovieWithGenreDTO;
import com.microservice.movies.exception.GenreNotFoundException;
import com.microservice.movies.model.MovieEntity;
import com.microservice.movies.repository.MovieRepository;
import com.microservice.movies.validation.MovieValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieValidation movieValidation;

    public MovieService(MovieRepository movieRepository, MovieValidation movieValidation) {
        this.movieRepository = movieRepository;
        this.movieValidation = movieValidation;
    }

    public Page<MovieWithGenreDTO> findAllMovies(int page, int size) {
        movieValidation.validatePageAndSize(page, size);
        Page<MovieEntity> movies = movieRepository.findAll(PageRequest.of(page, size));

        return movies.map(movieEntity -> {
           MovieWithGenreDTO dto = new MovieWithGenreDTO();
           dto.setIdMovie(movieEntity.getIdMovie());
           dto.setTitle(movieEntity.getTitle());
           dto.setDescription(movieEntity.getDescription());
           dto.setDuration(movieEntity.getDuration());
           dto.setImageUrl(movieEntity.getImageUrl());
           dto.setGenreName(movieEntity.getGenreEntity().getNameGenre());
           return dto;
        });
    }

    public MovieWithGenreDTO findMovieById(int idMovie) {
        movieValidation.validateIdMovie(idMovie);
        movieValidation.validateIdMovie(idMovie);
        MovieEntity movieEntity = movieRepository.findByIdMovie(idMovie);
        MovieWithGenreDTO dto = new MovieWithGenreDTO();
        dto.setIdMovie(movieEntity.getIdMovie());
        dto.setTitle(movieEntity.getTitle());
        dto.setDescription(movieEntity.getDescription());
        dto.setDuration(movieEntity.getDuration());
        dto.setImageUrl(movieEntity.getImageUrl());
        dto.setGenreName(movieEntity.getGenreEntity().getNameGenre());
        return dto;
    }

    public Page<MovieEntity> findMoviesByGenre(int idGenre, int page, int size) {

        if (!movieRepository.existsByGenreEntityIdGenre(idGenre)) {
            throw new GenreNotFoundException("We did not find movies that contain the ID " + idGenre + " in the database.");
        }

        movieValidation.validateIdGenre(idGenre);
        movieValidation.validatePageAndSize(page, size);

        return movieRepository.findByGenreEntityIdGenre(idGenre, PageRequest.of(page, size));
    }

    public Page<MovieEntity> findMoviesByTitle(String title, int page, int size) {
        movieValidation.validateTitle(title);
        movieValidation.validatePageAndSize(page, size);
        return movieRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(page, size));
    }

    public boolean existsMovie(int idMovie) {
        movieValidation.validateIdMovie(idMovie);
        return movieRepository.existsByIdMovie(idMovie);
    }

    @Transactional
    public MovieEntity saveMovie(MovieEntity movieEntity) {
        movieValidation.validateMovieEntity(movieEntity);
        return movieRepository.save(movieEntity);
    }

    @Transactional
    public void deleteMovie(int idMovie) {
        movieValidation.validateIdMovie(idMovie);
        movieRepository.deleteById(idMovie);
    }

    @Transactional
    public MovieEntity updateMovie(int idMovie, MovieEntity movieEntity) {
        movieValidation.validateMovieEntity(movieEntity);
        movieValidation.validateIdMovie(idMovie);

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
