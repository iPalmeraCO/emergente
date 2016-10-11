package com.ipalmera.emergente;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipalmera.emergente.Permisos.PushAccess;
import com.ipalmera.emergente.Registro.Registro;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends Activity {

    EditText txt_correo;
    EditText txt_pass;
    ImageView btn_login;
    ImageView btn_loginfb;
    TextView txt_forgot;
    TextView txt_registr;
    ProgressDialog ringProgressDialog;
    ArrayList<HashMap<String,String>> lst_login = new ArrayList<HashMap<String,String>>();
    SharedPreferences sharedpreferences;
    String UrlServer = "http://159.203.108.98/rest/public/";
    String apikey ="$2y$10$M6cG1fk6SmUe5mrdQYM14era5Tyu75r83pyOMzbb38xHBTKSPd0bW";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        txt_correo = (EditText)findViewById(R.id.txt_correo);
        txt_pass = (EditText)findViewById(R.id.txt_pass);
        btn_login = (ImageView)findViewById(R.id.btn_login);
        btn_loginfb = (ImageView)findViewById(R.id.btn_loginfb);
        txt_forgot = (TextView)findViewById(R.id.txt_forgot);
        txt_registr = (TextView)findViewById(R.id.txt_registr);
        sharedpreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        txt_registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Registro.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInternetOn()){
                    ringProgressDialog = ProgressDialog.show(MainActivity.this,"Cargando" , "Cargando....", true);

                    Operation op = new Operation();
                    op.execute();
                }else{
                    Toast.makeText(MainActivity.this,"No cuentas con Internet",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public String md5(String s) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            byte[] digest = md.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

            System.out.println("original:" + s);
            System.out.println("digested(hex):" + sb.toString());

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public final boolean isInternetOn() {
        ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) {
            return true;
        } else if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||  connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED  ) {
            return false;
        }
        return false;

    }

    class Operation extends AsyncTask<String, Integer, Void>
    {


        Boolean isLoginOk = false;
        @Override
        protected Void doInBackground(String... params)
        {

            isLoginOk = GetLogin();
            return null;
        }

        // A callback method executed on UI thread, invoked after the completion of the task
        @Override
        protected void onPostExecute(Void result)
        {

            ringProgressDialog.dismiss();
            if(isLoginOk){
                finish();
                Intent intent = new Intent(MainActivity.this,PushAccess.class);
                startActivity(intent);

            }else{
                Toast.makeText(MainActivity.this,"Login Incorrecto",Toast.LENGTH_LONG).show();
            }
        }

    }


    public Boolean GetLogin(){

        lst_login.clear();
        Boolean isExist = false;
        Log.i("INFO", "Recuperando datos para sincronizar ");
        String respStr = null;

        try
        {

            HttpClient httpClient = new DefaultHttpClient();
            //ESTA URL ES DIFERENTE POR CADA TIPO DE DATO
           // String tet = "http://159.203.108.98/rest/public/user/"+ txt_correo.getText().toString()+"/"+md5(txt_pass.getText().toString())+"";

            HttpGet get = new HttpGet("http://159.203.108.98/rest/public/user/"+ txt_correo.getText().toString()+"/"+md5(txt_pass.getText().toString())+"");


            //get.getMethod();
            get.setHeader("content-type", "application/json; charset=UTF-8");
            get.setHeader("api-key", apikey);

            // StringEntity entity = new StringEntity(GetVirus());
            //get.setEntity(entity);
            Log.i("INFO GetLogin", "EJECUTANDO EXECUTE(get)");
            HttpResponse resp = httpClient.execute(get);
            Log.i("INFO GetLogin", "FIN DE EXECUCION");
            respStr = EntityUtils.toString(resp.getEntity());
            Log.i("INFO GetLogin", "Respuesta: " + respStr);

            if(!respStr.trim().equals("[]")) {


                isExist = true;

                String jsonstrinG[] = respStr.substring(2, respStr.length() - 2).split("\\},\\{");
                Log.i("Tamaño de GetLogin", String.valueOf(jsonstrinG.length));


                for (int x = 0; x < jsonstrinG.length; x++) {

                    JSONObject jsonObject = new JSONObject("{" + jsonstrinG[x] + "}");
                    //  JSONObject jsonObject = new JSONObject(jsonstrinG[x]);
                    Iterator keys = jsonObject.keys();
                    HashMap<String, String> map = new HashMap<String, String>();
                    ContentValues vlc = new ContentValues();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();


                        map.put(key.trim(), jsonObject.getString(key.trim()).trim());
                        vlc.put(key.trim(), jsonObject.getString(key.trim()).trim());

                    }
                    lst_login.add(map);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("id", map.get("id"));
                    editor.putString("usua_nombre", map.get("usua_nombre"));
                    editor.putString("usua_login", map.get("usua_login"));
                    editor.putString("email", map.get("email"));
                    editor.putString("usua_password", map.get("usua_password"));
                    editor.commit();

                }
            }

        }
        catch(Exception ex)
        {
            Log.e("ServicioRest","Error!", ex);

            respStr ="Error";
            isExist = false;
        }


        return isExist;
    }

}
