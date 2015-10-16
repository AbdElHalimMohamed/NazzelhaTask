package com.abdelhalim.nazzelhatask.http_connection;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public class HeaderRequestParameter {
    private String key;
    private String value;

    public HeaderRequestParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
