package com.microservice.showtime.service;

import com.microservice.showtime.client.MovieClient;
import com.microservice.showtime.dto.MovieResponseDTO;
import com.microservice.showtime.model.SeatEntity;
import com.microservice.showtime.model.ShowEntity;
import com.microservice.showtime.repository.ShowRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShowService {

    private final MovieClient movieClient;
    private final ShowRepository showRepository;

    public ShowService(MovieClient movieClient, ShowRepository showRepository) {
        this.movieClient = movieClient;
        this.showRepository = showRepository;
    }

    /*@Transactional
    public ShowEntity createShow(ShowEntity showEntity) {
        MovieResponseDTO movie = movieClient.getMovieById(showEntity.getIdMovie());
        SeatEntity seatEntity = showEntity.getSeatEntity();
        ShowEntity savedShow = showRepository.save(showEntity);
        return savedShow;
    }*/

    public MovieResponseDTO getMovieById(Long idMovie) {
        return movieClient.getMovieById(idMovie);
    }

    public Page<MovieResponseDTO> getAllMovies(int page, int elements) {
        return movieClient.getAllMovies(page, elements);
    }
}
