package com.microservice.showtime.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowRequestDTO {
    private Long idMovie;
    private LocalDateTime showtime;
    private int totalSeats;
    private int availableSeats;
    private Long seatId;
}
