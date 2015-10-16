package com.abdelhalim.nazzelhatask.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abdelhalim.nazzelhatask.Constants;
import com.abdelhalim.nazzelhatask.R;

/**
 * Created by Abd El-Halim on 10/16/2015.
 */
public class DialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        String title = getIntent().getStringExtra(Constants.DIALOG_EXTRA_TITLE);
        String message = getIntent().getStringExtra(Constants.DIALOG_EXTRA_MESSAGE);

        TextView titleTV = (TextView) findViewById(R.id.dialog_title_tv);
        TextView messageTv = (TextView) findViewById(R.id.dialog_message_tv);
        Button okBtn = (Button) findViewById(R.id.dialog_ok_btn);

        titleTV.setText(title);
        messageTv.setText(message);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
