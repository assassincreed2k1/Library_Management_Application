package com.library.model.helpers;

import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for generating and saving PDF documents.
 * Converts content from a {@link javafx.scene.layout.VBox} into a formatted PDF.
 */
public class PDFPrinter {

    /**
     * Generates a PDF document from the contents of a VBox and saves it to the specified file name.
     *
     * @param contentBox the VBox containing the content to print.
     * @param pdfName    the name of the PDF file to be created (without extension).
     */
    public static void printPDF(VBox contentBox, String pdfName) {
        // Create a new PDF document
        PDDocument document = new PDDocument();

        // Create a page and add it to the document
        PDPage page = new PDPage();
        document.addPage(page);

        try {
            // Prepare content stream in a try-with-resources block
            PDType0Font font = PDType0Font.load(document, new File("/font/DejaVuSans-Bold.ttf"));

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);

                // Iterate through the VBox's children and process any Label nodes
                for (javafx.scene.Node node : contentBox.getChildren()) {
                    if (node instanceof javafx.scene.control.Label label) {
                        String[] lines = label.getText().split("\n");
                        for (String line : lines) {
                            contentStream.showText(line.trim());
                            contentStream.newLineAtOffset(0, -15); // Move to the next line
                        }
                    }
                }

                contentStream.endText();
            }

            // Create the directory if it doesn't exist
            File directory = new File("pdf");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the document to a file
            File file = new File(directory, pdfName + ".pdf");
            document.save(file);

            // Show a success message
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
