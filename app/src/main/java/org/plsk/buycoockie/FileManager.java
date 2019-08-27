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


    /**
     * -- Handling with products file
     * ===================================================
     */
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

        for(Products p : product.listOfProducts) {
            if (p.polishName.equals(name)) {
                String data = getDataFromProductsTxt();

                lineToDelete = p.polishName + " / " + p.slovakianName + " / " + (int) (p.weight) + " / " + String.format("%.2f",p.price)+"\\n";

                data = data.replaceAll(lineToDelete, "");


                /**
                 * Delete this line
                 ============================
                 */
                File out;
                OutputStreamWriter outStreamWriter = null;
                FileOutputStream outStream = null;

                out = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/Android/data/org.plsk.buycoockie/files/", "products");
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


    /**
     * -- Handling with clients file
     * ===================================================
     */
    public String getDataFromClientsTxt() {
        String text = "";
        final File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() +"/Android/data/org.plsk.buycoockie/files/", "clients");
        try {
            text = getStringFromFile(file.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("error: "+e);
        }
        return text;
    }

    public void saveDataToClientsTxt(String text){
        File out;
        OutputStreamWriter outStreamWriter = null;
        FileOutputStream outStream = null;

        out = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() +"/Android/data/org.plsk.buycoockie/files/", "clients");
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

    public void deleteFromClientsTxt(String name){
        Clients client = new Clients();
        String lineToDelete = "";

        for(Clients c : client.listOfClients) {
            if ((c.companyName + " / " + c.adress).equals(name)) {
                String data = getDataFromClientsTxt();

                lineToDelete = c.acronim + " / " + c.companyName + " / " + c.nip + " / "
                        + c.dic + " / " + c.adress + " / " + c.haveVat+"\\n";

                data = data.replaceAll(lineToDelete, "");


                /**
                 * Delete this line
                 ============================
                 */
                File out;
                OutputStreamWriter outStreamWriter = null;
                FileOutputStream outStream = null;

                out = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/Android/data/org.plsk.buycoockie/files/", "clients");
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


    /**
     * -- Handling with Invoice Files
     * ===================================================
     */

    public String getDataFromInvoicesTxt(){
        String text = "";
        final File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() +"/Android/data/org.plsk.buycoockie/files/", "invoices");
        try {
            text = getStringFromFile(file.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("error: "+e);
        }
        return text;
    }

    public void saveDataToInvoiceTxt(String text){
        File out;
        OutputStreamWriter outStreamWriter = null;
        FileOutputStream outStream = null;

        out = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() +"/Android/data/org.plsk.buycoockie/files/", "invoices");
        try {
            outStream = new FileOutputStream(out, true);
            outStreamWriter = new OutputStreamWriter(outStream);
            try {
                outStreamWriter.append(text+"\n");
                outStreamWriter.flush();
                outStreamWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }

    public void setDirectoryForInvoices() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

            File txtDirectory = new File(Environment.getExternalStorageDirectory().toString()  + "/Android/data/org.plsk.buycoockie/invoices/");
            if(!txtDirectory.exists()){
                txtDirectory.mkdirs();
            }

        } else if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY)) {
        }

    }

}
