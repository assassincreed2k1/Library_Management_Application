package com.library.controller;

import com.library.service.BookManagement;
import com.library.service.LibrarianManagement;
import com.library.service.LibraryService;
import com.library.service.LoanManagement;
import com.library.service.MagazineManagement;
import com.library.service.MemberManagement;
import com.library.service.NewsPaperManagament;

public class ServiceManager {
    private static LibraryService libraryService;
    private static BookManagement bookManagement;
    private static MagazineManagement magazineManagement;
    private static NewsPaperManagament newsPaperManagament;
    private static LibrarianManagement librarianManagement;
    private static LoanManagement loanManagement;
    private static MemberManagement memberManagement;

    public static void initialize() {
        if (libraryService == null) {
            libraryService = new LibraryService();  
        }
        if (bookManagement == null) {
            bookManagement = new BookManagement();  
        }
        if (magazineManagement == null) {
            magazineManagement = new MagazineManagement();  
        }
        if (newsPaperManagament == null) {
            newsPaperManagament = new NewsPaperManagament();  
        }
        if (librarianManagement == null) {
            librarianManagement = new LibrarianManagement();  
        }
        if (loanManagement == null) {
            loanManagement = new LoanManagement(); 
        }
        if (memberManagement == null) {
            memberManagement = new MemberManagement();  
        }
    }

    public static LibraryService getLibraryService() {
        return libraryService;
    }

    public static BookManagement getBookManagement() {
        return bookManagement;
    }

    public static MagazineManagement getMagazineManagement() {
        return magazineManagement;
    }

    public static NewsPaperManagament getNewsPaperManagament() {
        return newsPaperManagament;
    }

    public static LibrarianManagement getLibrarianManagement() {
        return librarianManagement;
    }

    public static LoanManagement getLoanManagement() {
        return loanManagement;
    }

    public static MemberManagement getMemberManagement() {
        return memberManagement;
    }
}
