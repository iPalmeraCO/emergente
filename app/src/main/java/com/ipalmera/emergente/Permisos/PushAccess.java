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

import com.ipalmera.emergente.R;

/**
 * Created by Roger GV on 10/10/2016.
 */
public class PushAccess extends Activity {




    ImageView btn_nopush;
    ImageView btn_sipush;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notifipush);

        btn_nopush = (ImageView)findViewById(R.id.btn_nopush);
        btn_sipush = (ImageView)findViewById(R.id.btn_sipush);
        sharedpreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        btn_nopush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("push", false);
                editor.commit();
                Toast.makeText(PushAccess.this,"Notificaciones Desactivadas",Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(PushAccess.this,GpsAccess.class);
                startActivity(intent);

            }
        });

        btn_sipush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("push", true);
                editor.commit();
                Toast.makeText(PushAccess.this,"Notificaciones Activadas",Toast.LENGTH_LONG).show();
                finish();
                Intent intent = new Intent(PushAccess.this,GpsAccess.class);
                startActivity(intent);
            }
        });
    }
}
