package org.plsk.buycoockie;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileManager {

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

    public void saveDataToProductsTxt(String text){
            File out;
            OutputStreamWriter outStreamWriter = null;
            FileOutputStream outStream = null;

            out = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() +"/Android/data/org.plsk.buycoockie/files/", "products");
            try {
                outStream = new FileOutputStream(out, true);
                outStreamWriter = new OutputStreamWriter(outStream);;
                try {
                    outStreamWriter.append("\n"+text);
                    outStreamWriter.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }
    }

    public void deleteFromProductsTxt(String name){
        Products product = new Products();
        String lineToDelete = "";
        String data = getDataFromProductsTxt();

        for(Products p : product.listOfProducts){
            if(p.polishName.equals(name)){
                lineToDelete = p.polishName+" / "+ p.slovakianName+" / "+(int)(p.weight)+" / "+p.price;
                System.out.println(lineToDelete + " xx");

                data = data.replace(lineToDelete, "");

                /**
                 * Delete this line
                 ============================
                 */
                File out;
                OutputStreamWriter outStreamWriter = null;
                FileOutputStream outStream = null;

                out = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() +"/Android/data/org.plsk.buycoockie/files/", "products");
                try {
                    PrintWriter writer = new PrintWriter(out);
                    writer.print(data);
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
