package com.abdelhalim.nazzelhatask.ui.activities;

import android.os.Bundle;
import android.util.Log;

import com.abdelhalim.nazzelhatask.Constants;
import com.abdelhalim.nazzelhatask.R;
import com.abdelhalim.nazzelhatask.auth.Session;
import com.abdelhalim.nazzelhatask.http_connection.AbstractHttpGetRequest;
import com.abdelhalim.nazzelhatask.http_connection.HttpConnectionErrorException;
import com.abdelhalim.nazzelhatask.http_connection.HttpGetConnection;
import com.abdelhalim.nazzelhatask.http_connection.HttpRequestMismatchException;
import com.abdelhalim.nazzelhatask.http_connection.OnGetResponseListener;
import com.abdelhalim.nazzelhatask.ui.customviews.WebViewWithCallback;
import com.abdelhalim.nazzelhatask.utilities.DialogUtility;

/**
 * Created by Abd El-Halim on 10/16/2015.
 */
public class AuthActivity extends AbstractAppBarActivity {

    //should be more secure, but it just for test.
    private final String CLIENT_ID = "f8dbafdc9a40fc11549b";
    private final String CLIENT_SECRET = "69036f2f695ab76099818857d3db14f907b0398b";
    private final String REDIRECT_URL = "http://nazzelhatask.com";
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebViewWithCallback webViewWithCallback = (WebViewWithCallback) findViewById(R.id.web_view);
        webViewWithCallback.setOnExecuteCallBackUrlListener(new WebViewWithCallback.OnExecuteCallBackUrlListener() {
            @Override
            public void onComplete(String response) {
                code = getParameterValue(response, Constants.CODE_KEY);
                Log.i("Code", "Code  = " + code);
                HttpGetConnection httpGetConnection = new HttpGetConnection(new AbstractHttpGetRequest(Constants.TOKEN_URL) {
                    @Override
                    public String getURLParameters() {
                        return Constants.CLIENT_ID_KEY + "=" + CLIENT_ID + "&" +
                                Constants.CODE_KEY + "=" + code + "&" +
                                Constants.CLIENT_SECRET_KEY + "=" + CLIENT_SECRET;
                    }
                });
                httpGetConnection.setResponseListener(new OnGetResponseListener() {
                    @Override
                    public void onGetResponse(int responseCode, String data) {
                        Session.getCurrentSession().setAccessToken(getParameterValue(data, Constants.ACCESS_TOKEN_KEY));
                        finish();
                    }

                    @Override
                    public void onError(int responseCode, HttpConnectionErrorException connectionError) {
                        DialogUtility.showErrorDialog("Authentication Error", "Error retrieving the Authentication code.");
                        finish();
                    }
                });
                try {
                    httpGetConnection.connect();
                } catch (HttpRequestMismatchException e) {
                    DialogUtility.showErrorDialog("Authentication Error", "Error connecting to the Authentication server.");
                }
            }

            @Override
            public void onError(int errorCode, String description, String failingUrl) {
                Log.i("Code", "ErrorCode = " + errorCode + " , Des = " + description);
                finish();
            }
        });
        webViewWithCallback.setCallBackUrl(REDIRECT_URL);
        webViewWithCallback.loadUrl(Constants.AUTH_URL + Constants.CLIENT_ID_KEY + "=" + CLIENT_ID + "&" + Constants.REDIRECT_URL_KEY + "=" + REDIRECT_URL);
    }

    private String getParameterValue(String response, String parameterName) {
        int startIndex = response.indexOf(parameterName) + parameterName.length() + 1;
        int endIndex = response.indexOf('&', startIndex);
        String value;
        if (endIndex < 0) {
            value = response.substring(startIndex);
        } else {
            value = response.substring(startIndex, endIndex);
        }
        return value;
    }

    @Override
    public int getContentView() {
        return R.layout.activity_auth;
    }
}
