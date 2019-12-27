package org.plsk.buycoockie;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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

import pl.allegro.finance.tradukisto.MoneyConverters;

public class InvoiceManager {

    ArrayList<Products> pruchasedItems = new ArrayList<Products>();
    Clients client;
    Double price;
    Font crimsonromanFont;
    String mFileName;
    String currentDateandTime;

    PdfPTable table = new PdfPTable(1);

    PdfPCell cell1, cell2, cell3, cell4, cell5, cell6;

    public InvoiceManager(ArrayList<Products> purchasedItems, Clients client, double price){
        this.pruchasedItems = purchasedItems;
        this.client = client;
        this.price = price;

        crimsonromanFont = getFontForInvoice();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyyy");
        currentDateandTime = sdf.format(new Date());
        mFileName = getInvoiceName();
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
            crimsonroman = BaseFont.createFont("res/font/crimsonroman.ttf", "WINDOWS-1250",BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Font(crimsonroman, 12);
    }

    private Paragraph invoiceSumUmPrice(){

        MoneyConverters moneyConverters = MoneyConverters.POLISH_BANKING_MONEY_VALUE;

        if(client.haveVat) {
            String finalPrice = String.format("%.2f", price+price*0.08);
            //String moneyAsWords = moneyConverters.asWords(new BigDecimal(finalPrice));
            return new Paragraph("Razem do zaplaty: " + finalPrice +"\n"
                    /*+"Słownie: " + moneyAsWords, crimsonromanFont*/);
        }
        else{
         //   String moneyAsWords = moneyConverters.asWords(new BigDecimal(price));
            return new Paragraph("Razem do zapłaty: " + price + "\n"
                    /*+"Słownie: " + moneyAsWords, crimsonromanFont*/);
        }
    }


    private Paragraph invoiceTitle(){
        return new Paragraph(
                "Firma Cukierniczo Gastronomiczna GRZEŚ spółka z Ograniczoną " +
                        "Odpowiedzialnością Spółka Komandytowa (DAWNIEJ: Firma Cukierniczo" +
                        "Gastronoiczna 'GRZEŚ' R. Ząber G Z Krańcowa 4)", crimsonromanFont);
    }

    private PdfPTable invoiceCompanyData(){
        table = new PdfPTable(1);
        cell1   = new PdfPCell(new Phrase(
                "33-335\n" +
                        "Tel: 18-445-78-17\n" +
                        "E-mail: firmagrzes@interia.pl\n" +
                        "NIP: PL 734-327-73-19\n" +
                        "Bank: ING Bank Śląski S.A O. w Nowym Sączu\n" +
                        "7010501722100000903165025", crimsonromanFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        return table;
    }

    private PdfPTable invoiceInfo() {
        table = new PdfPTable(2);
        cell1 = new PdfPCell();
        cell2     = new PdfPCell(new Phrase(
                "Faktura VAT / Dowód dostawy\n" +
                        "Nr:"+mFileName+"\n" +
                        "Data wystawienia: "+currentDateandTime+"\n" +
                        "Data realizacji: "+currentDateandTime, crimsonromanFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        return table;
    }

    private PdfPTable invoiceClientInfo(){
        table = new PdfPTable(2);
        cell1 = new PdfPCell(new Paragraph("Nabywca \n" +
                client.companyName+"\n"+
                client.adress+"\n"+
                client.dic+"\n"+
                "NIP: "+ client.nip+"\n"+
                "Srodek transportu: KNS 80850", crimsonromanFont));

        cell2 = new PdfPCell(new Paragraph("Odbiorca\n"
                +client.companyName+"\n"+
                client.adress+"\n"+
                client.dic+"\n"+
                "NIP: "+ client.nip+"\n", crimsonromanFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        return table;
    }

    private PdfPTable invoiceOrderInformationTitle(){
        table = new PdfPTable(5);
        cell1  = new PdfPCell(new Phrase("Nazwa", crimsonromanFont));
        cell2  = new PdfPCell(new Phrase("Waga / Ilość", crimsonromanFont));
        cell3  = new PdfPCell(new Phrase("JM Cena", crimsonromanFont));
        cell4  = new PdfPCell(new Phrase("Wartość", crimsonromanFont));
        cell5  = new PdfPCell(new Phrase("Vat%", crimsonromanFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell4.setBorder(Rectangle.NO_BORDER);
        cell5.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        return table;
    }

    private PdfPTable invoiceOrderInformationData(Products product, String clientVat){
        table = new PdfPTable(6);
        cell1   = new PdfPCell(new Phrase(product.polishName, crimsonromanFont));
        cell2   = new PdfPCell(new Phrase(String.valueOf(product.weight), crimsonromanFont));
        cell3 = new PdfPCell(new Phrase(String.valueOf(product.price), crimsonromanFont));
        cell4  = new PdfPCell(new Phrase(String.valueOf(product.price*product.weight), crimsonromanFont));
        cell5  = new PdfPCell(new Phrase(clientVat, crimsonromanFont));
        cell6   = new PdfPCell(new Phrase(""));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell4.setBorder(Rectangle.NO_BORDER);
        cell5.setBorder(Rectangle.NO_BORDER);
        cell6.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell6);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        return  table;
    }

    private  PdfPTable invoicePaymentsMethodTitle(){
        table = new PdfPTable(4);
        cell1   = new PdfPCell(new Phrase("Forma Platnosci", crimsonromanFont));
        cell2   = new PdfPCell(new Phrase("Termin", crimsonromanFont));
        cell3 = new PdfPCell(new Phrase(" "));
        cell4  = new PdfPCell(new Phrase(""));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell4.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        return table;
    }

    private  PdfPTable invoicePaymentsMethodData(){
        table = new PdfPTable(4);
        cell1   = new PdfPCell(new Phrase("Gotowka", crimsonromanFont));
        cell2   = new PdfPCell(new Phrase("FA + 0 dni", crimsonromanFont));
        cell3 = new PdfPCell(new Phrase(" "));
        cell4  = new PdfPCell(new Phrase(""));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell4.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        return table;
    }

    private  PdfPTable invoiceVatTitle(){
        table = new PdfPTable(5);
        cell1   = new PdfPCell(new Phrase("Stawka VAT",crimsonromanFont));
        cell2   = new PdfPCell(new Phrase("Wartosc netto",crimsonromanFont));
        cell3 = new PdfPCell(new Phrase("Wartosc VAT",crimsonromanFont));
        cell4  = new PdfPCell(new Phrase("Wartosc brutto",crimsonromanFont));
        cell5  = new PdfPCell(new Phrase(""));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell4.setBorder(Rectangle.NO_BORDER);
        cell5.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        return  table;
    }

    private PdfPTable invoiceVatData(){
        table = new PdfPTable(5);
        if(client.haveVat){
            cell1   = new PdfPCell(new Phrase("C 8.0%",crimsonromanFont));
            cell2 = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));
            cell3  = new PdfPCell(new Phrase(String.format("%.2f", price*0.08),crimsonromanFont));
            cell4  = new PdfPCell(new Phrase(String.format("%.2f", price + price*0.08), crimsonromanFont));
        }else{
            cell1   = new PdfPCell(new Phrase("C 0.0%",crimsonromanFont));
            cell2  = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));
            cell3 = new PdfPCell(new Phrase("0.00",crimsonromanFont));
            cell4  = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));

        }
        cell5   = new PdfPCell(new Phrase(""));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell4.setBorder(Rectangle.NO_BORDER);
        cell5.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        return table;
    }

    private PdfPTable invoiceVatData2(){
        table = new PdfPTable(5);
        if(client.haveVat){
            cell2 = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));
            cell3  = new PdfPCell(new Phrase(String.format("%.2f", price*0.08),crimsonromanFont));
            cell4  = new PdfPCell(new Phrase(String.format("%.2f", price + price*0.08), crimsonromanFont));
        }else{
            cell2  = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));
            cell3 = new PdfPCell(new Phrase("0.00",crimsonromanFont));
            cell4  = new PdfPCell(new Phrase(price.toString(), crimsonromanFont));

        }
        cell1   = new PdfPCell(new Phrase("Razem",crimsonromanFont));
        cell5   = new PdfPCell(new Phrase(""));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell4.setBorder(Rectangle.NO_BORDER);
        cell5.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        return table;
    }

    private PdfPTable invoicePersonPermision(){
        table = new PdfPTable(2);
        cell1   = new PdfPCell(new Phrase("Uprawnienia do wystawienia dokumentu", crimsonromanFont));
        cell2   = new PdfPCell(new Phrase("Uprawnienia do odbioru dokumentu", crimsonromanFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        return table;
    }

    private Paragraph invoiceNotifyReception(){
        Paragraph reception = new Paragraph("odbiór " + currentDateandTime);
        reception.setAlignment(Element.ALIGN_CENTER);
        return reception;

    }

    public void createInvoicePdf(Context context){

        Document myDoc =  new Document();
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/org.plsk.buycoockie/invoices/"+mFileName+".pdf";

        try{
            PdfWriter.getInstance(myDoc, new FileOutputStream(mFilePath));
            myDoc.open();

            Paragraph splitOne = new Paragraph("============================================================");
            Paragraph splitTwo = new Paragraph("--------------------------------" +
                    "--------------------------------------------------------------------------");
            Paragraph splitThree = new Paragraph("==========================");
            Paragraph splitFour = new Paragraph("=================================================");
            Paragraph splitFive = new Paragraph("----------------------------------------------------------------------------------------");
            splitOne.setAlignment(Element.ALIGN_CENTER);
            splitTwo.setAlignment(Element.ALIGN_CENTER);
/**
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
            //myDoc.add(new Paragraph(invoiceNotifyReception()));


            myDoc.close();
            Toast.makeText(context, "Stworzono fakturę!", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(context, "Błąd: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

}
