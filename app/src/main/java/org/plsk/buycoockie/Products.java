package org.plsk.buycoockie;

import java.util.ArrayList;

public class Products {
    ArrayList<Products> listOfProducts = new ArrayList<Products>();
    public double price;
    public double weight;
    public String polishName;
    public String slovakianName;

    public Products(String polishName, String slovakianName, double weight, double price){
        this.price = price;
        this.weight = weight;
        this.polishName = polishName;
        this.slovakianName = slovakianName;
    }

    public Products(){
        addProductFromFile();
    }

    public void addProductFromFile(){
        FileManager fileManager = new FileManager();
        String data = fileManager.getDataFromProductsTxt();
        String lines[] = data.split("[\r\n]+");
        if (lines.length != 0) {
            for (String line : lines) {
                String tab[] = line.split(" / ");
                listOfProducts.add(new Products(tab[0], tab[1], Double.valueOf(tab[2].replace(" ", "")),
                        Double.valueOf(tab[3].replace(" ", ""))));
            }
        } else {
            return;
        }
    }
}
