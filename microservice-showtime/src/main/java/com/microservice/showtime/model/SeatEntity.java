package com.microservice.showtime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "seats")
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seat", nullable = false)
    private int idSeat;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private boolean reserved;

    @OneToMany(mappedBy = "seatEntity")
    @JsonIgnore
    private List<ShowEntity> showEntities;

}
