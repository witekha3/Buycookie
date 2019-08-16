package org.plsk.buycoockie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton productsBtn = (ImageButton) findViewById(R.id.productBtn);
        ImageButton clientsBtn  = (ImageButton) findViewById(R.id.clientsBtn);
        ImageButton orderBtn  = (ImageButton) findViewById(R.id.orderBtn);
        ImageButton printerBtn    = (ImageButton) findViewById(R.id.printerBtn);

        productsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductsManager.class);
                startActivity(intent);
            }
        });

        clientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientsManager.class);
                startActivity(intent);
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderManager.class);
                startActivity(intent);
            }
        });

        printerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PrinterManager.class);
                startActivity(intent);
            }
        });

    }
}
