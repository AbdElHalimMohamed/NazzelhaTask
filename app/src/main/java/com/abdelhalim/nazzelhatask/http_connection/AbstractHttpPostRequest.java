package com.abdelhalim.nazzelhatask.http_connection;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public abstract class AbstractHttpPostRequest extends AbstractHttpRequest {

    public AbstractHttpPostRequest(String baseURL) {
        super(baseURL);
    }

    public abstract String getBodyRequestParameters();

    @Override
    protected String getURL() {
        return baseURL;
    }
}
