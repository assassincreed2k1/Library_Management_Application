package com.library.model.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility class for converting and validating date strings.
 * Provides methods for converting between different date formats and checking the validity of dates.
 */
public class DateString {

    /**
     * Converts a date string in the format "yyyy-MM-dd" to a {@link java.util.Date}.
     *
     * @param dateString the date string to convert.
     * @return a {@link java.util.Date} representation of the input string, or null if the conversion fails.
     */
    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts a date string in the format "yyyy-MM-dd" to a {@link java.sql.Date}.
     *
     * @param dateString the date string to convert.
     * @return a {@link java.sql.Date} representation of the input string, or null if the conversion fails.
     */
    public static java.sql.Date toSqlDate(String dateString) {
        Date utilDate = convertStringToDate(dateString);
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }
        return null;
    }

    /**
     * Validates a date string to ensure it is in the format "yyyy-MM-dd" and within the valid range
     * (between 1900-01-01 and the current date).
     *
     * @param date the date string to validate.
     * @return true if the date string is valid, false otherwise.
     */
    public static boolean isValidDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            LocalDate minDate = LocalDate.of(1900, 1, 1);
            LocalDate today = LocalDate.now();
            return !parsedDate.isBefore(minDate) && !parsedDate.isAfter(today);
        } catch (Exception e) {
            return false;
        }
    }
}
