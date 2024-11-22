package com.microservice.showtime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shows")
public class ShowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_show", nullable = false)
    private Long idShow;

    @Column(name = "id_movie", nullable = false)
    private Long idMovie;

    @Column(name = "showtime", nullable = false)
    private LocalDateTime showtime;

    @Column(name = "total_seats", nullable = false)
    private int totalSeats;

    @Column(name = "available_seats", nullable = false)
    private int availableSeats;

    @ManyToOne
    @JoinColumn(name = "id_seat", referencedColumnName = "id_seat", nullable = false)
    private SeatEntity seatEntity;
}
