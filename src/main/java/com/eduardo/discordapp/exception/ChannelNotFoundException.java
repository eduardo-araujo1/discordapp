package com.eduardo.discordapp.exception;

public class ChannelNotFoundException extends RuntimeException{
    public ChannelNotFoundException(String message) {
        super(message);
    }
}
