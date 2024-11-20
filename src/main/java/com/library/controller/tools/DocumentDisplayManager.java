package com.library.controller.tools;

import com.library.service.BookManagement;
import com.library.model.doc.Book;
import com.library.service.LibraryService;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages the display of document information, including books,
 * in a JavaFX application. It provides functionality to load and display
 * book data dynamically along with associated images.
 */
public class DocumentDisplayManager {
    private BookManagement bookManagement;
    private LibraryService libraryService;
    private ImageView[] imageViews;
    private Label[] names;
    private Label[] authors;
    private Label[] genres;
    private Label[] availables;
    private final Map<String, Image> imageCache = new HashMap<>();

    /**
     * Constructor for DocumentDisplayManager.
     *
     * @param bookManagement the book management service to retrieve document
     *                       details.
     * @param libraryService the library service to fetch library data like IDs.
     * @param imageViews     an array of ImageViews to display book images.
     * @param names          an array of Labels to display book names.
     * @param authors        an array of Labels to display authors' names.
     * @param genres         an array of Labels to display genres of books.
     * @param availables     an array of Labels to display availability status.
     */
    public DocumentDisplayManager(BookManagement bookManagement, LibraryService libraryService,
            ImageView[] imageViews, Label[] names, Label[] authors, Label[] genres, Label[] availables) {
        this.bookManagement = bookManagement;
        this.libraryService = libraryService;
        this.imageViews = imageViews;
        this.names = names;
        this.authors = authors;
        this.genres = genres;
        this.availables = availables;
    }

    /**
     * Displays a set of documents based on the given starting ID and direction.
     *
     * @param startId  the starting document ID for fetching books.
     * @param isLatest a flag indicating whether to fetch the latest or oldest
     *                 books.
     *                 - true: fetch the latest books.
     *                 - false: fetch the oldest books.
     */
    public void showDocuments(int startId, boolean isLatest) {
        int numOfBooks = 4;
        Book[] bookList = new Book[numOfBooks];
        int currentIdDoc = startId;
        int latestID = Integer.parseInt(libraryService.getCurrentID());

        while (currentIdDoc > 0 && (isLatest ? currentIdDoc <= latestID : currentIdDoc > 0)) {
            String id = String.format("%09d", currentIdDoc);
            Book book = bookManagement.getDocument(id);
            if (book != null) {
                bookList[4 - numOfBooks] = book;
                numOfBooks--;
                if (numOfBooks <= 0)
                    break;
            }
            currentIdDoc = isLatest ? currentIdDoc - 1 : currentIdDoc + 1;
        }

        displayBooks(bookList);
    }

    /**
     * Displays book information in the corresponding UI components.
     *
     * @param bookList an array of books to be displayed.
     */
    private void displayBooks(Book[] bookList) {
        for (int i = 0; i < bookList.length; i++) {
            Book book = bookList[i];
            if (book != null) {
                names[i].setText(book.getName());
                names[i].setStyle("-fx-font-weight: bold;");
                authors[i].setText(book.getAuthor());
                genres[i].setText(book.getGroup());
                availables[i].setText(book.getIsAvailable() ? "Yes" : "No");

                String linkImg = book.getImagePreview();
                if (!linkImg.isEmpty()) {
                    Image cachedImage = getCachedImage(linkImg);
                    if (cachedImage.getProgress() < 1.0) {
                        loadImageAsync(linkImg, imageViews[i]);
                    } else {
                        imageViews[i].setImage(cachedImage);
                    }
                }
            } else {
                System.out.println("Current Book is Empty");
            }
        }
    }

    /**
     * Retrieves an image from the cache or loads it if not available.
     *
     * @param imageUrl the URL of the image to fetch.
     * @return the Image object, either from the cache or newly loaded.
     */
    private Image getCachedImage(String imageUrl) {
        if (imageCache.containsKey(imageUrl)) {
            return imageCache.get(imageUrl);
        }
        Image image = new Image(imageUrl, true);
        imageCache.put(imageUrl, image);
        return image;
    }

    /**
     * Loads an image asynchronously and sets it to the specified ImageView.
     *
     * @param imageUrl  the URL of the image to load.
     * @param imageView the ImageView to display the loaded image.
     */
    private void loadImageAsync(String imageUrl, ImageView imageView) {
        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() {
                return new Image(imageUrl, true);
            }
        };

        loadImageTask.setOnSucceeded(event -> {
            Image image = loadImageTask.getValue();
            imageCache.put(imageUrl, image); // Cache the loaded image
            imageView.setImage(image);
        });

        loadImageTask.setOnFailed(event -> System.out.println("Failed to load image: " + imageUrl));

        new Thread(loadImageTask).start();
    }
}