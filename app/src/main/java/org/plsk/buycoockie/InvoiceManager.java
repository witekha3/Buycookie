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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InvoiceManager {
    ArrayList<Products> pruchasedItems = new ArrayList<Products>();
    Clients client;

    public InvoiceManager(ArrayList<Products> purchasedItems, Clients client){
        this.pruchasedItems = purchasedItems;
        this.client = client;
    }

    public void createInvoicePdf(Context context){

        BaseFont urName = null;
        try {
            urName = BaseFont.createFont("res/font/crimsonroman.ttf", "ISO 8859-2",BaseFont.EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Font urFontName = new Font(urName, 12);

        Document myDoc =  new Document();
        String mFileName = getInvoiceName();
        String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/org.plsk.buycoockie/invoices/"+mFileName+".pdf";

        try{
            PdfWriter.getInstance(myDoc, new FileOutputStream(mFilePath));
            myDoc.open();

            myDoc.add(new Paragraph(
                    "Firma Cukierniczo Gastronomiczna GRZEŚ spółka z Ograniczoną " +
                            "Odpowiedzialnością Spółka Komandytowa (DAWNIEJ: Firma Cukierniczo" +
                            "Gastronoiczna 'GRZEŚ' R. Ząber G Z Krańcowa 4", urFontName));

            myDoc.add(new Paragraph());


            myDoc.close();
            Toast.makeText(context, "Stworzono fakturę!", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(context, "Błąd: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage() + "  xxx");

        }


    }

    public String getInvoiceName(){
        FileManager fm = new FileManager();
        String invoiceName = "";
        if(fm.getDataFromInvoicesTxt().equals("")){
            invoiceName = "Wz"+1+""+ Calendar.getInstance().get(Calendar.YEAR)+"S";
            fm.setDirectoryForInvoices();
        }else{
            String data = fm.getDataFromInvoicesTxt();
            String lines[] = data.split("[\r\n]+");
            if (lines.length != 0) {
                for (String line : lines) {
                    String tab[] = line.split("/");
                    Integer invoiceNr = Integer.valueOf(tab[1])+1;
                    invoiceName = "Wz"+invoiceNr+""+ Calendar.getInstance().get(Calendar.YEAR)+"S";
                }
            }
        }
        return invoiceName;
    }
}
