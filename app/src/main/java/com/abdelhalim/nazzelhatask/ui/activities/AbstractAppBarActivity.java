package com.abdelhalim.nazzelhatask.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.abdelhalim.nazzelhatask.R;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public abstract class AbstractAppBarActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        toolbar = (Toolbar) findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);
    }

    public abstract int getContentView();
}
