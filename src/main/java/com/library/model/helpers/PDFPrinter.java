package com.library.model.helpers;

import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;

public class PDFPrinter {
    /**
     * Export VBox content to a PDF file.
     *
     * @param contentBox the VBox containing the content
     * @param pdfName    the name of the PDF file (without extension)
     */
    public static void printPDF(VBox contentBox, String pdfName) {
        PDDocument document = new PDDocument(); // Create a new PDF document

        try {
            // Add a single page to the document
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set font and start writing text
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);

                // Loop through children of VBox to extract text
                for (javafx.scene.Node node : contentBox.getChildren()) {
                    if (node instanceof javafx.scene.control.Label) {
                        javafx.scene.control.Label label = (javafx.scene.control.Label) node;

                        // Remove diacritics (accents) from label text
                        String noAccentText = removeDiacritics(label.getText());

                        // Handle multi-line text in the label
                        String[] lines = noAccentText.split("\n");
                        for (String line : lines) {
                            contentStream.showText(line.trim());
                            contentStream.newLineAtOffset(0, -15); // Move to the next line
                        }
                    }
                }

                contentStream.endText();
            }

            // Ensure the "pdf" directory exists
            File directory = new File("pdf");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the document to a file
            File file = new File(directory, pdfName + ".pdf");
            document.save(file);

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

    /**
     * Removes diacritics (accents) from a string.
     *
     * @param input the input string
     * @return the string without diacritics
     */
    private static String removeDiacritics(String input) {
        if (input == null) {
            return null;
        }
        // Normalize the string and remove combining diacritical marks
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }
}