package com.cursosdedesarrollo.ejemploficheros07as;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("App","permiso para escribir?" + isStoragePermissionGranted());
    }

    public void crea (View v){
        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("prueba_int.txt", Context.MODE_PRIVATE));

            fout.write("Texto de prueba.");
            fout.close();
            Toast.makeText(this,"Fichero creado internamente",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }
    }
    public void lee (View v){
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("prueba_int.txt")));

            String texto = fin.readLine();
            Toast.makeText(this,texto,Toast.LENGTH_LONG).show();
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
    }
    private static final int REQUEST_WRITE_STORAGE = 112;
    public void creaExterno(View v){
        if (Build.VERSION.SDK_INT >= 23) {
            //do your check here
            compruebaPermisos();
        }else{
            escribeExterno();
        }

    }

    public void compruebaPermisos(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permisos","No tengo permisos");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Log.d("Permisos","Debería cambiar los permisos de Storage");

            } else {

                // No explanation needed, we can request the permission.
                Log.d("Permisos","Pido Permisos al usuario");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            Log.d("Permisos","El usuario me ha dado permiso");
            escribeExterno();
        }
    }
    public void escribeExterno(){
        Log.d("Permisos","Intenta escribir");
        try
        {
            File ruta_sd = Environment.getExternalStorageDirectory();

            File f = new File(ruta_sd.getAbsolutePath(), "prueba_sd.txt");

            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            new FileOutputStream(f));

            fout.write("Texto de prueba.");
            fout.close();
            Toast.makeText(this,"Fichero creado externo",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("Permisos","El usuario me da permiso: "+permissions[0]);
                    //reload my activity with permission granted or use the features what required the permission
                    compruebaPermisos();
                } else
                {
                    Log.d("Permisos","El usuario no me ha dado permisos");
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    public void leeExterno(View v){
        try
        {
            File ruta_sd = Environment.getExternalStorageDirectory();

            File f = new File(ruta_sd.getAbsolutePath(), "prueba_sd.txt");

            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(f)));

            String texto = fin.readLine();
            Toast.makeText(this,texto,Toast.LENGTH_LONG).show();
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
        }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("APP","Permission is granted");
                return true;
            } else {

                Log.v("APP","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("App","Permission is granted");
            return true;
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
