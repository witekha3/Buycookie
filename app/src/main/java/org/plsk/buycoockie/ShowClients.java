package org.plsk.buycoockie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowClients extends AppCompatActivity {

    Clients client = new Clients();
    ArrayList<String> itemsArray;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_clients);

        arrayList = new ArrayList<String>();
        itemsArray = new ArrayList<String >();
        ListView listView = (ListView) findViewById(R.id.ListOfItems);
        itemsArray = new ArrayList<String >();
        for (Clients client : client.listOfClients) {
            itemsArray.add(client.companyName + " / " + client.adress);
        }

        for(String product : itemsArray){

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);
            arrayList.add(product);
            adapter.notifyDataSetChanged();
            System.out.println(product);
        }
    }
}
