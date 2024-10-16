package com.library.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.library.model.document.*;

/**
 * The {@code Library} class manages collections of {@link Book},
 * {@link Magazine}, and {@link Newspaper}.
 * It provides methods to add, remove, and search for these documents within
 * their respective collections.
 * <p>
 * Example:
 * 
 * <pre>
 * Library library = new Library();
 * Book book = new Book("Book Title", "Fiction", 10, "Author", "12345");
 * library.addDocuments(book);
 * </pre>
 * </p>
 * 
 * @param listBooks      The collection of books in the library.
 * @param listMagazines  The collection of magazines in the library.
 * @param listNewspapers The collection of newspapers in the library.
 */
public class Library {
    private ArrayList<Book> listBooks;
    private ArrayList<Magazine> listMagazines;
    private ArrayList<Newspaper> listNewspapers;

    /**
     * Default constructor for the {@code Library} class.
     * Initializes empty lists for books, magazines, and newspapers.
     */
    public Library(Connection cn) {
        
        this.listBooks = new ArrayList<>();
        this.listMagazines = new ArrayList<>();
        this.listNewspapers = new ArrayList<>();
    }

    /**
     * Adds a new {@link Book} to the library's book collection.
     * If the book already exists, its quantity is updated.
     * 
     * @param book The new book to add.
     */
    public void addDocuments(Book book) {
        if (listBooks.size() == 0) {
            listBooks.add(book);
            return;
        }

        for (Book iterator : listBooks) {
            if (iterator.equals(book)) {
                iterator.setQuantity(iterator.getQuantity() + book.getQuantity());
                return;
            }
        }
        listBooks.add(book);
    }

