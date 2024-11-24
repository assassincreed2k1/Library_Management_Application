package com.library.model.helpers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateStringTest {

    @Test
    public void testConvertStringToDate_ValidInput() {
        String input = "2023-11-01";
        java.util.Date date = DateString.convertStringToDate(input);
        assertNotNull(date, "The date should not be null for valid input");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String formattedDate = formatter.format(date);
        assertEquals(input, formattedDate, "The converted date does not match the input string");
    }

    @Test
    public void testConvertStringToDate_InvalidInput() {
        String input = "invalid-date";
        java.util.Date date = DateString.convertStringToDate(input);
        assertNull(date, "The date should be null for invalid input");
    }

    @Test
    public void testConvertStringToDate_NullInput() {
        String input = null;
        java.util.Date date = DateString.convertStringToDate(input);
        assertNull(date, "The date should be null for null input");
    }

    @Test
    public void testToSqlDate_ValidInput() {
        String input = "2023-11-01";
        Date sqlDate = DateString.toSqlDate(input);
        assertNotNull(sqlDate, "The SQL date should not be null for valid input");
        assertEquals(Date.valueOf(input), sqlDate, "The SQL date does not match the input string");
    }

    @Test
    public void testToSqlDate_InvalidInput() {
        String input = "invalid-date";
        Date sqlDate = DateString.toSqlDate(input);
        assertNull(sqlDate, "The SQL date should be null for invalid input");
    }

    @Test
    public void testToSqlDate_NullInput() {
        String input = null;
        Date sqlDate = DateString.toSqlDate(input);
        assertNull(sqlDate, "The SQL date should be null for null input");
    }

    @Test
    public void testIsValidDate_ValidInput() {
        assertTrue(DateString.isValidDate("2023-11-01"), "The date should be valid");
        assertTrue(DateString.isValidDate("1900-01-01"), "The earliest valid date should be valid");
        assertTrue(DateString.isValidDate(java.time.LocalDate.now().toString()), "Today's date should be valid");
    }

    @Test
    public void testIsValidDate_InvalidInput() {
        assertFalse(DateString.isValidDate("invalid-date"), "The date should be invalid");
        assertFalse(DateString.isValidDate("1800-01-01"), "A date before 1900 should be invalid");
        assertFalse(DateString.isValidDate("3000-01-01"), "A future date beyond today should be invalid");
        assertFalse(DateString.isValidDate(null), "Null date should be invalid");
        assertFalse(DateString.isValidDate(""), "Empty date should be invalid");
    }
}
