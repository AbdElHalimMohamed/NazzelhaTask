package com.abdelhalim.nazzelhatask.http_connection;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public abstract class AbstractHttpGetRequest extends AbstractHttpRequest {

    public AbstractHttpGetRequest(String baseURL) {
        super(baseURL);
    }

    public abstract String getURLParameters();

    @Override
    protected String getURL() {
        return baseURL + (getURLParameters() == null ? "" : getURLParameters());
    }
}