package com.erik.heart_club.exceptions;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
