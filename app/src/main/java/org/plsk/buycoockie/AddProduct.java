package org.plsk.buycoockie;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class AddProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        final TextView polishProductNameTxt    = (TextView) findViewById(R.id.productNameTxt);
        final TextView slovakianProductNameTxt = (TextView) findViewById(R.id.slovakianNameTxt);
        final TextView priceTxt                = (TextView) findViewById(R.id.priceTxt);
        final TextView weightTxt               = (TextView) findViewById(R.id.weightTxt);
        final TextView errorTxt                      = (TextView) findViewById(R.id.errorTxt);
        final CheckBox isSmall                       = (CheckBox) findViewById(R.id.isSmallCheckBox);
        final CheckBox isMedium                      = (CheckBox) findViewById(R.id.isMediumCheckBox);
        final CheckBox isBig                         = (CheckBox) findViewById(R.id.isBigCheckBox);
        Button   addProductBtn           = (Button)   findViewById(R.id.addNewItemBtn);

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plSize="";
                String skSize="";
                int a=0;
                int b=0;
                int c=0;
                if(isSmall.isChecked()){
                    plSize = " PORJCA";
                    skSize = " ks";
                    a = 1;
                }
                if(isMedium.isChecked()){
                    plSize = " TACKA";
                    skSize = " balený";
                    b = 1;
                }
                if(isBig.isChecked()){
                    plSize = "";
                    skSize = "luz";
                    c = 1;
                }
                if(polishProductNameTxt.getText().toString().isEmpty() ||
                slovakianProductNameTxt.getText().toString().isEmpty() ||
                weightTxt.getText().toString().isEmpty() || priceTxt.getText().toString().isEmpty() ||
                        (a+b+c)>1){
                    errorTxt.setTextColor(Color.RED);
                    errorTxt.setText("Proszę uzupełnić wszystkie pola i upewnić się że wybrany jest tylko jeden checkbox");
                    return;
                }
                errorTxt.setText("");

                String text = polishProductNameTxt.getText()+plSize+" / "+slovakianProductNameTxt.getText()+
                        skSize+" / "+weightTxt.getText()+" / "+priceTxt.getText();
                FileManager fileManager = new FileManager();

                if(!fileManager.getDataFromProductsTxt().contains(text)) {

                    fileManager.saveDataToProductsTxt(text);
                    Toast.makeText(AddProduct.this, "Pomyślnie dodano nowy produkt!", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                else{
                    Toast.makeText(AddProduct.this, "Dany produkt już istnieje!", Toast.LENGTH_SHORT).show();
                }
                polishProductNameTxt.setText("");
                slovakianProductNameTxt.setText("");
                weightTxt.setText("");
                priceTxt.setText("");
                isSmall.setChecked(false);
                isMedium.setChecked(false);
                isBig.setChecked(false);
            }
        });
    }
}
