package com.abdelhalim.nazzelhatask.data_modules;

import com.abdelhalim.nazzelhatask.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abd El-Halim on 10/16/2015.
 */
public class User extends AbstractDataModule {

    public User(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    @Override
    protected void parseJSONToDataMap(JSONObject jsonObject) throws JSONException {
        dataMap.put(Constants.USER_NAME_KEY, jsonObject.getString(Constants.USER_NAME_KEY));
    }
}
