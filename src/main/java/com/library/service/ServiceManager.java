package com.library.service;

/**
 * The {@code ServiceManager} class is responsible for initializing and
 * providing access
 * to various service instances used across the application.
 * It ensures that each service is only initialized once and provides singleton
 * access
 * to the services.
 */
public class ServiceManager {

    // Service instances
    private static LibraryService libraryService;
    private static BookManagement bookManagement;
    private static MagazineManagement magazineManagement;
    private static NewsPaperManagement newsPaperManagement;
    private static LibrarianManagement librarianManagement;
    private static LoanManagement loanManagement;
    private static MemberManagement memberManagement;
    private static BackgroundService backgroundService;
    private static AdminManagement adminManagement;

    /**
     * Initializes all service instances if they haven't been initialized yet.
     * This method ensures that the services are created only once during the
     * application lifecycle.
     */
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
        if (newsPaperManagement == null) {
            newsPaperManagement = new NewsPaperManagement();
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
        if (backgroundService == null) {
            backgroundService = new BackgroundService();
        }
        if (adminManagement == null) {
            adminManagement = new AdminManagement();
        }
    }

    /**
     * Returns the instance of {@link LibraryService}.
     * 
     * @return the singleton instance of {@link LibraryService}
     */
    public static LibraryService getLibraryService() {
        return libraryService;
    }

    /**
     * Returns the instance of {@link BookManagement}.
     * 
     * @return the singleton instance of {@link BookManagement}
     */
    public static BookManagement getBookManagement() {
        return bookManagement;
    }

    /**
     * Returns the instance of {@link MagazineManagement}.
     * 
     * @return the singleton instance of {@link MagazineManagement}
     */
    public static MagazineManagement getMagazineManagement() {
        return magazineManagement;
    }

    /**
     * Returns the instance of {@link NewsPaperManagament}.
     * 
     * @return the singleton instance of {@link NewsPaperManagament}
     */
    public static NewsPaperManagement getNewsPaperManagement() {
        return newsPaperManagement;
    }

    /**
     * Returns the instance of {@link LibrarianManagement}.
     * 
     * @return the singleton instance of {@link LibrarianManagement}
     */
    public static LibrarianManagement getLibrarianManagement() {
        return librarianManagement;
    }

    /**
     * Returns the instance of {@link LoanManagement}.
     * 
     * @return the singleton instance of {@link LoanManagement}
     */
    public static LoanManagement getLoanManagement() {
        return loanManagement;
    }

    /**
     * Returns the instance of {@link MemberManagement}.
     * 
     * @return the singleton instance of {@link MemberManagement}
     */
    public static MemberManagement getMemberManagement() {
        return memberManagement;
    }

    /**
     * Returns the singleton instance of the {@link BackgroundService} class.
     * This method ensures that there is only one instance of BackgroundService used
     * throughout the application.
     *
     * @return the singleton instance of {@link BackgroundService}
     */
    public static BackgroundService getBackgroundService() {
        return backgroundService;
    }

    /**
     * Returns the instance of {@link AdminManagement}.
     * 
     * @return the singleton instance of {@link AdminManagement}
     */
    public static AdminManagement getAdminManagement() {
        return adminManagement;
    }
}
