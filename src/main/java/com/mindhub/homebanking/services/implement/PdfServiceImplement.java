package com.mindhub.homebanking.services.implement;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.PdfService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class PdfServiceImplement implements PdfService {
    @Override
    public void export(HttpServletResponse response, Client client1, Client client2, String numberAccount1, String numberAccount2,
                       double amount, String description) throws IOException{

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        Locale locale = new Locale("en", "US");
        DateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String currentDatTime= formatDate.format(new Date());
        NumberFormat currency = NumberFormat.getCurrencyInstance(locale);
        String amountFormatCurrency = currency.format(amount);

        document.open();
        Image imgeLogo = Image.getInstance("./src/main/resources/static/assets/logoM.png");
        imgeLogo.scaleAbsolute(70,70);
        imgeLogo.setAbsolutePosition(60,730);
        document.add(imgeLogo);
        Font fontTitle = FontFactory.getFont(FontFactory.COURIER_BOLD);
        fontTitle.setSize(20);
        Color bankColor = new Color(18, 60, 239, 100);
        fontTitle.setColor(bankColor);
        Paragraph paragraph = new Paragraph("Mindhub Bank", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(15);
        Paragraph paragraph1 = new Paragraph("Bank transfer receipt.", fontParagraph);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph1.setSpacingBefore(15);
        paragraph1.setSpacingAfter(20);
        document.add(paragraph1);

        Font fontDate = FontFactory.getFont(FontFactory.HELVETICA);
        fontDate.setSize(12);
        Paragraph paragraphDate = new Paragraph("Date: "+currentDatTime, fontDate);
        paragraphDate.setSpacingAfter(15);
        document.add(paragraphDate);

        Font fontParagraphTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontParagraphTitle.setSize(12);
        Font fontParagraph2 = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph2.setSize(12);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{7.5f, 5.5f});
        PdfPCell cell = new PdfPCell();
        Phrase phrase1 = new Phrase();
        Chunk text1= new Chunk("Origin Account: ", fontParagraphTitle);
        Chunk text2= new Chunk(numberAccount1,fontParagraph2);
        phrase1.add(text1);
        phrase1.add(text2);
        cell.setPhrase(phrase1);
        cell.setBorder(0);
        cell.setExtraParagraphSpace(10);
        table.addCell(cell);

        Phrase phrase2 = new Phrase();
        Chunk text3 = new Chunk("Owner: ", fontParagraphTitle);
        Chunk text4 = new Chunk(client1.getFirstName()+" "+client1.getLastName(), fontParagraph2);
        phrase2.add(text3);
        phrase2.add(text4);
        cell.setPhrase(phrase2);
        table.addCell(cell);

        Phrase phrase3 = new Phrase();
        Chunk text5 = new Chunk("Amount: ", fontParagraphTitle);
        Chunk text6 = new Chunk(amountFormatCurrency,fontParagraph2);
        phrase3.add(text5);
        phrase3.add(text6);
        cell.setPhrase(phrase3);
        table.addCell(cell);

        Phrase phrase4 = new Phrase();
        Chunk text7 = new Chunk("Description: ", fontParagraphTitle);
        Chunk text8 = new Chunk(description,fontParagraph2);
        phrase4.add(text7);
        phrase4.add(text8);
        cell.setPhrase(phrase4);
        table.addCell(cell);

        Phrase phrase5 = new Phrase();
        Chunk text9= new Chunk("Destination account: ", fontParagraphTitle);
        Chunk text10= new Chunk(numberAccount2,fontParagraph2);
        phrase5.add(text9);
        phrase5.add(text10);
        cell.setPhrase(phrase5);
        table.addCell(cell);

        Phrase phrase6 = new Phrase();
        Chunk text11 = new Chunk("Owner: ", fontParagraphTitle);
        Chunk text12 = new Chunk(client2.getFirstName()+" "+client2.getLastName(), fontParagraph2);
        phrase6.add(text11);
        phrase6.add(text12);
        cell.setPhrase(phrase6);
        table.addCell(cell);

        document.add(table);

        Font fontParagraph3 = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(15);
        Paragraph paragraph3 = new Paragraph("Thanks!!!!", fontParagraph3);
        paragraph3.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph3.setSpacingBefore(15);
        paragraph3.setSpacingAfter(20);
        document.add(paragraph3);

        document.close();
    }
}
