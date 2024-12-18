package com.library.model.Person;

import com.library.model.helpers.PersonIdHandle;

public class User {
    private static String id; 
    private static String type; 

    /**
     * set user information based on user id.
     * @param userId user id 
     */
    public static void setUser(String userId) {
        id = userId;
        type = userId != null && !userId.isEmpty() ? String.valueOf(userId.charAt(0)) : null;
    }

    public static String getId() {
        return id;
    }

    public static String getType() {
        return type;
    }

    public static boolean isLibrarian() {
        return "L".equalsIgnoreCase(type);
    }

    public static boolean isAdmin() {
        return "A".equalsIgnoreCase(type);
    }

    public static boolean isMember() {
        return "M".equalsIgnoreCase(type);
    }

    /**
     * clear all information when log out.
     */
    public static void clearUser() {
        id = null;
        type = null;
    }

    public static String getDetails() {
        Person person = PersonIdHandle.getPerson(id);

        if (type.equals("L")) {
            Librarian librarian = (Librarian) person;
            return librarian.getDetails();
        } else if (type.equals( "M")) {
            Member member = (Member) person;
            return member.getDetails();
        } else if (type.equals("A")) {
            Admin admin = (Admin) person;
            return admin.getDetails();
        }

        return "User not found";
    }

    /**
     * get last name from name.
     * @return String
     */
    public static String getLastName() {
        Person person = PersonIdHandle.getPerson(id);
        if (person == null) {
            return "Can't find this person.";
        }
        String fullName = person.getName();

        String[] nameParts = fullName.split("\\s+"); //tách tên cho vào mảng, có dấu cách thì tách

        return nameParts[nameParts.length - 1]; //lấy phần tử cuối
    }
    
}
