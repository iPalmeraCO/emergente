package com.ipalmera.emergente.Main;

import android.app.Activity;
import android.os.Bundle;

import com.ipalmera.emergente.R;
import com.pushbots.push.Pushbots;

/**
 * Created by Roger GV on 10/10/2016.
 */
public class Loading extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        Pushbots.sharedInstance().init(this);
    }
}
