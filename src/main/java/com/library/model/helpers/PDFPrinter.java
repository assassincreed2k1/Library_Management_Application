package com.library.model.helpers;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class PDFPrinter {

    public static void printPDF(VBox contentBox, String pdfName) {
        // Create a new PDF document
        PDDocument document = new PDDocument();
    
        // Create a page and add to the document
        PDPage page = new PDPage();
        document.addPage(page);
    
        try {
            // Prepare content stream in a try-with-resources block
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
    
                for (javafx.scene.Node node : contentBox.getChildren()) {
                    if (node instanceof javafx.scene.control.Label) {
                        javafx.scene.control.Label label = (javafx.scene.control.Label) node;
    
                        // Handle multi-line labels
                        String[] lines = label.getText().split("\n");
                        for (String line : lines) {
                            contentStream.showText(line.trim());
                            contentStream.newLineAtOffset(0, -15);
                        }
                    }
                }
    
                contentStream.endText();
            } // Automatically closes the stream here
            // Save document to a file
            File directory = new File("pdf");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, pdfName + ".pdf");
            document.save(file);
    
            // Success message
            MessageUtil.showAlert("information", "Print Success", "The document has been saved as a PDF.");
        } catch (IOException e) {
            e.printStackTrace();
            MessageUtil.showAlert("error", "Print Error", "An error occurred while printing the document.");
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
