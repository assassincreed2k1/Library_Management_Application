package com.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class APIServiceTest {

    @Test
    public void testSearchBooks_Success() {
        String mockQuery = "test";
        String mockResponse = "{\"books\": [{\"title\": \"Test Book\"}]}";

        try (MockedStatic<APIService> mockedStatic = mockStatic(APIService.class, invocation -> {
            if (invocation.getMethod().getName().equals("getJsonResponse")) {
                return new JSONObject(mockResponse);
            }
            return invocation.callRealMethod();
        })) {

            JSONObject result = APIService.searchBooks(mockQuery);

            assertNotNull(result, "Result should not be null");
            assertTrue(result.has("books"), "Response should contain 'books'");
            assertEquals("Test Book", result.getJSONArray("books").getJSONObject(0).getString("title"));
        }
    }

    @Test
    public void testGetBookInfoByISBN_Success() {
        String mockISBN = "1234567890";
        String mockResponse = "{\"ISBN:1234567890\": {\"title\": \"Test Book\"}}";

        try (MockedStatic<APIService> mockedStatic = mockStatic(APIService.class, invocation -> {
            if (invocation.getMethod().getName().equals("getJsonResponse")) {
                return new JSONObject(mockResponse);
            }
            return invocation.callRealMethod();
        })) {

            JSONObject result = APIService.getBookInfoByISBN(mockISBN);

            assertNotNull(result, "Result should not be null");
            assertTrue(result.has("ISBN:1234567890"), "Response should contain 'ISBN:1234567890'");
            assertEquals("Test Book", result.getJSONObject("ISBN:1234567890").getString("title"));
        }
    }

    @Test
    public void testGetCoverBookURL_Success() {
        String mockISBN = "1234567890";
        String mockResponse = "{\"ISBN:1234567890\": {\"cover\": {\"medium\": \"http://example.com/cover.jpg\"}}}";

        try (MockedStatic<APIService> mockedStatic = mockStatic(APIService.class, invocation -> {
            if (invocation.getMethod().getName().equals("getBookInfoByISBN")) {
                return new JSONObject(mockResponse);
            }
            return invocation.callRealMethod();
        })) {

            String result = APIService.getCoverBookURL(mockISBN);

            assertNotNull(result, "Result should not be null");
            assertEquals("http://example.com/cover.jpg", result, "Cover URL should match");
        }
    }

    @Test
    public void testGetCoverBookURL_NoCover() {
        String mockISBN = "1234567890";
        String mockResponse = "{\"ISBN:1234567890\": {}}";

        try (MockedStatic<APIService> mockedStatic = mockStatic(APIService.class, invocation -> {
            if (invocation.getMethod().getName().equals("getBookInfoByISBN")) {
                return new JSONObject(mockResponse);
            }
            return invocation.callRealMethod();
        })) {

            String result = APIService.getCoverBookURL(mockISBN);

            assertNotNull(result, "Result should not be null");
            assertTrue(result.isEmpty(), "Result should be empty for missing cover");
        }
    }
}