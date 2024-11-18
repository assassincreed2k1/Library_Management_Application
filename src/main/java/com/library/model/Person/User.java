package com.library.model.Person;

/**
 * This class saves information of the logged-in user
 */
public class User {
    private static String id; 
    private static String type; 

    // Set the user ID and type based on login
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

    /**
     * clear all information when log out.
     */
    public static void clearUser() {
        id = null;
        type = null;
    }
}
