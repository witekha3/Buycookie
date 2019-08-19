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

import java.util.ArrayList;

public class DeleteClient extends AppCompatActivity {

    Clients client = new Clients();
    int image = R.drawable.minus_image;
    ArrayList<String> clientsArray ;
    ListView listView;
    Dialog deleteProductDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        deleteProductDialog = new Dialog(this);
        clientsArray = new ArrayList<String>();
        listView   = (ListView) findViewById(R.id.listOfItemsToDelete);

        for(Clients c : client.listOfClients){
            clientsArray.add(c.companyName + " / " + c.adress);
        }

        AdapterToDelete adapterToDelete = new AdapterToDelete();
        listView.setAdapter(adapterToDelete);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedClient = (TextView) view.findViewById(R.id.itemToDelete);
                final String nameOfSelectedClient = selectedClient.getText().toString();


                deleteProductDialog.setContentView(R.layout.pop_up_delete_item);
                TextView message = (TextView) deleteProductDialog.findViewById(R.id.deleteMsg);
                Button deleteBtn = (Button) deleteProductDialog.findViewById(R.id.deleteItemBtn);
                Button cancelBtn = (Button) deleteProductDialog.findViewById(R.id.donTDeleteItemBtn);

                deleteProductDialog.show();
                message.setText("Czy na pewno chcesz usunąć "+nameOfSelectedClient+"?");

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
                        fm.deleteFromClientsTxt(nameOfSelectedClient);
                        Intent intent = new Intent(DeleteClient.this, DeleteClient.class);
                        startActivity(intent);
                    }
                });

            }
        });
    }
    class AdapterToDelete extends BaseAdapter {

        @Override
        public int getCount() {
            return clientsArray.size();
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
            TextView textView   = (TextView)  view.findViewById(R.id.itemToDelete);

            imageView.setImageResource(image);
            textView.setText(clientsArray.get(position));

            return view;
        }
    }
}
