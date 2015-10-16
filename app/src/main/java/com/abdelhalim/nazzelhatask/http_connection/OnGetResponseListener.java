package com.abdelhalim.nazzelhatask.http_connection;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public interface OnGetResponseListener {
    void onGetResponse(int responseCode, String data);

    void onError(int responseCode, HttpConnectionErrorException connectionError);
}
