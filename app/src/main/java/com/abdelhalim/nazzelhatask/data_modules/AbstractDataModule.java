package com.abdelhalim.nazzelhatask.data_modules;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abd El-Halim on 10/13/2015.
 */
public abstract class AbstractDataModule implements Serializable {

    protected Map<String, Object> dataMap;

    public AbstractDataModule(JSONObject jsonObject) throws JSONException{
        dataMap = new HashMap<>();
        parseJSONToDataMap(jsonObject);
    }

    public Map<String, Object> getModuleMap() {
        return dataMap;
    }

    protected abstract void parseJSONToDataMap(JSONObject jsonObject) throws JSONException;

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }
}
