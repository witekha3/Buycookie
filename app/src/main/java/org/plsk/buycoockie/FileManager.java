package org.plsk.buycoockie;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileManager {
    String root = Environment.getExternalStorageDirectory().getAbsolutePath();


    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }


    public String getDataFromProductsTxt() {
        String text = "";
        final File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() +"/Android/data/org.plsk.buycoockie/files/", "products");
        try {
         text = getStringFromFile(file.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("error: "+e);
        }
        return text;
    }
}
