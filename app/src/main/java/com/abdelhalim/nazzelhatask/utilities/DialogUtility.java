package com.abdelhalim.nazzelhatask.utilities;

import android.content.Intent;

import com.abdelhalim.nazzelhatask.ApplicationSingleTone;
import com.abdelhalim.nazzelhatask.Constants;
import com.abdelhalim.nazzelhatask.ui.activities.DialogActivity;

/**
 * Created by Abd El-Halim on 10/16/2015.
 */
public class DialogUtility {

    public static void showErrorDialog(String title, String message) {
        Intent i = new Intent(ApplicationSingleTone.getInstance(), DialogActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(Constants.DIALOG_EXTRA_TITLE, title);
        i.putExtra(Constants.DIALOG_EXTRA_MESSAGE, message);
        ApplicationSingleTone.getInstance().startActivity(i);
    }
}
