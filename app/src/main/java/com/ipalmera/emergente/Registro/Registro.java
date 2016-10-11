package com.ipalmera.emergente.Registro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ipalmera.emergente.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Roger GV on 10/10/2016.
 */
public class Registro extends Activity {
    ProgressDialog ringProgressDialog;
    ArrayList<HashMap<String,String>> lst_login = new ArrayList<HashMap<String,String>>();
    SharedPreferences sharedpreferences;
    String UrlServer = "http://159.203.108.98/rest/public/";
    String apikey ="$2y$10$M6cG1fk6SmUe5mrdQYM14era5Tyu75r83pyOMzbb38xHBTKSPd0bW";
    EditText txt_name;
    EditText txt_mail;
    EditText txt_pass1;
    EditText txt_pass2;
    ImageView btn_registrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        setTitle("Registro");
        txt_name = (EditText)findViewById(R.id.txt_name);
        txt_mail  = (EditText)findViewById(R.id.txt_mail);
        txt_pass1  = (EditText)findViewById(R.id.txt_pass1);
        txt_pass2  = (EditText)findViewById(R.id.txt_pass2);
        btn_registrar  = (ImageView)findViewById(R.id.btn_registrar);
        sharedpreferences = getSharedPreferences("data", Context.MODE_PRIVATE);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_pass1.getText().toString().trim().equals(txt_pass2.getText().toString().trim())){
                    if(isInternetOn()){
                        ringProgressDialog = ProgressDialog.show(Registro.this,"Registro" , "Registrando....", true);

                        Operation op = new Operation();
                        op.execute();
                    }else{
                        Toast.makeText(Registro.this,"No cuentas con Internet",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Registro.this,"No coinciden las contraseñas",Toast.LENGTH_LONG).show();
                }

            }
        });

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

            isLoginOk = getRegistro();
            return null;
        }

        // A callback method executed on UI thread, invoked after the completion of the task
        @Override
        protected void onPostExecute(Void result)
        {

            ringProgressDialog.dismiss();
            if(isLoginOk){
                Toast.makeText(Registro.this,"Registro Correcto - Inicia Sesión",Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(Registro.this,"Registro Incorrecto - Correo Ya Registado",Toast.LENGTH_LONG).show();
                txt_mail.setText("");
            }
        }

    }


    public Boolean getRegistro(){

        lst_login.clear();
        Boolean isExist = false;
        Log.i("INFO", "Recuperando datos para sincronizar ");
        String respStr = null;

        try
        {

            HttpClient httpClient = new DefaultHttpClient();
            //ESTA URL ES DIFERENTE POR CADA TIPO DE DATO

            HttpPost post = new HttpPost(UrlServer+"user?usua_nombre="+URLEncoder.encode(txt_name.getText().toString().trim())+"&usua_login="+txt_mail.getText().toString().trim()+"&email="+txt_mail.getText().toString().trim()+"&usua_password="+txt_pass1.getText().toString()+"&tipoUsuario=T");


            //get.getMethod();
            post.setHeader("content-type", "application/json; charset=UTF-8");
            post.setHeader("api-key", apikey);

            // StringEntity entity = new StringEntity(GetVirus());
            //get.setEntity(entity);
            Log.i("INFO GetLogin", "EJECUTANDO EXECUTE(get)");
            HttpResponse resp = httpClient.execute(post);
            Log.i("INFO GetLogin", "FIN DE EXECUCION");
            respStr = EntityUtils.toString(resp.getEntity());
            Log.i("INFO GetLogin", "Respuesta: " + respStr);

            if(respStr.trim().equals("true")) {
                isExist = true;
            }else{
                isExist = false;
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
