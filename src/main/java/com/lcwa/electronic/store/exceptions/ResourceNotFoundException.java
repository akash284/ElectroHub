package com.lcwa.electronic.store.exceptions;


// custom exception
public class ResourceNotFoundException  extends RuntimeException{

    public ResourceNotFoundException(){
        super("Resource not fund !");
    }
    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
