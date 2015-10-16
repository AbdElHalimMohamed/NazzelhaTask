package com.abdelhalim.nazzelhatask.auth;

import android.os.AsyncTask;

import com.abdelhalim.nazzelhatask.Constants;
import com.abdelhalim.nazzelhatask.data_modules.AbstractDataModule;
import com.abdelhalim.nazzelhatask.http_connection.AbstractHttpGetRequest;
import com.abdelhalim.nazzelhatask.http_connection.HttpConnectionErrorException;
import com.abdelhalim.nazzelhatask.http_connection.HttpGetConnection;
import com.abdelhalim.nazzelhatask.http_connection.HttpRequestMismatchException;
import com.abdelhalim.nazzelhatask.http_connection.OnGetResponseListener;
import com.abdelhalim.nazzelhatask.utilities.DialogUtility;
import com.abdelhalim.nazzelhatask.utilities.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abd El-Halim on 10/16/2015.
 */
public class Session {
    private String accessToken;
    private String userName;
    private static Session session;

    private Session() {

    }

    public static Session getCurrentSession() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    public String getUserName() {
        return userName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        new GetUserInfoAsyncTask().execute();
    }

    private class GetUserInfoAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpGetConnection httpGetConnection = new HttpGetConnection(new AbstractHttpGetRequest(Constants.USER_URL) {
                @Override
                public String getURLParameters() {
                    return Constants.ACCESS_TOKEN_KEY + "=" + accessToken;
                }
            });
            httpGetConnection.setResponseListener(new OnGetResponseListener() {
                @Override
                public void onGetResponse(int responseCode, String data) {
                    if (data != null) {
                        try {
                            AbstractDataModule user = JSONParser.parse(new JSONObject(data), JSONParser.DataType.USER);
                            userName = (String) user.getModuleMap().get(Constants.USER_NAME_KEY);
                        } catch (JSONException e) {
                            DialogUtility.showErrorDialog("Authentication Error","Error retrieving user info.");
                        }
                    }
                }

                @Override
                public void onError(int responseCode, HttpConnectionErrorException connectionError) {
                    DialogUtility.showErrorDialog("Authentication Error","Error retrieving user info.");
                }
            });
            try {
                httpGetConnection.connect();
            } catch (HttpRequestMismatchException e) {
                DialogUtility.showErrorDialog("Authentication Error", "Error connecting to the Authentication server.");
            }
            return null;
        }
    }
}
