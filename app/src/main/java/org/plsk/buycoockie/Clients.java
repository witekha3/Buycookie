package org.plsk.buycoockie;

import java.util.ArrayList;

public class Clients {
    ArrayList<Clients> listOfClients = new ArrayList<Clients>();
    public String acronim;
    public String companyName;
    public String nip;
    public String dic;
    public String adress;
    boolean haveVat;

    public Clients(){ addClientsFromFile();}

    public Clients(String acronim, String companyName, String nip,
                   String dic, String adress, boolean haveVat){
        this.acronim     = acronim;
        this.companyName = companyName;
        this.nip         = nip;
        this.dic         = dic;
        this.adress      = adress;
        this.haveVat     = haveVat;
    }

    private void addClientsFromFile() {
        FileManager fileManager = new FileManager();
        String data = fileManager.getDataFromClientsTxt();
        String lines[] = data.split("[\r\n]+");
        if (lines.length != 0) {
            for (String line : lines) {
                String tab[] = line.split(" / ");
                listOfClients.add(new Clients(tab[0], tab[1], tab[2], tab[3], tab[4], Boolean.valueOf(tab[5])));
            }
        } else {
            return;
        }
    }
}
