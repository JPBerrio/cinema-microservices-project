package com.microservice.movies.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieWithGenreDTO {

    private Integer idMovie;
    private String title;
    private String description;
    private int duration;
    private String imageUrl;
    private String genreName;
}
