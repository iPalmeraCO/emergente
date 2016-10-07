package com.ipalmera.emergente.DataAccess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Roger GV on 06/10/2016.
 */
public class dbHelper  extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
