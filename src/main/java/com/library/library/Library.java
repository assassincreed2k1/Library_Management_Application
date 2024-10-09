package com.library.library;

import java.util.ArrayList;
import com.library.document.*;

/**Library Management class
 * @param listBook
 * @param listMagazine
 * @param listNews
 */
public class Library {
    private ArrayList<Book> listBooks;
    private ArrayList<Magazine> listMagazines;
    private ArrayList<News> listNews;

    public Library() {
        this.listBooks = new ArrayList<>();
        this.listMagazines = new ArrayList<>();
        this.listNews = new ArrayList<>();
    }

    /**add new book to listBook.
     * @param book new book.
     */
    public void addBook(Book book) {
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

    /**Remove a book from listBooks by its ID.
     * @param id book ID.
     * @return true when removed successful, or false if not found this id
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

    /**Find a book by its ID.
     * @param id book ID.
     * @return the found book, or null if not found.
     */
    public Book findBookByID(String id) {
        for (Book book : listBooks) {
            if (book.getID().equals(id)) {
                return book;
            }
        }
        return null;
    }

    /**Find books by name.
     * @param name book name.
     * @return list of books with the given name. return empty list if not found
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

    /**Find books by group.
     * @param group book group.
     * @return list of books in the given group. Return empty list if not found
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

    /**Find books by author.
     * @param author book author.
     * @return list of books by the given author. Return empty list if not found
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

    /**Find a book by IBSN.
     * @param ibsn book IBSN.
     * @return the found book, or null if not found.
     */
    public Book findBookByIBSN(String ibsn) {
        for (Book book : listBooks) {
            if (book.getIBSN().equals(ibsn)) {
                return book;
            }
        }
        return null;
    }

    

};
