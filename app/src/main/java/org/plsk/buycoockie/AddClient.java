package org.plsk.buycoockie;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class AddClient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        final TextView acronimTxt       = (TextView) findViewById(R.id.akronimTxt);
        final TextView companyNameTxt   = (TextView) findViewById(R.id.companyNameTxt);
        final TextView nipTxt           = (TextView) findViewById(R.id.nipTxt);
        final TextView messageTxt       = (TextView) findViewById(R.id.errorMsg);
        final TextView dicTxt           = (TextView) findViewById(R.id.dicTxt);
        final TextView companyAdressTxt = (TextView) findViewById(R.id.adressTxt);
        final CheckBox haveVatCheckBox  = (CheckBox) findViewById(R.id.have8VatCheckBox);
        final Button   saveClientBtn    = (Button)   findViewById(R.id.saveClientBtn);


        saveClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTxt.setText("");
                boolean haveVat      = haveVatCheckBox.isChecked();
                String acronim       = acronimTxt.getText().toString();
                String companyName   = companyNameTxt.getText().toString();
                String dic           = dicTxt.getText().toString();
                String nip           = nipTxt.getText().toString();
                String companyAdress = companyAdressTxt.getText().toString();

                if(acronim.isEmpty() || companyName.isEmpty() || dic.isEmpty() && nip.isEmpty() ||
                companyAdress.isEmpty()){
                    messageTxt.setTextColor(Color.RED);
                    messageTxt.setText("Proszę uzupełnić wszystkie pola!");
                    return;
                }else{
                    String newClient = acronim + " / " + companyName + " / " + nip + " / " +
                            dic + " / " + companyAdress  + " / " + haveVat;
                    FileManager fm = new FileManager();
                    fm.saveDataToClientsTxt(newClient);
                    Toast.makeText(AddClient.this, "Pomyślnie dodano nowego clienta!", Toast.LENGTH_SHORT).show();
                    acronimTxt.setText("");
                    companyNameTxt.setText("");
                    nipTxt.setText("");
                    messageTxt.setText("");
                    dicTxt.setText("");
                    companyAdressTxt.setText("");
                    haveVatCheckBox.setChecked(false);;
                }
            }
        });


    }
}
