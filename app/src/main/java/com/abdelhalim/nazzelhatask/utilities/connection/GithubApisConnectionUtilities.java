package com.abdelhalim.nazzelhatask.utilities.connection;

import android.util.Log;

import com.abdelhalim.nazzelhatask.Constants;
import com.abdelhalim.nazzelhatask.auth.Session;
import com.abdelhalim.nazzelhatask.cache.CacheResponseHelper;
import com.abdelhalim.nazzelhatask.data_modules.AbstractDataModule;
import com.abdelhalim.nazzelhatask.http_connection.AbstractHttpConnection;
import com.abdelhalim.nazzelhatask.http_connection.AbstractHttpGetRequest;
import com.abdelhalim.nazzelhatask.http_connection.HttpConnectionErrorException;
import com.abdelhalim.nazzelhatask.http_connection.HttpGetConnection;
import com.abdelhalim.nazzelhatask.http_connection.HttpRequestMismatchException;
import com.abdelhalim.nazzelhatask.http_connection.OnGetResponseListener;
import com.abdelhalim.nazzelhatask.utilities.DialogUtility;
import com.abdelhalim.nazzelhatask.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Abd El-Halim on 10/11/2015.
 */
public class GithubApisConnectionUtilities {
    private OnResponseParseCompleteListener onResponseParseCompleteListener;

    public void requestNextDataPage(final ConnectionState connectionState, final JSONParser.DataType dataType, final boolean useCache) {
        if (connectionState.getNextPageURL() == null) {
            sendFeedBack(null);
        } else if (useCache) {
            CacheResponseHelper cacheHelper = new CacheResponseHelper();
            cacheHelper.setOnReadFromCacheCompletedListener(new CacheResponseHelper.OnReadFromCacheCompletedListener() {
                @Override
                public void onReadCompleted(List<AbstractDataModule> dataModules, String nexPageUrl) {
                    if (dataModules == null) {
                        Log.i("Cache", "makeHttpConnection");
                        makeHttpConnection(connectionState, dataType, useCache);
                    } else {
                        Log.i("Cache", "dataModules != null");
                        connectionState.setNextPageURL(nexPageUrl);
                        sendFeedBack(dataModules);
                    }
                }
            });
            cacheHelper.readFromCache(connectionState.getNextPageURL());
        } else {
            makeHttpConnection(connectionState, dataType, useCache);
        }
    }

    public static String getRepositoriesUrl() {
        String url = Constants.REPOSITORIES_URL + Constants.PER_PAGE_URL_PARAMETER_KEY + "=" + Constants.PER_PAGE_ITEMS;
        if (Session.getCurrentSession().getAccessToken() != null) {
            url += ("&" + Constants.ACCESS_TOKEN_KEY + "=" + Session.getCurrentSession().getAccessToken());
        }
        return url;
    }

    public void setOnResponseParseCompleteListener(OnResponseParseCompleteListener onResponseParseCompleteListener) {
        this.onResponseParseCompleteListener = onResponseParseCompleteListener;
    }

    private void makeHttpConnection(final ConnectionState connectionState, final JSONParser.DataType dataType, final boolean useCache) {

        final AbstractHttpConnection httpConnection = new HttpGetConnection(new AbstractHttpGetRequest(connectionState.getNextPageURL()) {
            @Override
            public String getURLParameters() {
                return baseURL.contains(Constants.ACCESS_TOKEN_KEY) ? null : (Session.getCurrentSession().getAccessToken() == null ? null : "&" + Constants.ACCESS_TOKEN_KEY + "=" + Session.getCurrentSession().getAccessToken());
            }
        });
        httpConnection.setResponseListener(new OnGetResponseListener() {
            @Override
            public void onGetResponse(int responseCode, String data) {

                if (data != null) {
                    List<AbstractDataModule> dataModules = parseResponse(data, dataType);
                    sendFeedBack(dataModules);
                    String pageUrl = connectionState.getNextPageURL();
                    handleResponseHeaders(httpConnection.getRequiredResponseHeaders(), connectionState);
                    if (useCache) {
                        CacheResponseHelper cacheHelper = new CacheResponseHelper();
                        cacheHelper.writToCache(pageUrl, dataModules, connectionState.getNextPageURL());
                    }
                } else {
                    sendFeedBack(null);
                }
            }

            @Override
            public void onError(int responseCode, HttpConnectionErrorException connectionError) {
                DialogUtility.showErrorDialog("Connection Error", "Error retrieving data Github server.");
                sendFeedBack(null);
            }
        });
        try {
            httpConnection.connect();
        } catch (HttpRequestMismatchException e) {
            DialogUtility.showErrorDialog("Connection Error", "Error connecting to Github server.");
        }
    }

    private void sendFeedBack(List<AbstractDataModule> dataModules) {
        if (onResponseParseCompleteListener != null) {
            onResponseParseCompleteListener.onParseComplete(dataModules);
        }
    }

    private List<AbstractDataModule> parseResponse(String data, JSONParser.DataType dataType) {
        List<AbstractDataModule> abstractDataModules = new ArrayList<>();
        try {
            JSONArray dataJSON = new JSONArray(data);

            for (int i = 0; i < dataJSON.length(); i++) {
                JSONObject jsonObject = dataJSON.getJSONObject(i);
                abstractDataModules.add(JSONParser.parse(jsonObject, dataType));
            }
        } catch (JSONException e) {
            DialogUtility.showErrorDialog("Parsing Error", "Error parsing the data retrieved from Github server.");
        }

        return abstractDataModules;
    }

    private void handleResponseHeaders(Map<String, String> headers, ConnectionState connectionState) {
        if (headers != null) {
            Log.i("Headers", headers.get(Constants.LINK_HEADER_KEY));
            String[] links = headers.get(Constants.LINK_HEADER_KEY).split(",");
            String nextLink = null;
            for (String link : links) {
                if (link.endsWith(Constants.LINK_NEXT_KEY)) {
                    nextLink = getURLFromLink(link);
                }
            }
            if (nextLink != null) {
                connectionState.setNextPageURL(nextLink);
            } else {
                connectionState.setNextPageURL(null);
            }
        }
    }

    private String getURLFromLink(String link) {
        String url = link.split(";")[0];
        return url.substring(1, url.length() - 1);
    }


    public interface OnResponseParseCompleteListener {
        void onParseComplete(List<AbstractDataModule> dataList);
    }
}
