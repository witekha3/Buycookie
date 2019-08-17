package org.plsk.buycoockie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class AddClient extends AppCompatActivity {

    TextView acronimTxt, companyNameTxt, nipTxt, dicTxt, companyAdressTxt, message;
    CheckBox haveVatCheckBox;
    String acronim = "", companyName = "", nip = "", dic = "", companyAdress = "";
    boolean haveVat=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
    }
}
