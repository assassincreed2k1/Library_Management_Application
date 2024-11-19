package com.library.model.helpers;

import com.library.model.Person.Admin;
import com.library.model.Person.Librarian;
import com.library.model.Person.Member;
import com.library.model.Person.Person;

public class PersonIdHandle {
    public static Person getPerson(String id) {
        // Kiểm tra độ dài của id để tránh lỗi ngoài phạm vi
        if (id == null || id.length() < 10) {
            System.out.println("Invalid ID format.");
            return null;
        }
        
        String type = String.valueOf(id.charAt(0));
        int ordinal;
        
        try {
            ordinal = Integer.parseInt(id.substring(1));
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID number format.");
            return null;
        }
        
        if (type.equals("M")) {
            Member mem = new Member();
            try {
                mem = mem.getInforFromDatabase(ordinal);
                mem.getDetails(); //test
                System.out.println(ordinal); //test
            } catch (Exception e) {
                System.out.println("Error fetching member information: " + e.getMessage());
                return null;
            }
            return mem;
        } else if (type.equals("L")) {
            Librarian lib = new Librarian();
            try {
                lib = lib.getInforFromDatabase(ordinal);
                lib.getDetails(); //test
            } catch (Exception e) {
                System.out.println("Error fetching librarian information: " + e.getMessage());
                return null;
            }
            return lib;
        } else if (type.equals("A")) {
            Admin admin = new Admin();
            try {
                //chu xet den
                admin = (Admin)admin.getInforFromDatabase(ordinal);
            } catch (Exception e) {
                System.out.println("Error fetching librarian information: " + e.getMessage());
                return null;
            }
            return admin;
        }
        
        System.out.println("Invalid ID type.");
        return null;
    }
    
}
