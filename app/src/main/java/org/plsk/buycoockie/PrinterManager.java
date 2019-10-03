package org.plsk.buycoockie;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PrinterManager extends AppCompatActivity {

    ArrayAdapter adapter;
    ArrayList arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_manager);

        ListView listView = (ListView) findViewById(R.id.listOfPdfFiles);
        arrayList = new ArrayList<String>();

        for(File file : getListOfPdfFiles()){
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);
            arrayList.add(file.getName());
            adapter.notifyDataSetChanged();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                File pdfFile = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() +"/Android/data/org.plsk.buycoockie/invoices/" + arrayList.get(position));
                Uri path = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", pdfFile);
                Log.e("create pdf uri path==>", "" + path);

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                    finish();
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "There is no any PDF Viewer",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private List<File> getListOfPdfFiles() {
        List<File> pdfFilesList = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() +"/Android/data/org.plsk.buycoockie/invoices";
        File directory = new File(path);
        pdfFilesList.addAll(Arrays.asList(directory.listFiles()));
        return pdfFilesList;
    }
}
