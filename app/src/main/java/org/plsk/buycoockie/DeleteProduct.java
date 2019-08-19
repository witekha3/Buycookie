package org.plsk.buycoockie;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteProduct extends AppCompatActivity {

    Products products = new Products();
    int image = R.drawable.minus_image;
    ArrayList<String> productsName ;
    ListView listView;
    Dialog deleteProductDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        deleteProductDialog = new Dialog(this);
        productsName = new ArrayList<String>();
        listView   = (ListView) findViewById(R.id.listOfItemsToDelete);

        for(Products product : products.listOfProducts){
            productsName.add(product.polishName);
        }

        AdapterToDelete adapterToDelete = new AdapterToDelete();
        listView.setAdapter(adapterToDelete);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedProdcut = (TextView) view.findViewById(R.id.nameToDeleteTxt);
                final String nameOfSelectedProduct = selectedProdcut.getText().toString();


                deleteProductDialog.setContentView(R.layout.pop_up_delete_item);
                TextView message = (TextView) deleteProductDialog.findViewById(R.id.deleteMsg);
                Button deleteBtn = (Button) deleteProductDialog.findViewById(R.id.deleteProductBtn);
                Button cancelBtn = (Button) deleteProductDialog.findViewById(R.id.donTDeleteProductBtn);

                deleteProductDialog.show();
                message.setText("Czy na pewno chcesz usunąć "+nameOfSelectedProduct+"?");
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteProductDialog.dismiss();
                    }
                });

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FileManager fm = new FileManager();
                        fm.deleteFromProductsTxt(nameOfSelectedProduct);
                        Intent intent = new Intent(DeleteProduct.this, DeleteProduct.class);
                        startActivity(intent);
                    }
                });

                //fm.deleteFromProductsTxt(nameOfSelectedProduct);
            }
        });
    }

    class AdapterToDelete extends BaseAdapter{

        @Override
        public int getCount() {
            return productsName.size();
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
            View view = getLayoutInflater().inflate(R.layout.layout_listview_to_delete, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.minusImage);
            TextView textView   = (TextView)  view.findViewById(R.id.nameToDeleteTxt);

            imageView.setImageResource(image);
            textView.setText(productsName.get(position));

            return view;
        }
    }

}
