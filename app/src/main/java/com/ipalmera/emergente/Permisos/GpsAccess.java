package com.ipalmera.emergente.Permisos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.ipalmera.emergente.Main.Loading;
import com.ipalmera.emergente.R;

/**
 * Created by Roger GV on 10/10/2016.
 */
public class GpsAccess  extends Activity{

    ImageView btn_sigps;
    ImageView btn_nogps;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notifigps);

        btn_sigps = (ImageView)findViewById(R.id.btn_sigps);
        btn_nogps = (ImageView)findViewById(R.id.btn_nogps);
        sharedpreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        btn_nogps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("gps", false);
                editor.commit();
                Toast.makeText(GpsAccess.this,"Gps Desactivado",Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(GpsAccess.this,Loading.class);
                startActivity(intent);

            }
        });

        btn_sigps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("gps", true);
                editor.commit();
                Toast.makeText(GpsAccess.this,"Gps Activado",Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(GpsAccess.this,Loading.class);
                startActivity(intent);
            }
        });

    }

}
