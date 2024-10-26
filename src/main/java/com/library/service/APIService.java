package com.library.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 * APIService class to interact with the Open Library API.
 * This class provides static methods to search for books and retrieve book information by ISBN.
 */
public class APIService {
    private static final String OPEN_LIBRARY_SEARCH_URL = "https://openlibrary.org/search.json?q=";
    private static final String OPEN_LIBRARY_BOOK_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:";

    /**
     * Searches for books by a given keyword.
     *
     * @param query the keyword to search for in book titles and descriptions.
     * @return a JSONObject containing the search results, or null if the request fails.
     */
    public static JSONObject searchBooks(String query) {
        String urlString = OPEN_LIBRARY_SEARCH_URL + query;
        return getJsonResponse(urlString);
    }

    /**
     * Retrieves book information by its ISBN.
     *
     * @param isbn the ISBN of the book to retrieve information for.
     * @return a JSONObject containing the book information, or null if the request fails.
     */
    public static JSONObject getBookInfoByISBN(String isbn) {
        String urlString = OPEN_LIBRARY_BOOK_URL + isbn + "&format=json&jscmd=data";
        return getJsonResponse(urlString);
    }

    /**
     * Sends a GET request to the specified URL and returns the response as a JSONObject.
     *
     * @param urlString the URL to send the GET request to.
     * @return a JSONObject containing the response data, or null if the request fails.
     */
    private static JSONObject getJsonResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return new JSONObject(response.toString()); // Use JSONObject to parse response
            } else {
                System.out.println("Request failed, status code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null in case of error
    }
}
