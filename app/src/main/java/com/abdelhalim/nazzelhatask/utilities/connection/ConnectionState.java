package com.abdelhalim.nazzelhatask.utilities.connection;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public class ConnectionState {
    private String nextPageURL;

    public ConnectionState(String firstPageURL) {
        nextPageURL = firstPageURL;
    }

    public String getNextPageURL() {
        return nextPageURL;
    }

    public void setNextPageURL(String nextPageURL) {
        this.nextPageURL = nextPageURL;
    }
}
