package org.plsk.buycoockie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ClientsManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_manager);

        ImageButton addClientBtn    = (ImageButton) findViewById(R.id.addNewItemBtn);
        ImageButton deleteClientBtn = (ImageButton) findViewById(R.id.deleteItemBtn);
        ImageButton showItems       = (ImageButton) findViewById(R.id.listOfItemsBtn);

        addClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientsManager.this, AddClient.class);
                startActivity(intent);
            }
        });

        deleteClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientsManager.this, DeleteClient.class);
                startActivity(intent);
            }
        });

        showItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientsManager.this, ShowClients.class);
                startActivity(intent);
            }
        });

    }
}
