package org.gunnarro.microservice.todoservice;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  <dependency>
 *             <groupId>org.apache.pdfbox</groupId>
 *             <artifactId>pdfbox</artifactId>
 *             <version>3.0.7</version>
 *         </dependency>
 */
public class PdfToTextTest {

    @Test
    void pdfToText() {
        // Paths to your input PDF and target output text file (optional)
        File pdfFile = new File("src/test/resources/invoice.pdf");

        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            // Instantiate PDFTextStripper to manage text extraction
            PDFTextStripper pdfStripper = new PDFTextStripper();

            // Optional configurations:
             pdfStripper.setStartPage(1); // Set starting page
             pdfStripper.setEndPage(1);   // Set ending page

            // Extract text from the loaded document
            String extractedText = pdfStripper.getText(document);

            // Print the result to the console
            //System.out.println(extractedText);
            findLineThatStartWithNumber(extractedText);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading the PDF file: " + e.getMessage());
        }
    }

    private void findLineThatStartWithNumber(String text) {
        Pattern pattern = Pattern.compile("(?m)^\\d{6,10}.*");
        Matcher matcher = pattern.matcher(text);

        System.out.println("Lines starting with a number:");
        while (matcher.find()) {
            String vareNrLinje = matcher.group();
            System.out.println(vareNrLinje);
            String vareNr = finn("\\d{4,10}+", vareNrLinje, true);
            String antall = finn(" \\d{1,4}+ ", vareNrLinje, true);
            String pris = finn("\\d+ \\d+\\,\\d{2}+", vareNrLinje, true);
            if (pris == null) {
                pris = finn("\\d+\\,\\d{2}+", vareNrLinje, true);
            }

            StringJoiner json = new StringJoiner(";");
            json.add("vareNr : " + vareNr);
            json.add("antall : " + antall);
            json.add("pris : " + pris);

            System.out.println(json);
        }
    }

    private String finn(String regex, String text, boolean findFirst) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        String s = null;
        while (matcher.find()) {
            if (findFirst) {
                s = matcher.group();
                return s;
            }
        }
        return s;
    }
}



