package com.abdelhalim.nazzelhatask.http_connection;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public abstract class AbstractHttpConnection {

    protected AbstractHttpRequest httpRequest;
    protected HttpURLConnection urlConnection;
    protected OnGetResponseListener responseListener;
    protected Map<String, String> requiredResponseHeaders;

    public AbstractHttpConnection(AbstractHttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    protected abstract String getRequestType();

    protected abstract List<HeaderRequestParameter> getHeaderRequestParameters();

    protected abstract boolean sendDataInRequestBody();

    protected abstract List<String> getRequiredResponseHeaderKeys();

    public abstract void connect() throws HttpRequestMismatchException;

    public void setResponseListener(OnGetResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public Map<String, String> getRequiredResponseHeaders() {
        return requiredResponseHeaders;
    }

    protected void addRequestHeader() {
        if (getHeaderRequestParameters() != null) {
            for (HeaderRequestParameter parameter : getHeaderRequestParameters()) {
                urlConnection.setRequestProperty(parameter.getKey(), parameter.getValue());
            }
        }
    }

    protected String getResponseMessage() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder message = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            message.append(line);
        }
        in.close();

        return message.toString();
    }

    protected class ConnectionAsyncTask extends AsyncTask<Void, Void, String> {
        private HttpConnectionErrorException connectionErrorException;
        private int responseCode;

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            try {
                urlConnection = (HttpURLConnection) new URL(httpRequest.getURL()).openConnection();
                urlConnection.setRequestMethod(getRequestType());
                addRequestHeader();
                if (sendDataInRequestBody()) {
                    urlConnection.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
                    out.write(((AbstractHttpPostRequest) httpRequest).getBodyRequestParameters().getBytes());
                    out.flush();
                    out.close();
                }
                responseCode = urlConnection.getResponseCode();
                if (getRequiredResponseHeaderKeys() != null) {
                    requiredResponseHeaders = new HashMap<>();
                    for (String headerKey : getRequiredResponseHeaderKeys()) {
                        requiredResponseHeaders.put(headerKey, urlConnection.getHeaderField(headerKey));
                    }
                }
                response = getResponseMessage();
            } catch (IOException e) {
                connectionErrorException = new HttpConnectionErrorException(e.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (responseListener != null) {
                if (connectionErrorException != null) {
                    responseListener.onError(responseCode, connectionErrorException);
                } else {
                    responseListener.onGetResponse(responseCode, response);
                }
            }
        }
    }
}