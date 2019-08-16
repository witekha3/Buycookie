package org.plsk.buycoockie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ProductsManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_manager);

        ImageButton addProductBtn      = (ImageButton) findViewById(R.id.addProductBtn);
        ImageButton deleteProductBtn   = (ImageButton) findViewById(R.id.deleteProductBtn);
        ImageButton listOfProductsBtn  = (ImageButton) findViewById(R.id.listOfProductsBtn);

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsManager.this, AddProduct.class);
                startActivity(intent);
            }
        });

        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsManager.this, DeleteProduct.class);
                startActivity(intent);
            }
        });

        listOfProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsManager.this, ShowProducts.class);
                startActivity(intent);
            }
        });
    }
}
