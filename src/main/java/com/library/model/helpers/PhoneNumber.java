package com.library.model.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumber {
    /**
     * check phone number is valid or not.
     * @param phoneNumber String phone number
     * @return boolean 
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        //Bieu thuc chinh quy cho so dien thoai hop le (do dai 10 - 11)
        String regex = "^[0-9]{10,11}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        
        return matcher.matches();
    }
}
