package com.library.model.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateString {
    /**
     * convert Sting to java.util.Date.
     * @param dateString String date
     * @return java.util.Date date
     */
    private static Date convertStringToDate(String dateString) {
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
    
    public static boolean isValidDate (String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); //khong cho phep cac gia tri khong hop le

        try {
            sdf.parse(date); // Thu parse chuoi thanh ngay thang, neu bi exception thi false
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
