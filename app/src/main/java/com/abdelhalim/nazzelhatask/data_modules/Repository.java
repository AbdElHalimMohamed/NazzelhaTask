package com.abdelhalim.nazzelhatask.data_modules;

import com.abdelhalim.nazzelhatask.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public class Repository extends AbstractDataModule implements Serializable {

    public Repository(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    @Override
    protected void parseJSONToDataMap(JSONObject jsonObject) throws JSONException {
        dataMap.put(Constants.REPO_NAME_KEY, jsonObject.getString(Constants.REPO_NAME_KEY));
        dataMap.put(Constants.REPO_DESC_KEY, jsonObject.getString(Constants.REPO_DESC_KEY));
        dataMap.put(Constants.REPO_OWNER_NAME_KEY, jsonObject.getJSONObject(Constants.REPO_OWNER_KEY).getString(Constants.REPO_OWNER_NAME_KEY));
        dataMap.put(Constants.REPO_HTML_URL, jsonObject.getString(Constants.REPO_HTML_URL));
        dataMap.put(Constants.REPO_USER_HTML_URL, jsonObject.getJSONObject(Constants.REPO_OWNER_KEY).getString(Constants.REPO_USER_HTML_URL));
        try {
            dataMap.put(Constants.REPO_FORK_KEY, jsonObject.getBoolean(Constants.REPO_FORK_KEY));
        } catch (JSONException e) {
            dataMap.put(Constants.REPO_FORK_KEY, true);
        }
    }
}
