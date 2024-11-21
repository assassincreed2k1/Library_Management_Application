package com.library.model.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateString {
    /**
     * convert Sting to java.util.Date.
     * @param dateString String date
     * @return java.util.Date date
     */
    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * convert java.util.Date to java.sql.Date.
     * @param dateString java.util.Date date
     * @return java.sql.Date date
     */
    public static java.sql.Date toSqlDate(String dateString) {
        java.util.Date utilDate = convertStringToDate(dateString);
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }
        return null;
    }
    
    public static boolean isValidDate(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }

        // Sử dụng LocalDate để làm chuẩn
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // Parse chuỗi ngày sang LocalDate
            LocalDate parsedDate = LocalDate.parse(date, formatter);

            // So sánh với ngày hiện tại và giới hạn 1900-01-01
            LocalDate minDate = LocalDate.of(1900, 1, 1);
            LocalDate today = LocalDate.now();

            if (parsedDate.isBefore(minDate) || parsedDate.isAfter(today)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false; 
        }
    } 
}
