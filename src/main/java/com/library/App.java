package com.library;

import com.library.model.Person.Librarian;
import com.library.model.Person.Member;
import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;

import com.library.service.LibraryService;
import com.library.service.APIService;
import com.library.service.BookManagement;
import com.library.service.LibrarianManagement;
import com.library.service.MagazineManagement;
import com.library.service.NewsPaperManagament;
import com.library.service.MemberManagement;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.json.JSONArray;
import org.json.JSONObject;

public class App extends Application {
    LibraryService libraryService = new LibraryService();
    BookManagement bookManager = new BookManagement();
    MagazineManagement magazineManager = new MagazineManagement();
    NewsPaperManagament newspaperManager = new NewsPaperManagament();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Management System");

        // Grid layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Labels and input fields for Book
        Label bookLabel = new Label("Book");
        grid.add(bookLabel, 0, 0);

        TextField bookNameInput = new TextField();
        bookNameInput.setPromptText("Name");
        grid.add(bookNameInput, 0, 1);

        TextField bookGroupInput = new TextField();
        bookGroupInput.setPromptText("Group");
        grid.add(bookGroupInput, 0, 2);

        TextField bookAuthorInput = new TextField();
        bookAuthorInput.setPromptText("Author");
        grid.add(bookAuthorInput, 0, 3);

        TextField bookISBNInput = new TextField();
        bookISBNInput.setPromptText("ISBN");
        grid.add(bookISBNInput, 0, 4);

        TextField publishDateInput = new TextField();
        publishDateInput.setPromptText("Publish Date");
        grid.add(publishDateInput, 0, 5);

        Button addBookButton = new Button("Add Book");
        grid.add(addBookButton, 0, 6);

        // Add book on button click
        addBookButton.setOnAction(e -> {
            JSONObject getAPIBook = APIService.getBookInfoByISBN(bookISBNInput.getText());

            if (getAPIBook != null && getAPIBook.has("ISBN:" + bookISBNInput.getText())) {
                JSONObject bookData = getAPIBook.getJSONObject("ISBN:" + bookISBNInput.getText());

                String bookName = bookData.optString("title", "Unknown Title");

                String bookAuthor = "";
                if (bookData.has("authors")) {
                    JSONArray authorsArray = bookData.getJSONArray("authors");
                    if (authorsArray.length() > 0) {
                        bookAuthor = authorsArray.getJSONObject(0).optString("name", "Unknown Author");
                    }
                } else {
                    bookAuthor = "Unknown Author";
                }

                String publishDate = bookData.optString("publish_date", "Unknown Date");

                String bookGroup = "General";
                if (bookData.has("subjects")) {
                    JSONArray subjectsArray = bookData.getJSONArray("subjects");
                    if (subjectsArray.length() > 0) {
                        bookGroup = subjectsArray.getJSONObject(0).optString("name", "General");
                    }
                }

                Book book = new Book(libraryService.generateID(3), // Type 3 for Book
                        bookName,
                        bookGroup,
                        bookISBNInput.getText(),
                        bookAuthor,
                        publishDate);

                bookManager.addDocuments(book);
                System.out.println("Added Book from API: " + book.getName());
            } else {
                Book book = new Book(libraryService.generateID(3), // Type 3 for Book
                        bookNameInput.getText(),
                        bookGroupInput.getText(),
                        bookISBNInput.getText(),
                        bookAuthorInput.getText(),
                        publishDateInput.getText());

                bookManager.addDocuments(book);
                System.out.println("Added Book: " + book.getName());
            }
        });

        TextField bookIDinput = new TextField();
        bookIDinput.setPromptText("ID");
        grid.add(bookIDinput, 0, 8);

        Button updateButton = new Button("Update Book");
        grid.add(updateButton, 0, 9);

        updateButton.setOnAction(e -> {
            Book upBook = new Book(bookIDinput.getText(),
                    bookNameInput.getText(),
                    bookGroupInput.getText(),
                    bookISBNInput.getText(),
                    bookAuthorInput.getText(),
                    publishDateInput.getText());

            bookManager.updateDocuments(upBook);
            System.out.println("Updated Book: " + upBook.getName());
        });

        Button removeBookButton = new Button("Remove book");
        grid.add(removeBookButton, 0, 10);

        removeBookButton.setOnAction(e -> {
            Book rmBook = new Book();
            rmBook.setID(bookIDinput.getText());
            bookManager.removeDocument(rmBook);
            System.out.println("Removed Book with ID: " + bookIDinput.getText());
        });

        // Labels and input fields for Magazine
        Label magazineLabel = new Label("Magazine");
        grid.add(magazineLabel, 1, 0);

