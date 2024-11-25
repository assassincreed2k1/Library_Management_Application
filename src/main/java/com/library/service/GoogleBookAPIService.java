package com.library.service;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * APIService class to interact with the Google Books API.
 * This class provides static methods to search for books and retrieve book
 * information by ISBN.
 */
public class GoogleBookAPIService {
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String API_KEY = "AIzaSyA4dH8cvS9tYosNgKnMShZWK5EyLR3Dbg4";

    /**
     * Searches for books by a given keyword.
     *
     * @param query the keyword to search for in book titles and descriptions.
     * @return a JSONObject containing the search results, or null if the request
     *         fails.
     */
    public static JSONObject searchBooks(String query) {
        String urlString = API_URL + query + "&key=" + API_KEY;
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
        String urlString = API_URL + "isbn:" + isbn + "&key=" + API_KEY;
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
     * Retrieves the cover image URL of a book by its ISBN.
     *
     * @param isbn the ISBN of the book to get the cover image for.
     * @return the URL of the book cover image, or an empty string if not found.
     */
    public static String getCoverBookURL(String isbn) {
        JSONObject response = APIService.getBookInfoByISBN(isbn);
        if (response != null && response.has("items")) {
            JSONArray items = response.getJSONArray("items");
            JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

            if (volumeInfo.has("imageLinks")) {
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String thumbnailUrl = imageLinks.optString("thumbnail", "");
                if (thumbnailUrl.isEmpty()) {
                    System.out.println("Load Book's Cover failed! ISBN: " + isbn + " - 'thumbnail' not found\n");
                }
                return thumbnailUrl;
            } else {
                System.out.println("Image links not found for ISBN: " + isbn + "\n");
            }
        } else {
            System.out.println("No items found for ISBN: " + isbn + "\n");
        }
        return "";
    }
}
