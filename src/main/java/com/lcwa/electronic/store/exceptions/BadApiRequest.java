package com.lcwa.electronic.store.exceptions;

public class BadApiRequest  extends RuntimeException{

    public BadApiRequest(String message){
        super(message);
    }

    public BadApiRequest(){
        super("Bad request !");
    }
}
