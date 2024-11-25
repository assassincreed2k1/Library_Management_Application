package com.library.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONObject;

// import javafx.scene.image.Image;

/**
 * APIService class to interact with the Open Library API.
 * This class provides static methods to search for books and retrieve book
 * information by ISBN.
 */
public class APIService {
    private static final String OPEN_LIBRARY_SEARCH_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:";
    private static final String OPEN_LIBRARY_BOOK_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:";

    /**
     * Searches for books by a given keyword.
     *
     * @param query the keyword to search for in book titles and descriptions.
     * @return a JSONObject containing the search results, or null if the request
     *         fails.
     */
    public static JSONObject searchBooks(String query) {
        String urlString = OPEN_LIBRARY_SEARCH_URL + query;
        return getJsonResponse(urlString);
    }

    /**
     * Retrieves book information by its ISBN.
     *
     * @param isbn the ISBN of the book to retrieve information for.
     * @return a JSONObject containing the book information, or null if the request
     *         fails.
     */
    public static JSONObject getBookInfoByISBN(String isbn) {
        String urlString = OPEN_LIBRARY_BOOK_URL + isbn + "&format=json&jscmd=data";
        return getJsonResponse(urlString);
    }

    /**
     * Sends a GET request to the specified URL and returns the response as a
     * JSONObject.
     *
     * @param urlString the URL to send the GET request to.
     * @return a JSONObject containing the response data, or null if the request
     *         fails.
     */
    static JSONObject getJsonResponse(String urlString) {
        try {
            URI uri = URI.create(urlString); // Create URI from the string
            URL url = uri.toURL(); // Convert URI to URL

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Use try-with-resources to automatically close the BufferedReader
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    return new JSONObject(response.toString()); // Use JSONObject to parse response
                }
            } else {
                System.out.println("Request failed, status code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null in case of error
    }

    /**
     * Retrieves the URL for the book cover image based on the provided ISBN.
     * The method fetches book data using an external API and attempts to extract
     * the
     * medium-sized cover image URL.
     *
     * @param isbn the ISBN of the book whose cover image URL is to be fetched.
     * @return a `String` containing the URL of the medium-sized cover image, or an
     *         empty string if not found.
     */
    public static String getCoverBookURL(String isbn) {
        JSONObject getAPIBook = APIService.getBookInfoByISBN(isbn);
        JSONObject bookData = getAPIBook.getJSONObject("ISBN:" + isbn);

        if (bookData.has("cover")) {
            JSONObject cover = bookData.getJSONObject("cover");
            String prevImgUrl = cover.optString("medium", "");
            if (prevImgUrl.isEmpty()) {
                System.out.println("Load Book's Cover failed! ISBN: " + isbn + " - 'medium' not found\n");
            }
            return prevImgUrl;
        } else {
            System.out.println("Cover not found for ISBN: " + isbn + "\n");
            return "";
        }
    }

    /**
     * Generates a QR code image from the provided text and saves it to the
     * specified file path.
     *
     * @param text     the content to encode in the QR code.
     * @param width    the width of the generated QR code image.
     * @param height   the height of the generated QR code image.
     * @param filePath the file path (including file name) where the QR code image
     *                 will be saved.
     * @throws WriterException if an error occurs while encoding the QR code.
     * @throws IOException     if an I/O error occurs during file writing.
     */
    public static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = new File(filePath).toPath();
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