        TextField magazineNameInput = new TextField();
        magazineNameInput.setPromptText("Name");
        grid.add(magazineNameInput, 1, 1);

        TextField magazineGroupInput = new TextField();
        magazineGroupInput.setPromptText("Group");
        grid.add(magazineGroupInput, 1, 2);

        TextField magazinePublisherInput = new TextField();
        magazinePublisherInput.setPromptText("Publisher");
        grid.add(magazinePublisherInput, 1, 3);

        Button addMagazineButton = new Button("Add Magazine");
        grid.add(addMagazineButton, 1, 5);

        // Add magazine on button click
        addMagazineButton.setOnAction(e -> {
            Magazine magazine = new Magazine(libraryService.generateID(4), // Type 4 for Magazine
                    magazineNameInput.getText(),
                    magazineGroupInput.getText(),
                    magazinePublisherInput.getText());

            magazineManager.addDocuments(magazine);
            System.out.println("Added Magazine: " + magazine.getName());
        });

        TextField magazineIdInput = new TextField();
        magazineIdInput.setPromptText("ID");
        grid.add(magazineIdInput, 1, 6);

        Button updateMagazineButton = new Button("Update Magazine");
        grid.add(updateMagazineButton, 1, 7);

        updateMagazineButton.setOnAction(e -> {
            Magazine upMagazine = new Magazine(magazineIdInput.getText(),
                    magazineNameInput.getText(),
                    magazineGroupInput.getText(),
                    magazinePublisherInput.getText());
            magazineManager.updateDocuments(upMagazine);
            System.out.println("Updated Magazine: " + upMagazine.getName());
        });

        Button removeMagazineButton = new Button("Remove Magazine");
        grid.add(removeMagazineButton, 1, 8);

        removeMagazineButton.setOnAction(e -> {
            Magazine rmMgz = new Magazine();
            rmMgz.setID(magazineIdInput.getText());
            magazineManager.removeDocument(rmMgz);
            System.out.println("Removed Magazine with ID: " + magazineIdInput.getText());
        });

        // Labels and input fields for Newspaper
        Label newspaperLabel = new Label("Newspaper");
        grid.add(newspaperLabel, 2, 0);

        TextField newspaperNameInput = new TextField();
        newspaperNameInput.setPromptText("Name");
        grid.add(newspaperNameInput, 2, 1);

        TextField newspaperGroupInput = new TextField();
        newspaperGroupInput.setPromptText("Group");
        grid.add(newspaperGroupInput, 2, 2);

        TextField newspaperSourceInput = new TextField();
        newspaperSourceInput.setPromptText("Source");
        grid.add(newspaperSourceInput, 2, 3);

        TextField newspaperRegionInput = new TextField();
        newspaperRegionInput.setPromptText("Region");
        grid.add(newspaperRegionInput, 2, 4);

        Button addNewspaperButton = new Button("Add Newspaper");
        grid.add(addNewspaperButton, 2, 5);

        // Add newspaper on button click
        addNewspaperButton.setOnAction(e -> {
            Newspaper newspaper = new Newspaper(libraryService.generateID(5), // Type 5 for Newspaper
                    newspaperNameInput.getText(),
                    newspaperGroupInput.getText(),
                    newspaperSourceInput.getText(),
                    newspaperRegionInput.getText());

            newspaperManager.addDocuments(newspaper);
            System.out.println("Added Newspaper: " + newspaper.getName());
        });

        TextField newspaperIdInput = new TextField();
        newspaperIdInput.setPromptText("ID");
        grid.add(newspaperIdInput, 2, 6);

        Button updateNewspaperButton = new Button("Update Newspaper");
        grid.add(updateNewspaperButton, 2, 7);

        updateNewspaperButton.setOnAction(e -> {
            Newspaper upNews = new Newspaper(newspaperIdInput.getText(),
                    newspaperNameInput.getText(),
                    newspaperGroupInput.getText(),
                    newspaperSourceInput.getText(),
                    newspaperRegionInput.getText());

            newspaperManager.updateDocuments(upNews);
            System.out.println("Updated Newspaper: " + upNews.getName());
        });

        Button removeNewspaperButton = new Button("Remove Newspaper");
        grid.add(removeNewspaperButton, 2, 8);

        removeNewspaperButton.setOnAction(e -> {
            Newspaper news = new Newspaper();
            news.setID(newspaperIdInput.getText());
            newspaperManager.removeDocument(news);
            System.out.println("Removed Newspaper with ID: " + newspaperIdInput.getText());
        });

        // Create scene and show the stage
        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        //launch(args);
        LibraryService lib = new LibraryService();
        lib.createDataBase();
    }
}
