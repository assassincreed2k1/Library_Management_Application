package com.library;

import com.library.model.Person.Member;
import com.library.model.doc.Book;
import com.library.model.doc.Magazine;
import com.library.model.doc.Newspaper;

import com.library.service.LibraryService;
import com.library.service.BookManagement;
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

        TextField bookIBSNInput = new TextField();
        bookIBSNInput.setPromptText("IBSN");
        grid.add(bookIBSNInput, 0, 4);

        Button addBookButton = new Button("Add Book");
        grid.add(addBookButton, 0, 5);

        // Add book on button click
        addBookButton.setOnAction(e -> {
            Book book = new Book(libraryService.generateID(),
                                bookNameInput.getText(),
                                bookGroupInput.getText(),
                                bookIBSNInput.getText(),
                                bookAuthorInput.getText());

            bookManager.addDocuments(book);
            System.out.println("Added Book: " + book.getName());
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
            Magazine magazine = new Magazine(libraryService.generateID(),
                                            magazineNameInput.getText(),
                                            magazineGroupInput.getText(),
                                            magazinePublisherInput.getText());

            magazineManager.addDocuments(magazine);
            System.out.println("Added Magazine: " + magazine.getName());
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
            Newspaper newspaper = new Newspaper(libraryService.generateID(),
                                                newspaperNameInput.getText(),
                                                newspaperGroupInput.getText(),
                                                newspaperSourceInput.getText(),
                                                newspaperRegionInput.getText());

            newspaperManager.addDocuments(newspaper);
            System.out.println("Added Newspaper: " + newspaper.getName());
        });

        // Create scene and show the stage
        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        //launch(args);
        MemberManagement memberManagement = new MemberManagement();
        // Giả sử member đã tồn tại
            String membershipId = "M100";
            Member mem = memberManagement.getMemberInfo(membershipId);
            System.out.println(mem.getDetails());
    }
}
