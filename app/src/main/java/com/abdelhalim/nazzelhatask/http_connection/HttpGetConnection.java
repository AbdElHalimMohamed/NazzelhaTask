package com.abdelhalim.nazzelhatask.http_connection;

import com.abdelhalim.nazzelhatask.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public class HttpGetConnection extends AbstractHttpConnection {

    public HttpGetConnection(AbstractHttpRequest httpRequest) {
        super(httpRequest);
    }

    @Override
    protected String getRequestType() {
        return "GET";
    }

    @Override
    protected List<HeaderRequestParameter> getHeaderRequestParameters() {
        return null;
    }

    @Override
    protected boolean sendDataInRequestBody() {
        return false;
    }

    @Override
    protected List<String> getRequiredResponseHeaderKeys() {
        List<String> headerKeys = new ArrayList<>();
        headerKeys.add(Constants.LINK_HEADER_KEY);
        return headerKeys;
    }

    @Override
    public void connect() throws HttpRequestMismatchException {

        if (!(httpRequest instanceof AbstractHttpGetRequest)) {
            throw new HttpRequestMismatchException("Http request is not a GET request.");
        }

        new ConnectionAsyncTask().execute();
    }
}
