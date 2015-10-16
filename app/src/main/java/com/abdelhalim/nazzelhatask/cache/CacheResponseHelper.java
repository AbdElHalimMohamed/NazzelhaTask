package com.abdelhalim.nazzelhatask.cache;

import android.os.AsyncTask;
import android.util.Log;

import com.abdelhalim.nazzelhatask.ApplicationSingleTone;
import com.abdelhalim.nazzelhatask.Constants;
import com.abdelhalim.nazzelhatask.data_modules.AbstractDataModule;
import com.abdelhalim.nazzelhatask.utilities.DialogUtility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by AbdEl-Halim on 10/15/2015.
 */
public class CacheResponseHelper {
    private OnReadFromCacheCompletedListener onReadFromCacheCompletedListener;
    private OnCacheDirectoryClearedListener onCacheDirectoryClearedListener;
    private File cacheDir;

    public CacheResponseHelper() {
        cacheDir = ApplicationSingleTone.getInstance().getCacheDir();
    }

    public void writToCache(final String url, final List<AbstractDataModule> dataModules, final String nextPageUrl) {
        if (dataModules == null) {
            return;
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                String cacheFileName = url.replaceAll("/|:|&|=|\\?|" + getTokenFromUrl(url), "");
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(cacheDir, cacheFileName))));
                    out.writeObject(dataModules);
                    out.writeObject(nextPageUrl);
                    out.flush();
                } catch (IOException e) {
                    DialogUtility.showErrorDialog("Caching Error", "Error writing to cache file.");
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            DialogUtility.showErrorDialog("Caching Error", "Error closing cache file.");
                        }
                    }
                }
                return null;
            }
        }.execute();
    }

    public void readFromCache(final String url) {
        new AsyncTask<Void, Void, List<AbstractDataModule>>() {
            String nextPageUrl;

            @Override
            protected List<AbstractDataModule> doInBackground(Void... params) {
                List<AbstractDataModule> dataModules = null;
                String cacheFileName = url.replaceAll("/|:|&|=|\\?|" + getTokenFromUrl(url), "");
                ObjectInputStream in = null;
                try {
                    File cacheFile = new File(cacheDir, cacheFileName);
                    if (!cacheFile.exists()) {
                        return null;
                    }
                    in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(cacheFile)));
                    dataModules = (List<AbstractDataModule>) in.readObject();
                    nextPageUrl = (String) in.readObject();
                    Log.i("Cache", "NextUrl = " + (nextPageUrl == null ? "Null" : nextPageUrl));
                } catch (IOException | ClassNotFoundException e) {
                    DialogUtility.showErrorDialog("Caching Error", "Error reading from cache file.");
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            DialogUtility.showErrorDialog("Caching Error", "Error closing cache file.");
                        }
                    }
                }
                return dataModules;
            }

            @Override
            protected void onPostExecute(List<AbstractDataModule> dataModules) {
                if (onReadFromCacheCompletedListener != null) {
                    onReadFromCacheCompletedListener.onReadCompleted(dataModules, nextPageUrl);
                }
            }
        }.execute();
    }

    public void clearCacheDirectory() {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                boolean allCleared = true;
                for (File file : cacheDir.listFiles()) {
                    allCleared &= file.delete();
                }
                return allCleared;
            }

            @Override
            protected void onPostExecute(Boolean allCleared) {
                if (onCacheDirectoryClearedListener != null) {
                    onCacheDirectoryClearedListener.onCleared(allCleared);
                }
            }
        }.execute();
    }

    public void setOnReadFromCacheCompletedListener(OnReadFromCacheCompletedListener onReadFromCacheCompletedListener) {
        this.onReadFromCacheCompletedListener = onReadFromCacheCompletedListener;
    }

    public void setOnCacheDirectoryClearedListener(OnCacheDirectoryClearedListener onCacheDirectoryClearedListener) {
        this.onCacheDirectoryClearedListener = onCacheDirectoryClearedListener;
    }

    public interface OnCacheDirectoryClearedListener {
        void onCleared(boolean allCleared);
    }

    private String getTokenFromUrl(String url) {
        String token = "";
        int startIndex = url.indexOf(Constants.ACCESS_TOKEN_KEY);
        int endIndex = url.indexOf('&', startIndex);
        if (startIndex > 0 && endIndex < 0) {
            token = url.substring(startIndex);
        } else if (startIndex > 0 && endIndex > 0) {
            token = url.substring(startIndex, endIndex);
        }
        return token;
    }

    public interface OnReadFromCacheCompletedListener {
        void onReadCompleted(List<AbstractDataModule> dataModules, String nextPageUrl);
    }
}
