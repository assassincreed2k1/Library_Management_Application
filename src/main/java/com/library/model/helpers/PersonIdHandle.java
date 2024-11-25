package com.library.model.helpers;

import com.library.model.Person.Admin;
import com.library.model.Person.Librarian;
import com.library.model.Person.Member;
import com.library.model.Person.Person;

public class PersonIdHandle {
    /**
     * get Person from Id. 
     * @param id String id of Person
     * @return child class of Person
     */
    public static Person getPerson(String id) {
        // Validate ID format (should be 7 characters: 1 letter + 6 digits)
        if (id == null || !id.matches("^[MLA]\\d{6}$")) {
            System.out.println("Invalid ID format.");
            return null;
        }

        String type = id.substring(0, 1); // Extract the first character to identify the type

        // Determine the type and retrieve the corresponding person from the database
        if (type.equals("M")) {
            Member member = new Member();
            try {
                member = member.getInforFromDatabase(id);
            } catch (Exception e) {
                System.out.println("Error fetching member information: " + e.getMessage());
                return null;
            }
            return member;
        } else if (type.equals("L")) {
            Librarian librarian = new Librarian();
            try {
                librarian = librarian.getInforFromDatabase(id);
            } catch (Exception e) {
                System.out.println("Error fetching librarian information: " + e.getMessage());
                return null;
            }
            return librarian;
        } else if (type.equals("A")) {
            Admin admin = new Admin();
            try {
                admin = admin.getInforFromDatabase();
            } catch (Exception e) {
                System.out.println("Error fetching admin information: " + e.getMessage());
            }
            return admin;
        }

        System.out.println("Invalid ID type.");
        return null;
    }
}
