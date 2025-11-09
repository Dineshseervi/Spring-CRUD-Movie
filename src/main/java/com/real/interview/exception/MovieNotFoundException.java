package com.real.interview.exception;

public class MovieNotFoundException  extends RuntimeException{

    public MovieNotFoundException(Long id)
    {
        super("Movie not found exception for id:"+id);
    }
}
