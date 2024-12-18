package com.microservice.showtime.controller;

import com.microservice.showtime.dto.MovieResponseDTO;
import com.microservice.showtime.repository.SeatRepository;
import com.microservice.showtime.service.ShowService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/shows")
public class ShowController {

    private final ShowService showService;
    private final SeatRepository seatRepository;

    public ShowController(ShowService showService, SeatRepository seatRepository) {
        this.showService = showService;
        this.seatRepository = seatRepository;
    }

    @GetMapping
    public ResponseEntity<MovieResponseDTO> getMovieById(@RequestParam Long idMovie) {
        return ResponseEntity.ok(showService.getMovieById(idMovie));
    }

    @GetMapping("/movies")
    public ResponseEntity<Page<MovieResponseDTO>> getAllMovies(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int elements) {
        Page<MovieResponseDTO> movies = showService.getAllMovies(page, elements);
        return ResponseEntity.ok(movies);
    }



}
