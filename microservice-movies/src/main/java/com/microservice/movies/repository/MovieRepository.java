package com.microservice.movies.repository;

import com.microservice.movies.model.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {

    Page<MovieEntity> findAll(Pageable pageable);
    Page<MovieEntity> findByGenreEntityIdGenre(Integer idGenre, Pageable pageable);
    Page<MovieEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    boolean existsByIdMovie(Integer idMovie);
    MovieEntity findByIdMovie(Integer idMovie);
    boolean existsByGenreEntityIdGenre(Integer idGenre);
    boolean existsByTitleContainingIgnoreCase(String title);
    MovieEntity save(MovieEntity movieEntity);
    void deleteById(Integer idMovie);
}