    /**
     * Removes a {@link Book} from the library's book collection by its ID.
     * 
     * @param id The ID of the book to remove.
     * @return true if the book was successfully removed, false if not found.
     */
    public boolean removeBook(String id) {
        for (int i = 0; i < listBooks.size(); i++) {
            if (listBooks.get(i).getID().equals(id)) {
                listBooks.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a {@link Book} by its ID.
     * 
     * @param id The ID of the book to find.
     * @return The found book, or null if not found.
     */
    public Book findBookByID(String id) {
        for (Book book : listBooks) {
            if (book.getID().equals(id)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Finds books by name.
     * 
     * @param name The name of the book(s) to find.
     * @return A list of books with the given name. Returns an empty list if not
     *         found.
     */
    public ArrayList<Book> findBookByName(String name) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Book book : listBooks) {
            if (book.getName().equalsIgnoreCase(name)) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }

    /**
     * Finds books by group.
     * 
     * @param group The group or category of the book(s) to find.
     * @return A list of books in the given group. Returns an empty list if not
     *         found.
     */
    public ArrayList<Book> findBookByGroup(String group) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Book book : listBooks) {
            if (book.getGroup().equalsIgnoreCase(group)) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }

    /**
     * Finds books by author.
     * 
     * @param author The author of the book(s) to find.
     * @return A list of books by the given author. Returns an empty list if not
     *         found.
     */
    public ArrayList<Book> findBookByAuthor(String author) {
        ArrayList<Book> foundBooks = new ArrayList<>();
        for (Book book : listBooks) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                foundBooks.add(book);
            }
        }
        return foundBooks;
    }

    /**
     * Finds a {@link Book} by its ISBN.
     * 
     * @param ibsn The ISBN of the book to find.
     * @return The found book, or null if not found.
     */
    public Book findBookByIBSN(String ibsn) {
        for (Book book : listBooks) {
            if (book.getIBSN().equals(ibsn)) {
                return book;
            }
        }
        return null;
    }

    /**
     * Adds a new {@link Magazine} to the library's magazine collection.
     * If the magazine already exists, its quantity is updated.
     * 
     * @param magazine The new magazine to add.
     */
    public void addDocuments(Magazine magazine) {
        if (listMagazines.size() == 0) {
            listMagazines.add(magazine);
            return;
        }

        for (Magazine iterator : listMagazines) {
            if (iterator.equals(magazine)) {
                iterator.setQuantity(iterator.getQuantity() + magazine.getQuantity());
                return;
            }
        }
        listMagazines.add(magazine);
    }

    /**
     * Removes a {@link Magazine} from the library's magazine collection by its ID.
     * 
     * @param id The ID of the magazine to remove.
     * @return true if the magazine was successfully removed, false if not found.
     */
    public boolean removeMagazine(String id) {
        for (int i = 0; i < listMagazines.size(); i++) {
            if (listMagazines.get(i).getID().equals(id)) {
                listMagazines.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Finds magazines by publisher.
     * 
     * @param publisher The publisher of the magazine(s) to find.
     * @return A list of magazines by the given publisher. Returns an empty list if
     *         not found.
     */
    public ArrayList<Magazine> findMagazinesByPublisher(String publisher) {
        ArrayList<Magazine> foundMagazines = new ArrayList<>();
        for (Magazine magazine : listMagazines) {
            if (magazine.getPublisher().equalsIgnoreCase(publisher)) {
                foundMagazines.add(magazine);
            }
        }
        return foundMagazines;
    }

    /**
     * Finds magazines by genre.
     * 
     * @param genre The genre of the magazine(s) to find.
     * @return A list of magazines by the given genre. Returns an empty list if not
     *         found.
     */
    public ArrayList<Magazine> findMagazinesByGenre(String genre) {
        ArrayList<Magazine> foundMagazines = new ArrayList<>();
        for (Magazine magazine : listMagazines) {
            if (magazine.getGenre().equalsIgnoreCase(genre)) {
                foundMagazines.add(magazine);
            }
        }
        return foundMagazines;
    }

    /**
     * Adds a new {@link Newspaper} to the library's newspaper collection.
     * If the newspaper already exists, its quantity is updated.
     * 
     * @param newspaper The new newspaper to add.
     */
    public void addDocuments(Newspaper newspaper) {
        if (listNewspapers.size() == 0) {
            listNewspapers.add(newspaper);
            return;
        }

        for (Newspaper iterator : listNewspapers) {
            if (iterator.equals(newspaper)) {
                iterator.setQuantity(iterator.getQuantity() + newspaper.getQuantity());
                return;
            }
        }
        listNewspapers.add(newspaper);
    }

    /**
     * Removes a {@link Newspaper} from the library's newspaper collection by its
     * ID.
     * 
     * @param id The ID of the newspaper to remove.
     * @return true if the newspaper was successfully removed, false if not found.
     */
    public boolean removeNewspaper(String id) {
        for (int i = 0; i < listNewspapers.size(); i++) {
            if (listNewspapers.get(i).getID().equals(id)) {
                listNewspapers.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Finds Newspapers by source.
     * 
     * @param source The source of the Newspaper(s) to find.
     * @return A list of Newspapers by the given source. Returns an empty list if not
     *         found.
     */
    public ArrayList<Newspaper> findNewspapersBySource(String source) {
        ArrayList<Newspaper> foundNewspaper = new ArrayList<>();
        for (Newspaper newspaper : listNewspapers) {
            if (newspaper.getSource().equals(source)) {
                foundNewspaper.add(newspaper);
            }
        }
        return foundNewspaper;
    }

    /**
     * Finds Newspapers by category.
     * 
     * @param source The category of the Newspaper(s) to find.
     * @return A list of Newspapers by the given category. Returns an empty list if
     *         not found.
     */
    public ArrayList<Newspaper> findNewspapersByCategory(String category) {
        ArrayList<Newspaper> foundNewspaper = new ArrayList<>();
        for (Newspaper newspaper : listNewspapers) {
            if (newspaper.getSource().equals(category)) {
                foundNewspaper.add(newspaper);
            }
        }
        return foundNewspaper;
    }

    /**
     * Finds Newspapers by region.
     * 
     * @param source The category of the Newspaper(s) to find.
     * @return A list of Newspapers by the given region. Returns an empty list if
     *         not found.
     */
    public ArrayList<Newspaper> findNewspapersByRegion(String region) {
        ArrayList<Newspaper> foundNewspaper = new ArrayList<>();
        for (Newspaper newspaper : listNewspapers) {
            if (newspaper.getSource().equals(region)) {
                foundNewspaper.add(newspaper);
            }
        }
        return foundNewspaper;
    }
}
