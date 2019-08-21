package org.plsk.buycoockie;

import android.app.Dialog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class OrderManager extends AppCompatActivity {

    ArrayList<Products> selectedItemsArray = new ArrayList<Products>();
    Clients selectedClient = new Clients();

    ArrayList<String> clientsArray  = new ArrayList<String>();
    ArrayList<String> productsArray = new ArrayList<String>();
    SpinnerDialog spinnerDialogClient;
    SpinnerDialog spinnerDialogProdukt;
    Button selectClientBtn;
    Button selectProductBtn;
    SelectedItemAdapter selectedItemAdapter;

    ListView listView;
    Dialog changeProductValueDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);

        selectedItemAdapter = new SelectedItemAdapter();
        changeProductValueDialog = new Dialog(this);
        changeProductValueDialog.setContentView(R.layout.layout_change_product_value);


        listView = (ListView) findViewById(R.id.listOfPurchasedItems);
        selectClientSpinner();
        selectProductSpinner();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageButton deleteItem = (ImageButton) view.findViewById(R.id.removeItemBtn);
                final int item = position;
                deleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedItemsArray.remove(item);
                        selectedItemAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        Button createInvoiceBtn = (Button) findViewById(R.id.createInvoiceBtn);
        createInvoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InvoiceManager invoiceManager = new InvoiceManager(selectedItemsArray, selectedClient);
                invoiceManager.createInvoicePdf(getApplicationContext());
            }
        });

    }

    private void initClients() {
        Clients clients = new Clients();
        for(Clients client : clients.listOfClients){
            clientsArray.add(client.companyName + " / " + client.adress);
        }
    }

    private void initProducts(){
        Products products = new Products();
        for(Products product : products.listOfProducts){
            productsArray.add(product.polishName);
        }
    }

    private void selectClientSpinner(){
        selectClientBtn = (Button) findViewById(R.id.selectClientBtn);
        initClients();
        spinnerDialogClient =  new SpinnerDialog(OrderManager.this, clientsArray, "Wybierz klienta");
        spinnerDialogClient.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                Toast.makeText(OrderManager.this, "Wybrano: " + item, Toast.LENGTH_SHORT).show();
                selectClientBtn.setText(item);
                selectedClient = returnSelectedClient(item);
                selectedItemAdapter.notifyDataSetChanged();
            }
        });

        selectClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogClient.showSpinerDialog();
            }
        });
    }

    private void selectProductSpinner(){
        selectProductBtn = (Button) findViewById(R.id.selectProductBtn);
        initProducts();
        spinnerDialogProdukt =  new SpinnerDialog(OrderManager.this, productsArray, "Wybierz produkt");

        spinnerDialogProdukt.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                Toast.makeText(OrderManager.this, "Wybrano: " + item, Toast.LENGTH_SHORT).show();
                selectProductBtn.setText(item);
                Products products = new Products();
                for(Products product : products.listOfProducts){
                    if(product.polishName.equals(item)){
                        product = popUpToChangeValues(product);
                        selectedItemsArray.add(product);
                    }
                }
            }
        });

        selectProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogProdukt.showSpinerDialog();
            }
        });
    }

    class SelectedItemAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return selectedItemsArray.size();
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

            View view = getLayoutInflater().inflate(R.layout.layout_selected_product, null);
            Products product = selectedItemsArray.get(position);
            calculateFullPrice();

            /**
             *  --  FOR LISTVIEW
             *  ============================
             */

            TextView productNameTxt   = (TextView)  view.findViewById(R.id.productNameTxt);
            TextView amountTxt        = (TextView)  view.findViewById(R.id.amountTxt);
            TextView priceTxt         = (TextView)  view.findViewById(R.id.priceTxt);
            TextView scoreTxt         = (TextView)  view.findViewById(R.id.scoreTxt);
            TextView vatTxt           = (TextView)  view.findViewById(R.id.vatTxt);
            ImageButton removeItem    = (ImageButton) view.findViewById(R.id.removeItemBtn);



            String productName = product.polishName;
            double amount      = product.weight;
            double price       = product.price;
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            String score;

            if(selectedClient.haveVat){
                vatTxt.setText("8%VAT");
                score = String.valueOf(df.format((amount*price)*0.08+amount*price));
            }else {
                vatTxt.setText("0%VAT");
                score = String.valueOf(df.format(amount*price));
            }


            productNameTxt.setText(productName);
            amountTxt.setText(String.valueOf(amount));
            priceTxt.setText(String.valueOf(price));
            scoreTxt.setText(score);

            return view;
        }
    }

    private Products popUpToChangeValues(final Products product){
        final Products changedProduct = new Products();

        /**
         * -- POP UP MENU FOR CHANGE VALUE
         * ===============================
         */

        Button popUpAddBtn             = changeProductValueDialog.findViewById(R.id.addBtn);
        ImageButton popUpCancelBtn     = changeProductValueDialog.findViewById(R.id.cancelBtn);
        TextView popUpWeightTxt        = changeProductValueDialog.findViewById(R.id.weightTxt);
        TextView popUpPriceTxt         = changeProductValueDialog.findViewById(R.id.priceTxt);
        TextView popUpAmountTxt        = changeProductValueDialog.findViewById(R.id.amountTxt);
        TextView popUpNameOfProductTxt = changeProductValueDialog.findViewById(R.id.nameOfProductTxt);
        final EditText popUpWeight           = changeProductValueDialog.findViewById(R.id.weightPlain);
        final EditText popUpPrice            = changeProductValueDialog.findViewById(R.id.pricePlain);

        popUpWeight.setText          (String.valueOf(product.weight));
        popUpPrice.setText           (String.valueOf(product.price));
        popUpNameOfProductTxt.setText(product.polishName);
        popUpWeightTxt.setText       ("Waga/Sztuki:");
        popUpPriceTxt.setText        ("Cena:");

        popUpCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProductValueDialog.dismiss();
                return;
            }
        });

        popUpAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changedProduct.polishName    = product.polishName;
                changedProduct.slovakianName = product.slovakianName;
                changedProduct.weight        = Double.valueOf(popUpWeight.getText().toString());
                changedProduct.price         = Double.valueOf(popUpPrice.getText().toString());

                listView.setAdapter(selectedItemAdapter);
                changeProductValueDialog.dismiss();
            }
        });

        changeProductValueDialog.show();
        return changedProduct;
    }

    private Clients returnSelectedClient(String item){
        String tab[] = item.split(" / ");
        String companyName = tab[0];
        String adress = tab[1];

        Clients thisClient = new Clients();
        Clients clients = new Clients();

        for(Clients client : clients.listOfClients){
            if(client.companyName.equals(companyName) && client.adress.equals(adress)){
                thisClient = client;
            }
        }
        return thisClient;
    }

    private double[] calculateFullPrice(){
        Button addDiscount = (Button) findViewById(R.id.addDiscountBtn);
        final TextView discountTxt = (TextView) findViewById(R.id.discountTxt);
        final double[] price = {calculatePrice()};

        addDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(discountTxt.getText().toString().isEmpty()){
                    price[0] = calculatePrice();
                }
                else{
                    double discount = Double.valueOf(discountTxt.getText().toString());
                    double newPrice = calculatePrice() - discount/100*calculatePrice();
                    price[1] = calculatePrice() - discount/100*calculatePrice();
                    TextView newPriceTxt = findViewById(R.id.allPriceTxt);
                    newPriceTxt.setText("Kwota: " + newPrice + " Euro");
                }
            }
        });
        return price;
    }

    private double calculatePrice(){
        double price=0;
        for(Products p : selectedItemsArray){
            price += p.price * p.weight;
        }
        TextView priceTxt = findViewById(R.id.allPriceTxt);
        priceTxt.setText("Kwota: "+String.valueOf(price)+" Euro");
        return price;
    }

}
