package com.eduardo.discordapp.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public  UserAlreadyExistsException (String message) {
        super(message);
    }
}
