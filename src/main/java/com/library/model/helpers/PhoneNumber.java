package com.library.model.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for phone number validation.
 */
public class PhoneNumber {

    /**
     * Validates if the given phone number is in a valid format.
     * 
     * <p>A valid phone number should:</p>
     * <ul>
     *   <li>Be non-null and non-empty</li>
     *   <li>Contain only digits</li>
     *   <li>Have a length of 10 to 11 characters</li>
     * </ul>
     *
     * @param phoneNumber The phone number to validate.
     * @return {@code true} if the phone number is valid, otherwise {@code false}.
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
