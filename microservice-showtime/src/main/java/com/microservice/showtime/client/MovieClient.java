package com.microservice.showtime.client;

import com.microservice.showtime.dto.MovieResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "microservice-movies", url = "localhost:8090/movies")
public interface MovieClient {

    @GetMapping("/search-movie/{idMovie}")
    MovieResponseDTO getMovieById(@PathVariable("idMovie") Long id);

    @GetMapping("all-movies")
    Page<MovieResponseDTO> getAllMovies(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int elements);
}
