package com.microservice.movies.model;

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
@Table(name = "genres")
public class GenreEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_genre", nullable = false)
        private Integer idGenre;

        @Column(name = "name_genre", nullable = false)
        private String nameGenre;

        @OneToMany(mappedBy = "genreEntity")
        @JsonIgnore
        private List<MovieEntity> movieEntities;
}
