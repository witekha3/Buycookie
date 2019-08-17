package org.plsk.buycoockie;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeleteProduct extends AppCompatActivity {

    Products products = new Products();
    ArrayList<String> itemsArray;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    int image = R.drawable.minus_image;
    String productsNames[] = new String[products.listOfProducts.size()] ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        arrayList           = new ArrayList<String>();
        itemsArray          = new ArrayList<String >();
        ListView listView   = (ListView) findViewById(R.id.listOfItemsToDelete);

        int i=0;
        for(Products product : products.listOfProducts){
            productsNames[i] = product.polishName;
            i++;
        }

        AdapterToDelete adapterToDelete = new AdapterToDelete();
        listView.setAdapter(adapterToDelete);
    }

    class AdapterToDelete extends BaseAdapter{

        @Override
        public int getCount() {
            return productsNames.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.layout_to_delete, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.minusImage);
            TextView textView   = (TextView)  view.findViewById(R.id.nameToDeleteTxt);

            imageView.setImageResource(image);
            textView.setText(productsNames[position]);

            return view;
        }
    }

}
