package org.plsk.buycoockie;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.allegro.finance.tradukisto.MoneyConverters;
import pl.allegro.finance.tradukisto.ValueConverters;

public class InvoiceManager {
    ArrayList<Products> pruchasedItems = new ArrayList<Products>();
    Clients client;
    Double price;
    Font crimsonromanFont;
    String mFileName;
    String currentDateandTime;

    public InvoiceManager(ArrayList<Products> purchasedItems, Clients client, double price){
        this.pruchasedItems = purchasedItems;
        this.client = client;
        this.price = price;

        crimsonromanFont = getFontForInvoice();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyyy");
        currentDateandTime = sdf.format(new Date());
        mFileName = getInvoiceName();

    }

    public void createInvoicePdf(Context context){

        Document myDoc =  new Document();
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/org.plsk.buycoockie/invoices/"+mFileName+".pdf";

        try{
            PdfWriter.getInstance(myDoc, new FileOutputStream(mFilePath));
            myDoc.open();
/**
 *  ---------- SPLITERS
 *  ========================================================================
 */

            Paragraph splitOne = new Paragraph("============================================================");
            Paragraph splitTwo = new Paragraph("--------------------------------" +
                    "--------------------------------------------------------------------------");
            Paragraph splitThree = new Paragraph("==========================");
            Paragraph splitFour = new Paragraph("=================================================");
            Paragraph splitFive = new Paragraph("----------------------------------------------------------------------------------------");
            splitOne.setAlignment(Element.ALIGN_CENTER);
            splitTwo.setAlignment(Element.ALIGN_CENTER);
/**
 * ------------------- INVOICE TITLE
 * =================================================================================
 */
            myDoc.add(invoiceTitle());
            myDoc.add(new Paragraph(" "));


/**
 * ------------------- COMPANY ADRESS
 * =================================================================================
 */
            myDoc.add(invoiceCompanyData());
            myDoc.add(new Paragraph(" "));
/**
 * ------------------- INVOICE INFO
 * =================================================================================
 */
            myDoc.add(invoiceInfo());
            myDoc.add(new Paragraph("\n"));
/**
 * ------------------- CLIENT INFO
 * =================================================================================
 */
            myDoc.add(invoiceClientInfo());
            myDoc.add(new Paragraph("\n"));
/**
 * ------------------- ORDER INFORMATION TITLE
 * =================================================================================
 */
            myDoc.add(invoiceOrderInformationTitle());
            myDoc.add(splitOne);
/**
 * ------------------- ORDER INFORMATION DATA
 * =================================================================================
 */
            for(Products product : pruchasedItems) {
                String clientVat = "0%";
                if(client.haveVat){
                    clientVat="8%";
                }
                else{
                    clientVat="0%";
                }
                myDoc.add(invoiceOrderInformationData(product, clientVat));
                myDoc.add(splitTwo);
            }
            myDoc.add(splitOne);
            myDoc.add(new Paragraph(" "));
/**
 * ------------------- PAYMENTS METHOD TITLE
 * =================================================================================
 */
            myDoc.add(invoicePaymentsMethodTitle());
            myDoc.add(splitThree);
/**
 * ------------------- PAYMENTS METHOD DATA
 * =================================================================================
 */
            myDoc.add(invoicePaymentsMethodData());
            myDoc.add(splitThree);
            myDoc.add(new Paragraph(" "));
/**
 * ------------------- VAT TITLE
 * =================================================================================
 */
            myDoc.add(invoiceVatTitle());
            myDoc.add(splitFour);
/**
 * ------------------- VAT DATA
 * =================================================================================
 */
            myDoc.add(invoiceVatData());
            myDoc.add(splitFive);
            myDoc.add(invoiceVatData2());
            myDoc.add(splitFour);
/**
 * --------- SUM UP
 * ===========================================================================
 */
            myDoc.add(new Paragraph(" "));
            myDoc.add(invoiceSumUmPrice());

            myDoc.add(new Paragraph(" "));

            myDoc.add(invoicePersonPermision());

            myDoc.add(new Paragraph(" "));
            myDoc.add(new Paragraph(" "));
            myDoc.add(new Paragraph(" "));
            myDoc.add(new Paragraph(invoiceNotifyReception()));




            myDoc.close();
            Toast.makeText(context, "Stworzono fakturę!", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(context, "Błąd: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage() + "  xxx");

        }


    }


    private String getInvoiceName(){
        FileManager fm = new FileManager();
        fm.setDirectoryForInvoices();
        String invoiceName = "";
        if(fm.getDataFromInvoicesTxt().isEmpty()){
            invoiceName = "Wz-"+1+"-"+ Calendar.getInstance().get(Calendar.YEAR)+"-S";
            fm.saveDataToInvoiceTxt(invoiceName);
        }else{
            String lines[] = fm.getDataFromInvoicesTxt().split("[\r\n]+");
            if (lines.length != 0) {
                for (String line : lines) {
                    String tab[] = line.split("-");
                    int invoiceNr = Integer.valueOf(tab[1])+1;
                    invoiceName = "Wz-"+invoiceNr+"-"+ Calendar.getInstance().get(Calendar.YEAR)+"-S";
                    fm.saveDataToInvoiceTxt(invoiceName);
                }
            }
        }
        return invoiceName;
    }

    private Font getFontForInvoice(){

        BaseFont crimsonroman = null;
        try {
            crimsonroman = BaseFont.createFont("res/font/crimsonroman.ttf", "ISO 8859-2",BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Font(crimsonroman, 12);
    }

    private Paragraph invoiceTitle(){
        return new Paragraph(
                "Firma Cukierniczo Gastronomiczna GRZEŚ spółka z Ograniczoną " +
                        "Odpowiedzialnością Spółka Komandytowa (DAWNIEJ: Firma Cukierniczo" +
                        "Gastronoiczna 'GRZEŚ' R. Ząber G Z Krańcowa 4)", crimsonromanFont);

    }

    private PdfPTable invoiceCompanyData(){
        PdfPTable splitSideOnOne    = new PdfPTable(1);
        PdfPCell companyData   = new PdfPCell(new Phrase(
                "33-335\n" +
                        "Tel: 18-445-78-17\n" +
                        "E-mail: firmagrzes@interia.pl\n" +
                        "NIP: PL 734-327-73-19\n" +
                        "Bank: ING Bank Śląski S.A O. w Nowym Sączu\n" +
                        "7010501722100000903165025", crimsonromanFont));
        companyData.setBorder(Rectangle.NO_BORDER);
        splitSideOnOne.addCell(companyData);
        return splitSideOnOne;
    }

    private PdfPTable invoiceInfo() {
        PdfPTable splitSideOnTwo    = new PdfPTable(2);
        PdfPCell firstSplit = new PdfPCell();
        PdfPCell secondSplit     = new PdfPCell(new Phrase(
                "Faktura VAT / Dowód dostawy\n" +
                        "Nr:"+mFileName+"\n" +
                        "Data wystawienia: "+currentDateandTime+"\n" +
                        "Data realizacji: "+currentDateandTime, crimsonromanFont));
        firstSplit.setBorder(Rectangle.NO_BORDER);
        secondSplit.setBorder(Rectangle.NO_BORDER);
        splitSideOnTwo.addCell(firstSplit);
        splitSideOnTwo.addCell(secondSplit);
        return splitSideOnTwo;
    }

    private PdfPTable invoiceClientInfo(){
        PdfPTable splitSideOnTwo    = new PdfPTable(2);
        PdfPCell firstSplit = new PdfPCell(new Paragraph("Nabywca \n" +
                client.companyName+"\n"+
                client.adress+"\n"+
                client.dic+"\n"+
                "NIP: "+ client.nip+"\n"+
                "Srodek transportu: KNS 80850", crimsonromanFont));

        PdfPCell secondSplit = new PdfPCell(new Paragraph("Odbiorca\n"
                +client.companyName+"\n"+
                client.adress+"\n"+
                client.dic+"\n"+
                "NIP: "+ client.nip+"\n", crimsonromanFont));
        firstSplit.setBorder(Rectangle.NO_BORDER);
        secondSplit.setBorder(Rectangle.NO_BORDER);
        splitSideOnTwo.addCell(firstSplit);
        splitSideOnTwo.addCell(secondSplit);
        return splitSideOnTwo;
    }

    private PdfPTable invoiceOrderInformationTitle(){
        PdfPTable discribeTxt = new PdfPTable(5);
        PdfPCell nameTxt   = new PdfPCell(new Phrase("Nazwa", crimsonromanFont));
        PdfPCell amountTxt = new PdfPCell(new Phrase("Waga / Ilość", crimsonromanFont));
        PdfPCell priceTxt  = new PdfPCell(new Phrase("JM Cena", crimsonromanFont));
        PdfPCell valueTxt  = new PdfPCell(new Phrase("Wartość", crimsonromanFont));
        PdfPCell vatTxt    = new PdfPCell(new Phrase("Vat%", crimsonromanFont));
        nameTxt.setBorder(Rectangle.NO_BORDER);
        amountTxt.setBorder(Rectangle.NO_BORDER);
        priceTxt.setBorder(Rectangle.NO_BORDER);
        valueTxt.setBorder(Rectangle.NO_BORDER);
        vatTxt.setBorder(Rectangle.NO_BORDER);
        discribeTxt.addCell(nameTxt);
        discribeTxt.addCell(amountTxt);
        discribeTxt.addCell(priceTxt);
        discribeTxt.addCell(valueTxt);
        discribeTxt.addCell(vatTxt);
        return discribeTxt;
    }

    private PdfPTable invoiceOrderInformationData(Products product, String clientVat){
        PdfPTable productsTable    = new PdfPTable(6);
        PdfPCell cellOne   = new PdfPCell(new Phrase(product.polishName, crimsonromanFont));
        PdfPCell cellTwo   = new PdfPCell(new Phrase(String.valueOf(product.weight), crimsonromanFont));
        PdfPCell cellThree = new PdfPCell(new Phrase(String.valueOf(product.price), crimsonromanFont));
        PdfPCell cellFour  = new PdfPCell(new Phrase(String.valueOf(product.price*product.weight), crimsonromanFont));
        PdfPCell cellFive  = new PdfPCell(new Phrase(clientVat, crimsonromanFont));
        PdfPCell cellSix   = new PdfPCell(new Phrase(""));
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellTwo.setBorder(Rectangle.NO_BORDER);
        cellThree.setBorder(Rectangle.NO_BORDER);
        cellFour.setBorder(Rectangle.NO_BORDER);
        cellFive.setBorder(Rectangle.NO_BORDER);
        cellSix.setBorder(Rectangle.NO_BORDER);
        productsTable.addCell(cellOne);
        productsTable.addCell(cellSix);
        productsTable.addCell(cellTwo);
        productsTable.addCell(cellThree);
        productsTable.addCell(cellFour);
        productsTable.addCell(cellFive);
        return  productsTable;
    }

    private  PdfPTable invoicePaymentsMethodTitle(){
        PdfPTable paymentsMethodTable    = new PdfPTable(4);
        PdfPCell cellOne   = new PdfPCell(new Phrase("Forma Platnosci", crimsonromanFont));
        PdfPCell cellTwo   = new PdfPCell(new Phrase("Termin", crimsonromanFont));
        PdfPCell cellThree = new PdfPCell(new Phrase(" "));
        PdfPCell cellFour  = new PdfPCell(new Phrase(""));
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellTwo.setBorder(Rectangle.NO_BORDER);
        cellThree.setBorder(Rectangle.NO_BORDER);
        cellFour.setBorder(Rectangle.NO_BORDER);
        paymentsMethodTable.addCell(cellOne);
        paymentsMethodTable.addCell(cellTwo);
        paymentsMethodTable.addCell(cellThree);
        paymentsMethodTable.addCell(cellFour);
        return paymentsMethodTable;
    }

    private  PdfPTable invoicePaymentsMethodData(){
        PdfPTable paymentsMethodTable    = new PdfPTable(4);
        PdfPCell cellOne   = new PdfPCell(new Phrase("Gotowka", crimsonromanFont));
        PdfPCell cellTwo   = new PdfPCell(new Phrase("FA + 0 dni", crimsonromanFont));
        PdfPCell cellThree = new PdfPCell(new Phrase(" "));
        PdfPCell cellFour  = new PdfPCell(new Phrase(""));
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellTwo.setBorder(Rectangle.NO_BORDER);
        cellThree.setBorder(Rectangle.NO_BORDER);
        cellFour.setBorder(Rectangle.NO_BORDER);
        paymentsMethodTable.addCell(cellOne);
        paymentsMethodTable.addCell(cellTwo);
        paymentsMethodTable.addCell(cellThree);
        paymentsMethodTable.addCell(cellFour);
        return paymentsMethodTable;
    }

    private  PdfPTable invoiceVatTitle(){
        PdfPTable splitSideOnFive    = new PdfPTable(5);
        PdfPCell cellOne   = new PdfPCell(new Phrase("Stawka VAT",crimsonromanFont));
        PdfPCell cellTwo   = new PdfPCell(new Phrase("Wartosc netto",crimsonromanFont));
        PdfPCell cellThree = new PdfPCell(new Phrase("Wartosc VAT",crimsonromanFont));
        PdfPCell cellFour  = new PdfPCell(new Phrase("Wartosc brutto",crimsonromanFont));
        PdfPCell cellFive  = new PdfPCell(new Phrase(""));
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellTwo.setBorder(Rectangle.NO_BORDER);
        cellThree.setBorder(Rectangle.NO_BORDER);
        cellFour.setBorder(Rectangle.NO_BORDER);
        cellFive.setBorder(Rectangle.NO_BORDER);
        splitSideOnFive.addCell(cellOne);
        splitSideOnFive.addCell(cellTwo);
        splitSideOnFive.addCell(cellThree);
        splitSideOnFive.addCell(cellFour);
        splitSideOnFive.addCell(cellFive);
        return  splitSideOnFive;
    }

    private PdfPTable invoiceVatData(){
        PdfPTable paymentsMethodTable    = new PdfPTable(5);
        PdfPCell cellOne, cellTwo, cellThree, cellFour, cellFive;
        if(client.haveVat){
            cellOne   = new PdfPCell(new Phrase("C 8.0%",crimsonromanFont));
            cellTwo = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));
            cellThree  = new PdfPCell(new Phrase(String.format("%.2f", price*0.08),crimsonromanFont));
            cellFour  = new PdfPCell(new Phrase(String.format("%.2f", price + price*0.08), crimsonromanFont));
        }else{
            cellOne   = new PdfPCell(new Phrase("C 0.0%",crimsonromanFont));
            cellTwo  = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));
            cellThree = new PdfPCell(new Phrase("0.00",crimsonromanFont));
            cellFour  = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));

        }
        cellFive   = new PdfPCell(new Phrase(""));
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellTwo.setBorder(Rectangle.NO_BORDER);
        cellThree.setBorder(Rectangle.NO_BORDER);
        cellFour.setBorder(Rectangle.NO_BORDER);
        cellFive.setBorder(Rectangle.NO_BORDER);
        paymentsMethodTable.addCell(cellOne);
        paymentsMethodTable.addCell(cellTwo);
        paymentsMethodTable.addCell(cellThree);
        paymentsMethodTable.addCell(cellFour);
        paymentsMethodTable.addCell(cellFive);
        return paymentsMethodTable;
    }

    private PdfPTable invoiceVatData2(){
        PdfPTable paymentsMethodTable    = new PdfPTable(5);
        PdfPCell cellOne, cellTwo, cellThree, cellFour, cellFive;
        if(client.haveVat){
            cellTwo = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));
            cellThree  = new PdfPCell(new Phrase(String.format("%.2f", price*0.08),crimsonromanFont));
            cellFour  = new PdfPCell(new Phrase(String.format("%.2f", price + price*0.08), crimsonromanFont));
        }else{
            cellTwo  = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));
            cellThree = new PdfPCell(new Phrase("0.00",crimsonromanFont));
            cellFour  = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));

        }
        cellOne   = new PdfPCell(new Phrase("Razem",crimsonromanFont));
        cellFive   = new PdfPCell(new Phrase(""));
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellTwo.setBorder(Rectangle.NO_BORDER);
        cellThree.setBorder(Rectangle.NO_BORDER);
        cellFour.setBorder(Rectangle.NO_BORDER);
        cellFive.setBorder(Rectangle.NO_BORDER);
        paymentsMethodTable.addCell(cellOne);
        paymentsMethodTable.addCell(cellTwo);
        paymentsMethodTable.addCell(cellThree);
        paymentsMethodTable.addCell(cellFour);
        paymentsMethodTable.addCell(cellFive);
        return paymentsMethodTable;
    }

    private PdfPTable invoicePersonPermision(){
        PdfPTable paymentsMethodTable    = new PdfPTable(2);
        PdfPCell cellOne   = new PdfPCell(new Phrase("Uprawnienia do wystawienia dokumentu", crimsonromanFont));
        PdfPCell cellTwo   = new PdfPCell(new Phrase("Uprawnienia do odbioru dokumentu", crimsonromanFont));
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellTwo.setBorder(Rectangle.NO_BORDER);
        paymentsMethodTable.addCell(cellOne);
        paymentsMethodTable.addCell(cellTwo);
        return paymentsMethodTable;
    }

    private Paragraph invoiceSumUmPrice(){

        MoneyConverters moneyConverters = MoneyConverters.SLOVAK_BANKING_MONEY_VALUE;

        if(client.haveVat) {
            String finalPrice = String.format("%.2f", price+price*0.08);
            String moneyAsWords = moneyConverters.asWords(new BigDecimal(finalPrice));
            return new Paragraph("Razem do zaplaty: " + finalPrice +"\n"
                    +"Słownie: " + moneyAsWords, crimsonromanFont);
        }
        else{
            String moneyAsWords = moneyConverters.asWords(new BigDecimal(price));
            return new Paragraph("Razem do zapłaty: " + price + "\n"
                    +"Słownie: " + moneyAsWords, crimsonromanFont);
        }
    }

    private Paragraph invoiceNotifyReception(){
        Paragraph reception = new Paragraph("odbiór " + currentDateandTime);
        reception.setAlignment(Element.ALIGN_CENTER);
        return reception;

    }

}
