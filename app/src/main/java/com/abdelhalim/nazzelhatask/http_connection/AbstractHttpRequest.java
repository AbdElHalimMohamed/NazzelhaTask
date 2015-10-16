package com.abdelhalim.nazzelhatask.http_connection;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public abstract class AbstractHttpRequest {
    protected String baseURL;

    public AbstractHttpRequest(String baseURL) {
        this.baseURL = baseURL;
    }

    protected abstract String getURL();
}