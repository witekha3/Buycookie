package org.plsk.buycoockie;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShowProducts extends AppCompatActivity  {

    Products products = new Products();
    ArrayList<String> itemsArray;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        arrayList = new ArrayList<String>();
        itemsArray = new ArrayList<String >();
        ListView listView = (ListView) findViewById(R.id.productsList);
        itemsArray = new ArrayList<String >();
        for (Products product : products.listOfProducts) {
            itemsArray.add(product.polishName);
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
