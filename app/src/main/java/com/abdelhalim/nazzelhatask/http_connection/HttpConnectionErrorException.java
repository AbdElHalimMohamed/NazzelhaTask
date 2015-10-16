package com.abdelhalim.nazzelhatask.http_connection;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public class HttpConnectionErrorException extends Throwable {

    public HttpConnectionErrorException(String errorMessage) {
        super(errorMessage);
    }
}
