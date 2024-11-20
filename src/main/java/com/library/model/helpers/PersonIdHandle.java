package com.library.model.helpers;

import com.library.model.Person.Admin;
import com.library.model.Person.Librarian;
import com.library.model.Person.Member;
import com.library.model.Person.Person;

public class PersonIdHandle {
    /**
     * Retrieves a person object (Member, Librarian, or Admin) based on their ID.
     * 
     * @param id The ID of the person, starting with "M" (Member), "L" (Librarian), or "A" (Admin),
     *           followed by a 6-digit numeric value (e.g., "M000001").
     * @return A {@link Person} object corresponding to the ID, or null if the ID is invalid or not found.
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
            return new Admin(); //Admin là giá trị cố định nên khongo xử lý lỗi
            
        }

        System.out.println("Invalid ID type.");
        return null;
    }
}
