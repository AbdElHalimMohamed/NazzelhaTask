package com.abdelhalim.nazzelhatask.utilities;

import com.abdelhalim.nazzelhatask.data_modules.AbstractDataModule;
import com.abdelhalim.nazzelhatask.data_modules.Repository;
import com.abdelhalim.nazzelhatask.data_modules.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abd El-Halim on 10/13/2015.
 */
public class JSONParser {

    public static AbstractDataModule parse(JSONObject jsonObject, DataType type) throws JSONException {
        AbstractDataModule abstractDataModule = null;
        switch (type) {
            case REPOSITORY:
                abstractDataModule = parseRepository(jsonObject);
                break;
            case USER:
                abstractDataModule = parseUser(jsonObject);
                break;
        }
        return abstractDataModule;
    }

    private static Repository parseRepository(JSONObject jsonObject) throws JSONException {
        return new Repository(jsonObject);
    }

    private static User parseUser(JSONObject jsonObject) throws JSONException {
        return new User(jsonObject);
    }

    public enum DataType {
        REPOSITORY, USER
    }
}
