package org.plsk.buycoockie;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StartUp extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_READ = 1000;
    private static final String CLIENTS_FILE_NAME = "clients.txt";
    private static final String INVOICE_FILE_NAME = "invoices.txt";
    private static final String PRODUCTS_FILE_NAME = "products.txt";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirstRun();

        storagePermission();


    }

    /**
     * -- Require permision to storage
     * ===========================================================
     */
    private void storagePermission(){

        if (ContextCompat.checkSelfPermission(StartUp.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(StartUp.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(StartUp.this, "Aby aplikacja działała poprwanie\n" +
                        "musisz dać dostęp do pamięci", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(StartUp.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ);
            }
        } else {
            firstRun();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    firstRun();
                } else {
                    Intent intent = new Intent(StartUp.this, StartUp.class);
                    startActivity(intent);
                }
                return;
            }
        }
    }

    /**
     * -- Chekcing if it is first run of our app
     * =====================================================================
     */
    private void firstRun() {
        SharedPreferences settings = this.getSharedPreferences("Buycoockie", 0);
        boolean firstrun = settings.getBoolean("firstrun", true);
        if (firstrun) {
            SharedPreferences.Editor e = settings.edit();
            e.putBoolean("firstrun", false);
            e.commit();
            SetDirectory();
            Intent home = new Intent(StartUp.this, MainActivity.class);
            startActivity(home);

        } else {
            Intent home = new Intent(StartUp.this, MainActivity.class);
            startActivity(home);
        }
    }

    /**
     * -- Select where app should save data
     * =============================================================
     */
    private void SetDirectory() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

            File txtDirectory = new File(Environment.getExternalStorageDirectory().toString()  + "/Android/data/org.plsk.buycoockie/files/");

            txtDirectory.mkdirs();// Have the object build the directory
            // structure, if needed.
            CopyAssets(); // Then run the method to copy the file.

        } else if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY)) {

        }

    }
    /**
     * -- Copy the file from the assets folder to the sdCard
     * ===========================================================
     **/
    private void CopyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        for (int i = 0; i < files.length; i++) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(files[i]);
                out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/Android/data/org.plsk.buycoockie/files/" + files[i]);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                Log.e("tag", e.getMessage());
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}




