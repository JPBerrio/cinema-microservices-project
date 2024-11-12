package com.microservice.movies.exception;

public class InvalidMovieEntityException extends RuntimeException {
    public InvalidMovieEntityException(String message) {
        super(message);
    }
}
