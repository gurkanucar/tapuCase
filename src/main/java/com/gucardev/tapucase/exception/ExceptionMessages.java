package com.gucardev.tapucase.exception;

public enum ExceptionMessages {


    USER_NOT_FOUND("user not found!"),
    USER_ALREADY_EXISTS("user already exists!"),
    SHORT_URL_NOT_FOUND("short url not found!"),
    USER_NOT_MATCHED("you are not owner of this url!"),
    SHORT_URL_ALREADY_EXISTS("short url already exists!");

    private final String value;

    ExceptionMessages(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
